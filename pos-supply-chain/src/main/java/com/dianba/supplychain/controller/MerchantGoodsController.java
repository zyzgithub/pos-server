package com.dianba.supplychain.controller;

import com.alibaba.fastjson.JSONArray;
import com.dianba.pos.common.exception.lang.AbstractApiController;
import com.dianba.pos.config.SupplyChainURLConstant;
import com.dianba.supplychain.service.GoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(SupplyChainURLConstant.MERCHANT_GOODS)
public class MerchantGoodsController extends AbstractApiController {

    @Autowired
    private GoodsManager goodsManager;

    @RequestMapping("matchItems")
    @ResponseBody
    public void matchItems(HttpServletRequest request) {
        String barcodes = mustText("barcodes");
        Integer userId = mustInt("userId");
        JSONArray response = goodsManager.matchItems(userId, barcodes);
        addResponse("result", response);
    }
}
