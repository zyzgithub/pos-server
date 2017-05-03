package com.dianba.pos.purchase.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.HttpProxy;
import com.dianba.pos.menu.mapper.MenuMapper;
import com.dianba.pos.menu.po.Menu;
import com.dianba.pos.menu.po.MenuType;
import com.dianba.pos.purchase.mapper.OneKeyPurchaseMapper;
import com.dianba.pos.purchase.service.OneKeyPurchaseManager;
import com.dianba.supplychain.controller.MerchantGoodsController;
import com.dianba.supplychain.service.GoodsManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class DefaultOneKeyPurchaseManager implements OneKeyPurchaseManager {

    @Autowired
    private OneKeyPurchaseMapper oneKeyPurchaseMapper;
    @Autowired
    private GoodsManager goodsManager;

    public Map<String, Object> warnInvenstoryList(Integer merchantId, Integer userId)
            throws HttpProxy.HttpAccessException, IOException {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        //TODO 排查进货中的
        // 不再从缓存里面去拿进货中的数据
//        List<String> barcodes = getPurchaseBarcodes(null, userId, null);
//        // 查询 库存小于 预警库存的商品
//        List<Menu> menuEntities=oneKeyPurchaseMapper.queryWarnInvenstory(merchantId,barcodes);

        // 查询 库存小于 预警库存的商品
        List<Menu> menuEntities=oneKeyPurchaseMapper.queryWarnInvenstory(merchantId,null);

        if (CollectionUtils.isEmpty(menuEntities)) {
            return resultMap;
        }
        // 拿出 barcodes
        List<MenuType> menutypeEntites = new ArrayList<MenuType>();
        StringBuilder sb = new StringBuilder();
        Map<String, Menu> menuEntityMap = new HashMap<>();
        for (Menu menuEntity : menuEntities) {
            if (StringUtils.isNotBlank(menuEntity.getBarcode())) {
                sb.append(menuEntity.getBarcode()).append(",");
                menuEntityMap.put(menuEntity.getBarcode(), menuEntity);
            }
            if(null != menuEntity.getMenuType()){
                if(!menutypeEntites.contains(menuEntity.getMenuType())){
                    menutypeEntites.add(menuEntity.getMenuType());
                }
            }
        }
        sb = sb.delete(sb.length() - 1, sb.length());

        // 用 barcodes 去 供应链查找可进货的商品,极其规格.
        JSONArray list = goodsManager.matchItems(userId,sb.toString());

        //系统外建议采购
        JSONArray externalList = new JSONArray();
        for(Menu menuEntity: menuEntityMap.values()){
            boolean isNotExists = true;
            if(CollectionUtils.isNotEmpty(list)){
                for (Object o : list) {
                    JSONObject jo = (JSONObject) o;
                    if(menuEntity.getBarcode().equals(jo.get("barcode"))){
                        isNotExists = false;
                        break;
                    }
                }
            }
            if(isNotExists){
                JSONObject json = new JSONObject();
                int todayRepertory = menuEntity.getTodayRepertory() == null ? 0 : menuEntity.getTodayRepertory();
                int standardInventory = menuEntity.getStandardInventory() == null ? 50 : menuEntity.getStandardInventory();
                Integer standard = menuEntity.getStandardInventory() == null ? 12 : menuEntity.getStandardInventory();
                int need = standardInventory - todayRepertory;
                // 设置当前库存
                json.put("repertory", todayRepertory);
                // 这2个库存是计算出来的. 不是拿它原来的.
                json.put("standardInventory", menuEntity.getStandardInventory() == null ? 12 : menuEntity.getStandardInventory());
                json.put("warnInventory", menuEntity.getWarnInventory() == null ? 0 : menuEntity.getWarnInventory());
                json.put("barcode",menuEntity.getBarcode());
                if(null != menuEntity.getMenuType()){
                    json.put("menuTypeId",menuEntity.getMenuType().getId());
                }

                JSONArray items = new JSONArray();
                JSONObject itemJson = new JSONObject();
                itemJson.put("standard", standard);
                itemJson.put("image", menuEntity.getImage());
                itemJson.put("unit", menuEntity.getUnit());
                // 设置默认采购数量
                int defaultPurchase = need % standard == 0 ? need / standard : need / standard + 1;
                itemJson.put("defaultPurchase", defaultPurchase);
                itemJson.put("price", menuEntity.getPrice());
                itemJson.put("name", menuEntity.getName());
                itemJson.put("id", menuEntity.getId());
                itemJson.put("stock", todayRepertory);
                items.add(itemJson);
                json.put("items", items);

                externalList.add(json);
            }
        }

        //系统内建议采购
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object o : list) {
                JSONObject jo = (JSONObject) o;
                Menu menuEntity = menuEntityMap.get(jo.get("barcode") + "");
                int todayRepertory = menuEntity.getTodayRepertory() == null ? 0 : menuEntity.getTodayRepertory();
                int standardInventory = menuEntity.getStandardInventory() == null ? 50 : menuEntity.getStandardInventory();
                String name = menuEntity.getName();
                int need = standardInventory - todayRepertory;
                if (menuEntity == null) {
                    continue;
                }
                // 设置当前库存
                jo.put("repertory", todayRepertory);
                if(null != menuEntity.getMenuType()){
                    jo.put("menuTypeId",menuEntity.getMenuType().getId());
                }
                // 这2个库存是计算出来的. 不是拿它原来的.
                jo.put("standardInventory", menuEntity.getStandardInventory() == null ? 12 : menuEntity.getStandardInventory());
                jo.put("warnInventory", menuEntity.getWarnInventory() == null ? 0 : menuEntity.getWarnInventory());
                JSONArray items = jo.getJSONArray("items");
                if (items != null && !items.isEmpty()) {
                    for (Object item : items) {
                        JSONObject ijo = (JSONObject) item;
                        Integer standard = ijo.getInteger("standard");
                        if (standard == null) {
                            standard = 12;
                            ijo.put("standard", standard);
                        }
                        // 设置默认采购数量
                        int defaultPurchase = need % standard == 0 ? need / standard : need / standard + 1;
                        ijo.put("defaultPurchase", defaultPurchase);
                        ijo.put("name", name);
                    }
                }
            }
        }
        resultMap.put("preferentialList", list);
        resultMap.put("externalList", externalList);
        resultMap.put("menutypeList", JSONArray.toJSONString(menutypeEntites));
        return resultMap;
    }
}
