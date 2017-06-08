package com.dianba.pos.supplychain.service.impl;

import com.dianba.pos.base.exception.PosNullPointerException;
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
import com.dianba.pos.supplychain.vo.ItemsVo;
import com.dianba.pos.supplychain.vo.MatchItemsVo;
import com.dianba.pos.supplychain.vo.WarehouseItemsVo;
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
    public WarehouseItemsVo matchItemsByBarcode(Long passportId, String barcodes) {
        WarehouseItemsVo warehouseItemsVo = new WarehouseItemsVo();
        List<MatchItemsVo> matchItemsList = new ArrayList<>();
        //获取商家地址
        LifePassportAddress passportAddress = passportAddressJpaRepository.findByPassportId(passportId);
        if (passportAddress == null) {
            throw new PosNullPointerException("商家地址信息不存在！" + passportId);
        }
        Long nearbyWarehouseId = warehouseOrgManager.getNearbyWarehouse(
                passportAddress.getLatitude(), passportAddress.getLongitude());
        if (nearbyWarehouseId <= 0) {
            // 附近没有子仓
            return warehouseItemsVo;
        }
        // 获取本次目标的内容对应于供应链端中存在的记录
        List<LifeBarcodeRelationship> batchLifeBarcodeRelationships
                = barcodeRelationshipJpaRepository.findByTargetBarcodeIn(Arrays.asList(barcodes.split(",")));
        if (batchLifeBarcodeRelationships == null || batchLifeBarcodeRelationships.isEmpty()) {
            // 供应链没有对应的商品可以出售
            return warehouseItemsVo;
        }
        Map<String, LifeBarcodeRelationship> matchSourceBarcodes = new HashMap<>();
        Map<String, String> barcodeRelateionShip = new HashMap<>();
        for (LifeBarcodeRelationship lifeBarcodeRelationship : batchLifeBarcodeRelationships) {
            barcodeRelateionShip.put(lifeBarcodeRelationship.getSourceBarcode()
                    , lifeBarcodeRelationship.getTargetBarcode());
            matchSourceBarcodes.put(lifeBarcodeRelationship.getSourceBarcode(), lifeBarcodeRelationship);
        }
        //商品模板
        List<LifeItemTemplate> lifeItemTemplates = itemTemplateJpaRepository
                .findByBarcodeIn(new ArrayList<>(matchSourceBarcodes.keySet()));
        List<Long> itemTemplateIds = new ArrayList<>();
        List<Long> unitIds = new ArrayList<>();
        for (LifeItemTemplate template : lifeItemTemplates) {
            itemTemplateIds.add(template.getId());
            unitIds.add(template.getUnitId());
        }
        //供应链商品
        List<LifeSupplyChainItems> supplyChainItemsList = supplyChainItemsJpaRepository
                .findByWarehouseIdAndItemTemplateIdInAndStatusAndRestrictionQuantity(nearbyWarehouseId
                        , itemTemplateIds, GlobalAppointmentOptEnum.LOGIC_TRUE.getKey(), -1);

        Map<Long, LifeItemUnit> lifeItemUnitMap = new HashMap<>();
        List<LifeItemUnit> itemUnits = itemUnitJpaRepository.findAll(unitIds);
        for (LifeItemUnit itemUnit : itemUnits) {
            lifeItemUnitMap.put(itemUnit.getId(), itemUnit);
        }
        for (LifeSupplyChainItems lifeSupplyChainItems : supplyChainItemsList) {
            for (LifeItemTemplate lifeItemTemplate : lifeItemTemplates) {
                if (lifeSupplyChainItems.getItemTemplateId().longValue() == lifeItemTemplate.getId().longValue()) {
                    LifeBarcodeRelationship barcodeRelationship
                            = matchSourceBarcodes.get(lifeItemTemplate.getBarcode());
                    MatchItemsVo matchItems = new MatchItemsVo();
                    ItemsVo items = new ItemsVo();
                    items.setMinSales(lifeSupplyChainItems.getMinimumSellCount());
                    items.setId(lifeSupplyChainItems.getId());
                    items.setName(lifeItemTemplate.getName());
                    items.setImage(lifeItemTemplate.getImageUrl());
                    items.setStock(lifeSupplyChainItems.getStock());
                    BigDecimal retailPrice = BigDecimal.valueOf(lifeSupplyChainItems.getMarketPrice())
                            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                    items.setRetailPrice(retailPrice);
                    BigDecimal price = BigDecimal.valueOf(lifeSupplyChainItems.getSellPrice())
                            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                    items.setPrice(price);
                    LifeItemUnit itemUnit = lifeItemUnitMap.get(lifeItemTemplate.getUnitId());
                    if (itemUnit != null) {
                        items.setUnit(itemUnit.getTitle());
                    }
                    items.setStandard(barcodeRelationship.getRelationCoefficient());
                    matchItems.getItems().add(items);
                    matchItems.setBarcode(lifeItemTemplate.getBarcode());
                    matchItemsList.add(matchItems);
                }
            }
        }
        warehouseItemsVo.setWarehouseId(nearbyWarehouseId);
        warehouseItemsVo.setBarcodeRelateionShip(barcodeRelateionShip);
        warehouseItemsVo.setMatchItems(matchItemsList);
        return warehouseItemsVo;
    }

}
