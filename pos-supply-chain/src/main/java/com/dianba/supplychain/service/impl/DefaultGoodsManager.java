package com.dianba.supplychain.service.impl;

import com.dianba.pos.merchant.po.Merchant;
import com.dianba.pos.merchant.repository.MerchantJpaRepository;
import com.dianba.supplychain.mapper.WarehouseGoodsMapper;
import com.dianba.supplychain.po.*;
import com.dianba.supplychain.repository.*;
import com.dianba.supplychain.service.GoodsManager;
import com.dianba.supplychain.service.WarehouseOrgManager;
import com.dianba.supplychain.vo.Items;
import com.dianba.supplychain.vo.MatchItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public List<MatchItems> matchItemsByBarcode(int userId, String barcodes) {
        List<MatchItems> matchItemsList = new ArrayList<>();
        Merchant merchant = merchantJpaRepository.findByUserId((long) userId);
        if (merchant == null) {
            return matchItemsList;
        }
        int nearbyWarehouseId = warehouseOrgManager.getNearbyWarehouse(
                merchant.getLatitude().doubleValue(), merchant.getLongitude().doubleValue()); // 183; //
        if (nearbyWarehouseId <= 0) {
            // 附近没有子仓
            return matchItemsList;
        }

        // 获取本次目标的内容对应于供应链端中存在的记录
        List<BarcodeRelationship> batchBarcodeRelationships
                = barcodeRelationshipJpaRepository.findByTargetBarcodeIn(Arrays.asList(barcodes.split(",")));
        if (batchBarcodeRelationships == null || batchBarcodeRelationships.isEmpty()) {
            // 供应链没有对应的商品可以出售
            return matchItemsList;
        }
        Map<String, List<BarcodeRelationship>> matchSourceBarcodes = new HashMap<String, List<BarcodeRelationship>>();
        // 组合 用于获取商品模版数据
        StringBuilder sourceBarcodes = new StringBuilder();
        // 按目标进行分组
        for (BarcodeRelationship barcodeRelationship : batchBarcodeRelationships) {
            List<BarcodeRelationship> barcodeRelationships
                    = matchSourceBarcodes.get(barcodeRelationship.getTargetBarcode());
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
                .findByBarcodeIn(Arrays.asList(sourceBarcodes.toString().split(",")));
        Map<String, Goods> itemTemplateMap = new HashMap<String, Goods>();
        StringBuilder builder = new StringBuilder();
        // 重组为映射表关系
        for (Goods itemTemplate : itemTemplates) {
            itemTemplateMap.put(itemTemplate.getBarcode(), itemTemplate);
            builder.append(itemTemplate.getId()).append(",");
        }
        builder = builder.delete(builder.length() - 1, builder.length());

        List<WarehouseGoods> warehouseGoods
                = warehouseGoodsMapper.getItemsByTemplate(nearbyWarehouseId, builder.toString());
        Map<Integer, WarehouseGoods> warehouseGoodsMap = new HashMap<Integer, WarehouseGoods>();
        for (WarehouseGoods item : warehouseGoods) {
            warehouseGoodsMap.put(item.getGoodsId(), item);
        }

        Warehouse warehouse = warehouseJpaRepository.findOne(nearbyWarehouseId);
        Iterator<Entry<String, List<BarcodeRelationship>>> entrys = matchSourceBarcodes.entrySet().iterator();
        while (entrys.hasNext()) {
            MatchItems matchItems = new MatchItems();
            Entry<String, List<BarcodeRelationship>> entry = entrys.next();
            List<BarcodeRelationship> barcodeRelationships = entry.getValue();
            for (BarcodeRelationship barcodeRelationship : barcodeRelationships) {
                Goods goods = itemTemplateMap.get(barcodeRelationship.getSourceBarcode());
                if (goods == null) {
                    continue;
                }
                WarehouseGoods warehouseGood = warehouseGoodsMap.get(goods.getId());
                if (warehouseGood == null) {
                    continue;
                }
                int parentStock = 0;
                if (warehouse.getPid() > 0) {
                    List<WarehouseGoods> parentItems = warehouseGoodsJpaRepository
                            .findByWarehouseIdAndGoodsId(warehouse.getPid(), goods.getId());
                    if (parentItems != null && !parentItems.isEmpty()) {
                        WarehouseGoods parentItem = parentItems.get(0);
                        parentStock = parentItem.getInventory();
                    }
                }
                int stock = warehouseGood.getInventory() + (parentStock < 0 ? 0 : parentStock);
                Items items = new Items();
                items.setMinSales(warehouseGood.getMinSales());
                items.setId(warehouseGood.getId());
                items.setName(goods.getName());
                items.setImage(goods.getImg());
                items.setStock(stock < 0 ? 0 : stock);
                items.setPrice(new BigDecimal(warehouseGood.getMarketPrice())
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                GoodsUnit itemUnit = goodsUnitJpaRepository.findOne(barcodeRelationship.getSourceUnitId());
                items.setUnit(itemUnit == null ? "箱" : itemUnit.getName());
                items.setStandard(barcodeRelationship.getTargetConversionCount());
                matchItems.getItems().add(items);
                matchItems.setBarcode(entry.getKey());
                matchItemsList.add(matchItems);
            }
        }
        return matchItemsList;
    }

}
