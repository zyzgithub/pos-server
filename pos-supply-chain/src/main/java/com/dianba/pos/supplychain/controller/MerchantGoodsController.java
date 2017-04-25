package com.dianba.pos.supplychain.controller;

import com.dianba.pos.supplychain.config.SupplyChainURLConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(SupplyChainURLConstant.MERCHANT_GOODS)
public class MerchantGoodsController {


    @RequestMapping("matchItems")
    public void matchItems(HttpServletRequest request) {
        System.out.println();
//        String barcodes = mustText("barcodes");
//        Integer userId = mustInt("userId");
//
//        JSONArray response = goodsService.matchItems(userId, barcodes);
//        addResponse("result", response);
    }
}
