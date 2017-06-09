package com.dianba.supplychain.controller;

import com.dianba.pos.common.exception.lang.AbstractApiController;
import com.dianba.supplychain.config.SupplyChainURLConstant;
import com.dianba.supplychain.service.GoodsManager;
import com.dianba.supplychain.vo.MatchItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(SupplyChainURLConstant.MERCHANT_GOODS)
public class MerchantGoodsController extends AbstractApiController {

    @Autowired
    private GoodsManager goodsManager;

    @RequestMapping("matchItems")
    @ResponseBody
    public List<MatchItems> matchItems(HttpServletRequest request) {
        String barcodes = mustText("barcodes");
        Integer userId = mustInt("userId");
        return goodsManager.matchItemsByBarcode(userId, barcodes);
    }
}
