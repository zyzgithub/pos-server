package com.dianba.pos.purchase.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.item.mapper.PosItemMapper;
import com.dianba.pos.item.po.LifeItemTemplate;
import com.dianba.pos.item.po.LifeItemType;
import com.dianba.pos.item.po.LifeItemUnit;
import com.dianba.pos.item.po.PosItem;
import com.dianba.pos.item.repository.LifeItemTemplateJpaRepository;
import com.dianba.pos.item.repository.LifeItemTypeJpaRepository;
import com.dianba.pos.item.repository.LifeItemUnitJpaRepository;
import com.dianba.pos.passport.po.LifeAchieve;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.service.LifeAchieveManager;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.purchase.mapper.OneKeyPurchaseMapper;
import com.dianba.pos.purchase.pojo.OneKeyPurchase;
import com.dianba.pos.purchase.service.PurchaseManager;
import com.dianba.pos.purchase.support.PurchaseRemoteService;
import com.dianba.pos.supplychain.service.LifeSupplyChainItemsManager;
import com.dianba.pos.supplychain.vo.ItemsVo;
import com.dianba.pos.supplychain.vo.MatchItemsVo;
import com.dianba.pos.supplychain.vo.WarehouseItemsVo;
import com.xlibao.common.constant.device.DeviceTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DefaultPurchaseManager extends PurchaseRemoteService implements PurchaseManager {


    private static Logger logger = LogManager.getLogger(DefaultPurchaseManager.class);
    @Autowired
    private OneKeyPurchaseMapper oneKeyPurchaseMapper;
    @Autowired
    private LifeSupplyChainItemsManager lifeSupplyChainItemsManager;
    @Autowired
    private LifeItemTemplateJpaRepository itemTemplateJpaRepository;
    @Autowired
    private LifeItemTypeJpaRepository itemTypeJpaRepository;
    @Autowired
    private LifeItemUnitJpaRepository itemUnitJpaRepository;
    @Autowired
    private PosItemMapper posItemMapper;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private LifeAchieveManager lifeAchieveManager;

    public List<OneKeyPurchase> getWarnRepertoryItems(Long passportId) {
        List<OneKeyPurchase> oneKeyPurchases = oneKeyPurchaseMapper.findWarnSaleItems(passportId);
        List<Long> itemTemplateIds = new ArrayList<>();
        for (OneKeyPurchase oneKeyPurchase : oneKeyPurchases) {
            itemTemplateIds.add(oneKeyPurchase.getItemTemplateId());
        }
        List<PosItem> items = posItemMapper.findWarningRepertoryItemsByExclude(passportId, itemTemplateIds);
        if (items != null && items.size() > 0) {
            for (PosItem posItem : items) {
                OneKeyPurchase oneKeyPurchase = new OneKeyPurchase();
                BeanUtils.copyProperties(posItem, oneKeyPurchase);
                oneKeyPurchases.add(oneKeyPurchase);
            }
        }
        return oneKeyPurchases;
    }

    public BasicResult getWarnRepertoryList(Long passportId) throws Exception {
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        if (merchantPassport == null) {
            throw new PosNullPointerException("商家信息不存在！" + passportId);
        }
        // 查询库存小于预警库存/当前库存小于周销量的日平均值的商品,且不在进货中的商品
        List<OneKeyPurchase> oneKeyPurchaseList = getWarnRepertoryItems(merchantPassport.getId());
        logger.info("商家一键采购：" + merchantPassport.getId() + "，匹配量：" + oneKeyPurchaseList.size());
        if (CollectionUtils.isEmpty(oneKeyPurchaseList)) {
            return BasicResult.createSuccessResult();
        }
        // 取出商品类型,条码
        StringBuilder sb = new StringBuilder();
        List<Long> typeIds = new ArrayList<>();
        Map<String, OneKeyPurchase> menuEntityMap = new HashMap<>();
        for (OneKeyPurchase purchase : oneKeyPurchaseList) {
            if (StringUtils.isNotBlank(purchase.getBarcode())) {
                sb.append(purchase.getBarcode()).append(",");
                menuEntityMap.put(purchase.getBarcode(), purchase);
            }
            typeIds.add(purchase.getItemTypeId());
        }
        sb = sb.delete(sb.length() - 1, sb.length());
        // 用 barcodes 去 供应链查找可进货的商品,极其规格.
        WarehouseItemsVo warehouseItemsVo = lifeSupplyChainItemsManager.matchItemsByBarcode(merchantPassport.getId()
                , sb.toString());
        logger.info("商家一键采购：" + merchantPassport.getId() + "，对应仓库：" + warehouseItemsVo.getWarehouseId());
        List<MatchItemsVo> matchItemsList = warehouseItemsVo.getMatchItems();
        List<LifeItemType> menutypeEntites = itemTypeJpaRepository.findAll(typeIds);
        //系统内建议采购
        for (MatchItemsVo matchItems : matchItemsList) {
            String targetBarcode = warehouseItemsVo.getBarcodeRelateionShip().get(matchItems.getBarcode());
            OneKeyPurchase menuEntity = menuEntityMap.get(targetBarcode);
            String name = menuEntity.getItemName();
            // 设置当前库存，标准库存，预警库存
            matchItems.setStandardInventory(menuEntity.getTotalSale());
            matchItems.setWarnInventory(menuEntity.getWarningRepertory());
            matchItems.setRepertory(menuEntity.getRepertory());
            matchItems.setMenuTypeId(menuEntity.getItemTypeId());
            for (ItemsVo item : matchItems.getItems()) {
                if (item.getStandard() == null) {
                    item.setStandard(12);
                }
                int defaultPurchase = calculationNeed(menuEntity.getDaySale()
                        , menuEntity.getRepertory()
                        , matchItems.getWarnInventory(), item.getStandard(), true);
                if (defaultPurchase < item.getMinSales()) {
                    defaultPurchase = item.getMinSales();
                }
                BigDecimal salesPrice = BigDecimal.valueOf(menuEntity.getSalesPrice());
                salesPrice = salesPrice.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal buyRate = item.getRetailPrice()
                        .subtract(item.getPrice())
                        .divide(item.getRetailPrice(), 2, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(100))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                item.setBuyRate(buyRate + "%");
                BigDecimal saleRate = salesPrice
                        .subtract(item.getPrice().multiply(BigDecimal.valueOf(item.getStandard())))
                        .divide(salesPrice, 2, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(100))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                item.setSaleRate(saleRate + "%");
                //限制采购数量为仓库库存数量
                if (item.getStock() < defaultPurchase) {
                    defaultPurchase = item.getStock();
                }
                //一键采购不限制仓库是否有货,无货一样可以采购
//                item.setStock(defaultPurchase * 10000);
                item.setDefaultPurchase(defaultPurchase);
                item.setName(name);
            }
        }
        //系统外建议采购
        for (OneKeyPurchase menuEntity : oneKeyPurchaseList) {
            for (MatchItemsVo items : matchItemsList) {
                if (menuEntity.getBarcode().equals(items.getBarcode())) {
                    menuEntity.setCanBuy(true);
                    break;
                }
            }
        }
        //商品模板ID
        List<Long> itemTemplateIdList = new ArrayList<>();
        for (OneKeyPurchase oneKeyPurchase : oneKeyPurchaseList) {
            itemTemplateIdList.add(oneKeyPurchase.getItemTemplateId());
        }
        List<LifeItemTemplate> itemTemplateList = itemTemplateJpaRepository.findAll(itemTemplateIdList);
        List<Long> itemUnitIdList = new ArrayList<>();
        for (LifeItemTemplate itemTemplate : itemTemplateList) {
            itemUnitIdList.add(itemTemplate.getUnitId());
        }
        List<LifeItemUnit> itemUnits = itemUnitJpaRepository.findAll(itemUnitIdList);
        Map<Long, LifeItemUnit> itemUnitMap = new HashMap<>();
        for (LifeItemTemplate itemTemplate : itemTemplateList) {
            for (LifeItemUnit itemUnit : itemUnits) {
                if (itemTemplate.getUnitId().longValue() == itemUnit.getId()) {
                    itemUnitMap.put(itemTemplate.getId(), itemUnit);
                }
            }
        }
        List<MatchItemsVo> externalList = new ArrayList<>();
        for (OneKeyPurchase menuEntity : oneKeyPurchaseList) {
            if (!menuEntity.isCanBuy()) {
                MatchItemsVo matchItems = new MatchItemsVo();
                matchItems.setRepertory(menuEntity.getRepertory());
                matchItems.setWarnInventory(menuEntity.getWarningRepertory());
                matchItems.setBarcode(menuEntity.getBarcode());
                matchItems.setMenuTypeId(menuEntity.getItemTypeId());
                ItemsVo items = new ItemsVo();
                items.setStandard(1);
                LifeItemUnit itemUnit = itemUnitMap.get(menuEntity.getItemTemplateId());
                String title = "";
                if (itemUnit != null) {
                    title = itemUnit.getTitle();
                }
                BigDecimal salesPrice = BigDecimal.valueOf(menuEntity.getSalesPrice());
                salesPrice = salesPrice.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);

                BigDecimal stockPrice = BigDecimal.valueOf(menuEntity.getStockPrice());
                stockPrice = stockPrice.divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);

                items.setUnit(title);
                items.setImage(menuEntity.getItemImgUrl());
                items.setDefaultPurchase(calculationNeed(menuEntity.getDaySale()
                        , menuEntity.getRepertory(), matchItems.getWarnInventory()
                        , items.getStandard(), false));
                items.setPrice(salesPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                items.setName(menuEntity.getItemName());
                items.setId(menuEntity.getId());
                items.setStock(menuEntity.getRepertory());
                BigDecimal saleRate = salesPrice.subtract(stockPrice)
                        .divide(salesPrice, 2, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(100))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                items.setSaleRate(saleRate + "%");
                matchItems.getItems().add(items);
                externalList.add(matchItems);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("preferentialList", matchItemsList);
        jsonObject.put("externalList", externalList);
        jsonObject.put("menutypeList", menutypeEntites);
        jsonObject.put("warehouseId", warehouseItemsVo.getWarehouseId());
        BasicResult basicResult = BasicResult.createSuccessResult();
        basicResult.setResponse(jsonObject);
        return basicResult;
    }

    private int calculationNeed(int daySale
            , int todayRepertory, int warnInventory, int standard, boolean haveStandard) {
        int need = 0;
        if (daySale > 0) {
            // 所需补充的库存=上周（日平均销量）*3-剩余库存
            need = daySale * 3 - todayRepertory;
        }
        if (need <= 0 && warnInventory > todayRepertory) {
            // 所需补充的库存=预警库存-剩余库存
            need = warnInventory - todayRepertory;
        }
        if (!haveStandard) {
            return need;
        }
        // 设置默认采购数量
        // 设置默认采购数量=所需补充库/供应链商品规格（如一箱12个），除不尽且大于规格一半的话采购数量加1
        if (need <= standard) {
            return 1;
        }
        int remainder = need % standard;
        if (remainder != 0 && remainder > (standard / 2)) {
            remainder = 1;
        }
        return need / standard + remainder;
    }

    public BasicResult generatePurchaseOrder(long passportId, String sequenceNumber, Long warehouseId
            , Map<String, Object> itemSet) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("passportId", passportId + "");
        params.put("sequenceNumber", sequenceNumber);
        params.put("partnerUserId", passportId + "");
        params.put("userSource", DeviceTypeEnum.DEVICE_TYPE_ANDROID.getKey() + "");
        LifeAchieve merchantPassportAdress = lifeAchieveManager
                .findByPassportId(passportId);
        //商家ID
        params.put("receiptProvince", "");
        params.put("receiptCity", "");
        params.put("receiptDistrict", "");
        params.put("receiptAddress", merchantPassportAdress.getAddress());
        params.put("receiptNickName", merchantPassportAdress.getShowName());
        params.put("receiptPhone", merchantPassportAdress.getPhoneNumber() + "");
        params.put("receiptLocation", merchantPassportAdress.getLatitude()
                + "," + merchantPassportAdress.getLongitude());
        JSONObject jsonObject = new JSONObject();
        for (String key : itemSet.keySet()) {
            jsonObject.put(key, warehouseId);
        }
        params.put("warehouseRemarkSet", jsonObject.toJSONString());
        JSONObject itemSetObj = (JSONObject) itemSet;
        params.put("itemSet", itemSetObj.toJSONString());
        return postPurchaseOrder(GENERATE_ORDER, params);
    }
}
