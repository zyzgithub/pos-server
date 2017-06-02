package com.dianba.pos.purchase.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.item.po.LifeItemType;
import com.dianba.pos.item.po.PosItem;
import com.dianba.pos.item.repository.LifeItemTypeJpaRepository;
import com.dianba.pos.item.repository.PosItemJpaRepository;
import com.dianba.pos.purchase.mapper.OneKeyPurchaseMapper;
import com.dianba.pos.purchase.pojo.OneKeyPurchase;
import com.dianba.pos.purchase.service.OneKeyPurchaseManager;
import com.dianba.pos.supplychain.service.GoodsManager;
import com.dianba.pos.supplychain.vo.Items;
import com.dianba.pos.supplychain.vo.MatchItems;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DefaultOneKeyPurchaseManager implements OneKeyPurchaseManager {


    private static Logger logger = LogManager.getLogger(DefaultOneKeyPurchaseManager.class);
    @Autowired
    private OneKeyPurchaseMapper oneKeyPurchaseMapper;
    @Autowired
    private GoodsManager goodsManager;
    @Autowired
    private LifeItemTypeJpaRepository itemTypeJpaRepository;
    @Autowired
    private PosItemJpaRepository posItemJpaRepository;

    public List<OneKeyPurchase> getWarnRepertoryItems(Long passportId) {
        List<OneKeyPurchase> menuEntities = oneKeyPurchaseMapper.findWarnSaleItems(passportId);
        if (menuEntities == null) {
            menuEntities = new ArrayList<>();
        }
        List<Long> itemTemplateIds = new ArrayList<>();
        for (OneKeyPurchase oneKeyPurchase : menuEntities) {
            itemTemplateIds.add(oneKeyPurchase.getItemTemplateId());
        }
        List<PosItem> items = posItemJpaRepository.findWarningRepertoryItemsByExclude(passportId
                , itemTemplateIds);
        if (items != null) {
            for (PosItem posItem : items) {
                menuEntities.add((OneKeyPurchase) posItem);
            }
        }
        return menuEntities;
    }

    public BasicResult getWarnRepertoryList(Long passportId) throws Exception {
        // 查询库存小于预警库存/当前库存小于周销量的日平均值的商品,且不在进货中的商品
        List<OneKeyPurchase> menuEntities = getWarnRepertoryItems(passportId);
        if (CollectionUtils.isEmpty(menuEntities)) {
            return BasicResult.createSuccessResult();
        }
        // 取出商品类型,条码
        StringBuilder sb = new StringBuilder();
        List<Long> typeIds = new ArrayList<>();
        Map<String, OneKeyPurchase> menuEntityMap = new HashMap<>();
        for (OneKeyPurchase purchase : menuEntities) {
            if (StringUtils.isNotBlank(purchase.getBarcode())) {
                sb.append(purchase.getBarcode()).append(",");
                menuEntityMap.put(purchase.getBarcode(), purchase);
            }
            typeIds.add(purchase.getItemTypeId());
        }
        sb = sb.delete(sb.length() - 1, sb.length());
        // 用 barcodes 去 供应链查找可进货的商品,极其规格.
        List<MatchItems> matchItemsList = goodsManager.matchItemsByBarcode(passportId, sb.toString());
        List<LifeItemType> menutypeEntites = itemTypeJpaRepository.findAll(typeIds);
        //系统内建议采购
        for (MatchItems matchItems : matchItemsList) {
            OneKeyPurchase menuEntity = menuEntityMap.get(matchItems.getBarcode());
            String name = menuEntity.getItemName();
            // 设置当前库存，标准库存，预警库存
            matchItems.setMenuTypeId(menuEntity.getItemTypeId());
            for (Items item : matchItems.getItems()) {
                if (item.getStandard() == null) {
                    item.setStandard(12);
                }
                int defaultPurchase = calculationNeed(menuEntity.getDaySale()
                        , menuEntity.getRepertory()
                        , matchItems.getWarnInventory(), item.getStandard(), true);
                if (defaultPurchase < item.getMinSales()) {
                    defaultPurchase = item.getMinSales();
                }
                BigDecimal buyRate = item.getRetailPrice()
                        .subtract(item.getPrice())
                        .divide(item.getRetailPrice(), 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal(100))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                item.setBuyRate(buyRate + "%");
                BigDecimal saleRate = BigDecimal.valueOf(menuEntity.getSalesPrice())
                        .subtract(item.getPrice().multiply(BigDecimal.valueOf(item.getStandard())))
                        .divide(BigDecimal.valueOf(menuEntity.getSalesPrice()), BigDecimal.ROUND_HALF_UP)
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
        for (OneKeyPurchase menuEntity : menuEntities) {
            for (MatchItems items : matchItemsList) {
                if (menuEntity.getBarcode().equals(items.getBarcode())) {
                    menuEntity.setCanBuy(true);
                    break;
                }
            }
        }
        List<MatchItems> externalList = new ArrayList<>();
        boolean isNotExists = true;
        for (OneKeyPurchase menuEntity : menuEntities) {
            if (!menuEntity.isCanBuy()) {
                MatchItems matchItems = new MatchItems();
                matchItems.setRepertory(menuEntity.getRepertory());
                matchItems.setWarnInventory(menuEntity.getWarningRepertory());
                matchItems.setBarcode(menuEntity.getBarcode());
                matchItems.setMenuTypeId(menuEntity.getItemTypeId());
                Items items = new Items();
                //TODO 一键采购标准库存以及单位
//                items.setStandard(standard);
//                items.setUnit(menuEntity.getUnit());
                items.setImage(menuEntity.getItemImgUrl());
                items.setDefaultPurchase(calculationNeed(menuEntity.getDaySale()
                        , menuEntity.getRepertory(), matchItems.getWarnInventory()
                        , items.getStandard(), false));
                items.setPrice(new BigDecimal(menuEntity.getSalesPrice())
                        .setScale(2, BigDecimal.ROUND_HALF_UP));
                items.setName(menuEntity.getItemName());
                items.setId(menuEntity.getId());
                items.setStock(menuEntity.getRepertory());
                BigDecimal saleRate = BigDecimal.valueOf(menuEntity.getSalesPrice())
                        .subtract(items.getPrice())
                        .divide(BigDecimal.valueOf(menuEntity.getStockPrice()), BigDecimal.ROUND_HALF_UP)
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
}
