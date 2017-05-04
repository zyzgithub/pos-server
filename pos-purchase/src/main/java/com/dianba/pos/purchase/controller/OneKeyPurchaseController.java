package com.dianba.pos.purchase.controller;

import com.alibaba.fastjson.JSONArray;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.HttpProxy;
import com.dianba.pos.purchase.config.PurchaseURLConstant;
import com.dianba.pos.purchase.service.OneKeyPurchaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping(PurchaseURLConstant.PURCHASE_ONE_KEY)
public class OneKeyPurchaseController {

    @Autowired
    private OneKeyPurchaseManager oneKeyPurchaseManager;

    /**
     * 一键采购获取商品
     **/
    @ResponseBody
    @RequestMapping("warnInventoryList")
    public AjaxJson warnInventoryList(Integer merchantId, Integer userId, HttpServletRequest request)
            throws HttpProxy.HttpAccessException, IOException {
        AjaxJson j = AjaxJson.successJson("请求成功");
        Map<String, Object> map = oneKeyPurchaseManager.warnInvenstoryList(552, 91174);
        if (map == null || map.isEmpty()) {
            j.setType("2");
        } else if (null == map.get("preferentialList") && null == map.get("externalList")) {
            j.setType("3");
        } else if (null != map.get("preferentialList") && null != map.get("externalList")) {
            JSONArray parray = (JSONArray) map.get("preferentialList");
            JSONArray earray = (JSONArray) map.get("externalList");
            if (parray.isEmpty() && earray.isEmpty()) {
                j.setType("3");
            } else {
                j.setType("1");
            }
        } else {
            j.setType("1");
        }

        j.setObj(map);
        return j;
    }
}
