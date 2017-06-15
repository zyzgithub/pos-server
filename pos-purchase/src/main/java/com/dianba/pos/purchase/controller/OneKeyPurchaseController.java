package com.dianba.pos.purchase.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.purchase.config.PurchaseURLConstant;
import com.dianba.pos.purchase.service.OneKeyPurchaseManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api("一键采购获取商品列表数据")
@Controller
@RequestMapping(PurchaseURLConstant.PURCHASE_ONE_KEY)
public class OneKeyPurchaseController {

    @Autowired
    private OneKeyPurchaseManager oneKeyPurchaseManager;

    /**
     * 一键采购获取商品
     **/
    @ApiOperation("获取优惠生活建议采购，系统外建议采购列表")
    @ApiImplicitParam(name = "passportId", value = "通行证ID", required = true)
    @ResponseBody
    @RequestMapping(value = "warnInventoryList", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResult warnInventoryList(Long passportId) throws Exception {
        return oneKeyPurchaseManager.getWarnRepertoryList(passportId);
    }
}
