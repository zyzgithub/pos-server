package com.dianba.supplychain.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.merchant.po.Merchant;
import com.dianba.pos.merchant.repository.MerchantJpaRepository;
import com.dianba.supplychain.mapper.WarehouseGoodsMapper;
import com.dianba.supplychain.po.*;
import com.dianba.supplychain.repository.*;
import com.dianba.supplychain.service.GoodsManager;
import com.dianba.supplychain.service.WarehouseOrgManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service
public class DefaultGoodsManager implements GoodsManager {

    @Autowired
    private BarcodeRelationshipJpaRepository barcodeRelationshipJpaRepository;
    @Autowired
    private SupplyChainGoodsJpaRepository supplyChainGoodsJpaRepository;
    @Autowired
    private WarehouseGoodsMapper warehouseGoodsMapper;
    @Autowired
    private WarehouseGoodsJpaRepository warehouseGoodsJpaRepository;
    @Autowired
    private WarehouseJpaRepository warehouseJpaRepository;
    @Autowired
    private GoodsUnitJpaRepository goodsUnitJpaRepository;
    @Autowired
    private MerchantJpaRepository merchantJpaRepository;
    @Autowired
    private WarehouseOrgManager warehouseOrgManager;

    @Override
    public JSONArray matchItems(int userId, String barcodes) {
        JSONArray relationships = new JSONArray();
        Merchant merchant = merchantJpaRepository.findByUserId((long) userId);
        if (merchant == null) {
            return relationships;
        }
        int nearbyWarehouseId = warehouseOrgManager.getNearbyWarehouse(
                merchant.getLatitude().doubleValue(), merchant.getLongitude().doubleValue()); // 183; //
        if (nearbyWarehouseId <= 0) {
            // 附近没有子仓
            return relationships;
        }

        // 获取本次目标的内容对应于供应链端中存在的记录
        List<BarcodeRelationship> batchBarcodeRelationships
                = barcodeRelationshipJpaRepository.findByTargetBarcode(Arrays.asList(barcodes.split(",")));
        if (batchBarcodeRelationships == null || batchBarcodeRelationships.isEmpty()) {
            // 供应链没有对应的商品可以出售
            return relationships;
        }
        Map<String, List<BarcodeRelationship>> matchSourceBarcodes = new HashMap<String, List<BarcodeRelationship>>();
        // 组合 用于获取商品模版数据
        StringBuilder sourceBarcodes = new StringBuilder();
        // 按目标进行分组
        for (BarcodeRelationship barcodeRelationship : batchBarcodeRelationships) {
            List<BarcodeRelationship> barcodeRelationships = matchSourceBarcodes.get(barcodeRelationship.getTargetBarcode());
            if (barcodeRelationships == null) {
                barcodeRelationships = new ArrayList<BarcodeRelationship>();
                matchSourceBarcodes.put(barcodeRelationship.getTargetBarcode(), barcodeRelationships);
            }
            barcodeRelationships.add(barcodeRelationship);

            sourceBarcodes.append(barcodeRelationship.getSourceBarcode()).append(",");
        }
        sourceBarcodes = sourceBarcodes.delete(sourceBarcodes.length() - 1, sourceBarcodes.length());
        // 获取供应链中所有出售中的商品模版
        List<Goods> itemTemplates = supplyChainGoodsJpaRepository
                .findByBarcode(Arrays.asList(sourceBarcodes.toString().split(",")));
        Map<String, Goods> itemTemplateMap = new HashMap<String, Goods>();
        StringBuilder builder = new StringBuilder();
        // 重组为映射表关系
        for (Goods itemTemplate : itemTemplates) {
            itemTemplateMap.put(itemTemplate.getBarcode(), itemTemplate);
            builder.append(itemTemplate.getId()).append(",");
        }
        builder = builder.delete(builder.length() - 1, builder.length());

        List<WarehouseGoods> items = warehouseGoodsMapper.getItemsByTemplate(nearbyWarehouseId, builder.toString());
        Map<Integer, WarehouseGoods> itemMap = new HashMap<Integer, WarehouseGoods>();
        for (WarehouseGoods item : items) {
            itemMap.put(item.getGoodsId(), item);
        }

        Warehouse warehouse = warehouseJpaRepository.findOne(nearbyWarehouseId);
        Iterator<Entry<String, List<BarcodeRelationship>>> entrys = matchSourceBarcodes.entrySet().iterator();
        while (entrys.hasNext()) {
            Entry<String, List<BarcodeRelationship>> entry = entrys.next();

            List<BarcodeRelationship> barcodeRelationships = entry.getValue();
            JSONArray itemArray = new JSONArray();
            for (BarcodeRelationship barcodeRelationship : barcodeRelationships) {
                Goods itemTemplate = itemTemplateMap.get(barcodeRelationship.getSourceBarcode());
                if (itemTemplate == null) {
                    continue;
                }
                WarehouseGoods item = itemMap.get(itemTemplate.getId());
                if (item == null) {
                    continue;
                }
                int parentStock = 0;
                if (warehouse.getPid() > 0) {
                    List<WarehouseGoods> parentItems = warehouseGoodsJpaRepository
                            .findByWarehouseIdAndGoodsId(warehouse.getPid(), itemTemplate.getId());
                    if (parentItems != null && !parentItems.isEmpty()) {
                        WarehouseGoods parentItem = parentItems.get(0);
                        parentStock = parentItem.getInventory();
                    }
                }
                int stock = item.getInventory() + (parentStock < 0 ? 0 : parentStock);
                JSONObject r = new JSONObject();
                r.put("id", item.getId());
                r.put("name", itemTemplate.getName());
                r.put("image", itemTemplate.getImg());
                r.put("stock", stock < 0 ? 0 : stock);
                r.put("price", item.getMarketPrice());

                GoodsUnit itemUnit = goodsUnitJpaRepository.findOne(barcodeRelationship.getSourceUnitId());
                r.put("unit", itemUnit == null ? "箱" : itemUnit.getName());
                r.put("standard", barcodeRelationship.getTargetConversionCount());
                itemArray.add(r);
            }
            if (itemArray.size() <= 0) {
                continue;
            }
            JSONObject relationship = new JSONObject();
            relationship.put("barcode", entry.getKey());
            relationship.put("items", itemArray);

            relationships.add(relationship);
        }
        return relationships;
    }

}