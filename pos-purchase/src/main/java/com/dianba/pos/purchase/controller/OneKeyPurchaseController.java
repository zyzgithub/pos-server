package com.dianba.pos.purchase.controller;

import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.purchase.config.PurchaseURLConstant;
import com.dianba.pos.purchase.service.OneKeyPurchaseManager;
import com.dianba.supplychain.vo.MatchItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    public AjaxJson warnInventoryList(Integer merchantId, Integer userId, HttpServletRequest request) throws Exception {
        AjaxJson j = AjaxJson.successJson("请求成功");
        Map<String, Object> map = oneKeyPurchaseManager.warnInvenstoryList(merchantId, userId);
        List<MatchItems> preferentialList = (List<MatchItems>) map.get("preferentialList");
        List<MatchItems> externalList = (List<MatchItems>) map.get("externalList");
        if (map.isEmpty()) {
            j.setType("2");
        } else if (preferentialList.isEmpty() && externalList.isEmpty()) {
            j.setType("3");
        } else {
            j.setType("1");
        }
        j.setObj(map);
        return j;
    }
}
