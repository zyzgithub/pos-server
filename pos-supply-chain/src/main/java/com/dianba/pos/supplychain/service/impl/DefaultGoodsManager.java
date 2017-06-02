package com.dianba.pos.supplychain.service.impl;

import com.dianba.pos.item.po.LifeBarcodeRelationship;
import com.dianba.pos.item.repository.LifeBarcodeRelationshipJpaRepository;
import com.dianba.pos.item.repository.LifeItemUnitJpaRepository;
import com.dianba.pos.merchant.repository.MerchantJpaRepository;
import com.dianba.pos.passport.po.LifePassportAddress;
import com.dianba.pos.passport.repository.LifePassportAddressJpaRepository;
import com.dianba.pos.supplychain.mapper.WarehouseGoodsMapper;
import com.dianba.pos.supplychain.po.Goods;
import com.dianba.pos.supplychain.po.LifeSupplyChainWarehouse;
import com.dianba.pos.supplychain.po.WarehouseGoods;
import com.dianba.pos.supplychain.repository.LifeSupplyChainWarehouseJpaRepository;
import com.dianba.pos.supplychain.repository.SupplyChainGoodsJpaRepository;
import com.dianba.pos.supplychain.repository.WarehouseGoodsJpaRepository;
import com.dianba.pos.supplychain.service.GoodsManager;
import com.dianba.pos.supplychain.service.WarehouseOrgManager;
import com.dianba.pos.supplychain.vo.MatchItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;

@Service
public class DefaultGoodsManager implements GoodsManager {

    @Autowired
    private LifeBarcodeRelationshipJpaRepository barcodeRelationshipJpaRepository;
    @Autowired
    private SupplyChainGoodsJpaRepository supplyChainGoodsJpaRepository;
    @Autowired
    private WarehouseGoodsMapper warehouseGoodsMapper;
    @Autowired
    private WarehouseGoodsJpaRepository warehouseGoodsJpaRepository;
    @Autowired
    private LifeSupplyChainWarehouseJpaRepository warehouseJpaRepository;
    @Autowired
    private LifeItemUnitJpaRepository goodsUnitJpaRepository;
    @Autowired
    private MerchantJpaRepository merchantJpaRepository;
    @Autowired
    private WarehouseOrgManager warehouseOrgManager;

    @Autowired
    private LifePassportAddressJpaRepository passportAddressJpaRepository;

    @Override
    public List<MatchItems> matchItemsByBarcode(Long passportId, String barcodes) {
        List<MatchItems> matchItemsList = new ArrayList<>();
        //获取商家地址
        LifePassportAddress passportAddress = passportAddressJpaRepository.findByPassportId(passportId);
        Long nearbyWarehouseId = warehouseOrgManager.getNearbyWarehouse(
                passportAddress.getLatitude(), passportAddress.getLongitude());
        if (nearbyWarehouseId <= 0) {
            // 附近没有子仓
            return matchItemsList;
        }
        // 获取本次目标的内容对应于供应链端中存在的记录
        List<LifeBarcodeRelationship> batchLifeBarcodeRelationships
                = barcodeRelationshipJpaRepository.findByTargetBarcodeIn(Arrays.asList(barcodes.split(",")));
        if (batchLifeBarcodeRelationships == null || batchLifeBarcodeRelationships.isEmpty()) {
            // 供应链没有对应的商品可以出售
            return matchItemsList;
        }
        Map<String, List<LifeBarcodeRelationship>> matchSourceBarcodes = new HashMap<>();
        // 组合 用于获取商品模版数据
        StringBuilder sourceBarcodes = new StringBuilder();
        // 按目标进行分组
        for (LifeBarcodeRelationship lifeBarcodeRelationship : batchLifeBarcodeRelationships) {
            List<LifeBarcodeRelationship> lifeBarcodeRelationships
                    = matchSourceBarcodes.get(lifeBarcodeRelationship.getTargetBarcode());
            if (lifeBarcodeRelationships == null) {
                lifeBarcodeRelationships = new ArrayList<LifeBarcodeRelationship>();
                matchSourceBarcodes.put(lifeBarcodeRelationship.getTargetBarcode(), lifeBarcodeRelationships);
            }
            lifeBarcodeRelationships.add(lifeBarcodeRelationship);

            sourceBarcodes.append(lifeBarcodeRelationship.getSourceBarcode()).append(",");
        }
        sourceBarcodes = sourceBarcodes.delete(sourceBarcodes.length() - 1, sourceBarcodes.length());
        // 获取供应链中所有出售中的商品模版
        List<Goods> itemTemplates = supplyChainGoodsJpaRepository
                .findByBarcodeIn(Arrays.asList(sourceBarcodes.toString().split(",")));
        Map<String, Goods> itemTemplateMap = new HashMap<String, Goods>();
        StringBuilder builder = new StringBuilder();
        // 重组为映射表关系
        for (Goods itemTemplate : itemTemplates) {
//            itemTemplateMap.put(itemTemplate.getBarcode(), itemTemplate);
            builder.append(itemTemplate.getId()).append(",");
        }
        builder = builder.delete(builder.length() - 1, builder.length());

        List<WarehouseGoods> warehouseGoods
                = warehouseGoodsMapper.getItemsByTemplate(nearbyWarehouseId, builder.toString());
        Map<Integer, WarehouseGoods> warehouseGoodsMap = new HashMap<Integer, WarehouseGoods>();
        for (WarehouseGoods item : warehouseGoods) {
            warehouseGoodsMap.put(item.getGoodsId(), item);
        }

        LifeSupplyChainWarehouse warehouse = warehouseJpaRepository.findOne(nearbyWarehouseId);
        Iterator<Entry<String, List<LifeBarcodeRelationship>>> entrys = matchSourceBarcodes.entrySet().iterator();
        while (entrys.hasNext()) {
            MatchItems matchItems = new MatchItems();
            Entry<String, List<LifeBarcodeRelationship>> entry = entrys.next();
            List<LifeBarcodeRelationship> lifeBarcodeRelationships = entry.getValue();
            for (LifeBarcodeRelationship lifeBarcodeRelationship : lifeBarcodeRelationships) {
                Goods goods = itemTemplateMap.get(lifeBarcodeRelationship.getSourceBarcode());
                if (goods == null) {
                    continue;
                }
                WarehouseGoods warehouseGood = warehouseGoodsMap.get(goods.getId());
                if (warehouseGood == null) {
                    continue;
                }
                int parentStock = 0;
//                if (warehouse.getPid() > 0) {
//                    List<WarehouseGoods> parentItems = warehouseGoodsJpaRepository
//                            .findByWarehouseIdAndGoodsId(warehouse.getPid(), goods.getId());
//                    if (parentItems != null && !parentItems.isEmpty()) {
//                        WarehouseGoods parentItem = parentItems.get(0);
//                        parentStock = parentItem.getInventory();
//                    }
//                }
//                int stock = warehouseGood.getInventory() + (parentStock < 0 ? 0 : parentStock);
//                Items items = new Items();
//                items.setMinSales(warehouseGood.getMinSales());
////                items.setId(warehouseGood.getId());
//                items.setName(goods.getName());
//                items.setImage(goods.getImg());
//                items.setStock(stock < 0 ? 0 : stock);
//                items.setRetailPrice(BigDecimal.valueOf(warehouseGood.getRetailPrice()));
//                items.setPrice(new BigDecimal(warehouseGood.getMarketPrice())
//                        .setScale(2, BigDecimal.ROUND_HALF_UP));
////                GoodsUnit itemUnit = goodsUnitJpaRepository.findOne(barcodeRelationship.getSourceUnitId());
////                items.setUnit(itemUnit == null ? "箱" : itemUnit.getName());
//                items.setStandard(lifeBarcodeRelationship.getRelationCoefficient());
//                matchItems.getItems().add(items);
                matchItems.setBarcode(entry.getKey());
                matchItemsList.add(matchItems);
            }
        }
        return matchItemsList;
    }

}
