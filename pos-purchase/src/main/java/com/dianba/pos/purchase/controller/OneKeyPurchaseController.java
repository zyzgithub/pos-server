package com.dianba.pos.purchase.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.purchase.config.PurchaseURLConstant;
import com.dianba.pos.purchase.service.OneKeyPurchaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public BasicResult warnInventoryList(Long passportId) throws Exception {
        return oneKeyPurchaseManager.getWarnRepertoryList(passportId);
    }
}
