package com.dianba.pos.supplychain.controller;

import com.dianba.pos.menu.po.Menu;
import com.dianba.pos.supplychain.config.SupplyChainURLConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(SupplyChainURLConstant.MERCHANT_GOODS)
public class MerchantGoodsController {


    @RequestMapping("matchItems")
    @ResponseBody
    public Menu matchItems(HttpServletRequest request) {
        Menu menu=new Menu();
        menu.setDisplay("this is a str");
//        String barcodes = mustText("barcodes");
//        Integer userId = mustInt("userId");
//
//        JSONArray response = goodsService.matchItems(userId, barcodes);
//        addResponse("result", response);
        return menu;
    }
}
