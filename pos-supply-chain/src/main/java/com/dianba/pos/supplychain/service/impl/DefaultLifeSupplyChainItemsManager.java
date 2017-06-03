package com.dianba.pos.supplychain.service.impl;

import com.dianba.pos.item.po.LifeBarcodeRelationship;
import com.dianba.pos.item.po.LifeItemTemplate;
import com.dianba.pos.item.po.LifeItemUnit;
import com.dianba.pos.item.repository.LifeBarcodeRelationshipJpaRepository;
import com.dianba.pos.item.repository.LifeItemTemplateJpaRepository;
import com.dianba.pos.item.repository.LifeItemUnitJpaRepository;
import com.dianba.pos.passport.po.LifePassportAddress;
import com.dianba.pos.passport.repository.LifePassportAddressJpaRepository;
import com.dianba.pos.supplychain.po.LifeSupplyChainItems;
import com.dianba.pos.supplychain.repository.LifeSupplyChainItemsJpaRepository;
import com.dianba.pos.supplychain.service.LifeSupplyChainItemsManager;
import com.dianba.pos.supplychain.service.LifeSupplyChainWarehouseManager;
import com.dianba.pos.supplychain.vo.Items;
import com.dianba.pos.supplychain.vo.MatchItems;
import com.xlibao.common.GlobalAppointmentOptEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DefaultLifeSupplyChainItemsManager implements LifeSupplyChainItemsManager {

    @Autowired
    private LifeBarcodeRelationshipJpaRepository barcodeRelationshipJpaRepository;
    @Autowired
    private LifeSupplyChainItemsJpaRepository supplyChainItemsJpaRepository;
    @Autowired
    private LifeSupplyChainWarehouseManager warehouseOrgManager;
    @Autowired
    private LifePassportAddressJpaRepository passportAddressJpaRepository;
    @Autowired
    private LifeItemTemplateJpaRepository itemTemplateJpaRepository;
    @Autowired
    private LifeItemUnitJpaRepository itemUnitJpaRepository;

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
        Map<String, LifeBarcodeRelationship> matchSourceBarcodes = new HashMap<>();
        // 组合 用于获取商品模版数据
        List<String> sourceBarcodes = new ArrayList<>();
        // 按目标进行分组
        for (LifeBarcodeRelationship lifeBarcodeRelationship : batchLifeBarcodeRelationships) {
            matchSourceBarcodes.put(lifeBarcodeRelationship.getTargetBarcode(), lifeBarcodeRelationship);
            sourceBarcodes.add(lifeBarcodeRelationship.getSourceBarcode());
        }
        //商品模板
        List<LifeItemTemplate> lifeItemTemplates = itemTemplateJpaRepository.findByBarcodeIn(sourceBarcodes);
        List<Long> itemTemplateIds = new ArrayList<>();
        for (LifeItemTemplate template : lifeItemTemplates) {
            itemTemplateIds.add(template.getId());
        }
        //供应链商品
        List<LifeSupplyChainItems> supplyChainItemsList = supplyChainItemsJpaRepository
                .findByWarehouseIdAndItemTemplateIdInAndStatusAndRestrictionQuantity(nearbyWarehouseId
                        , itemTemplateIds, GlobalAppointmentOptEnum.LOGIC_TRUE.getKey(), 1);

        for (LifeSupplyChainItems lifeSupplyChainItems : supplyChainItemsList) {
            for (LifeItemTemplate lifeItemTemplate : lifeItemTemplates) {
                if (lifeSupplyChainItems.getItemTemplateId().longValue() == lifeItemTemplate.getId().longValue()) {
                    LifeBarcodeRelationship barcodeRelationship
                            = matchSourceBarcodes.get(lifeItemTemplate.getBarcode());
                    MatchItems matchItems = new MatchItems();
                    Items items = new Items();
                    items.setMinSales(lifeSupplyChainItems.getMinimumSellCount());
                    items.setId(lifeSupplyChainItems.getId());
                    items.setName(lifeItemTemplate.getName());
                    items.setImage(lifeItemTemplate.getImageUrl());
                    items.setStock(lifeSupplyChainItems.getStock());
                    items.setRetailPrice(BigDecimal.valueOf(lifeSupplyChainItems.getMarketPrice()));
                    items.setPrice(new BigDecimal(lifeSupplyChainItems.getSellPrice())
                            .setScale(2, BigDecimal.ROUND_HALF_UP));
                    LifeItemUnit itemUnit = itemUnitJpaRepository.findOne(lifeItemTemplate.getUnitId());
                    items.setUnit(itemUnit.getTitle());
                    items.setStandard(barcodeRelationship.getRelationCoefficient());
                    matchItems.getItems().add(items);
                    matchItems.setBarcode(lifeItemTemplate.getBarcode());
                    matchItemsList.add(matchItems);
                }
            }
        }
        return matchItemsList;
    }

}