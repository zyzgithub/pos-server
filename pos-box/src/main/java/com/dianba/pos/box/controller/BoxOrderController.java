package com.dianba.pos.box.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.service.BoxOrderManager;
import com.dianba.pos.order.po.LifeOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(BoxURLConstant.ORDER)
public class BoxOrderController {

    private static Logger logger = LogManager.getLogger(BoxOrderController.class);

    @Autowired
    private BoxOrderManager boxOrderManager;

    @ResponseBody
    @RequestMapping("create_order")
    public BasicResult createOrder(Long passportId) {
        logger.info("创建无人便利店订单" + passportId);
        LifeOrder lifeOrder = boxOrderManager.createBoxOrder(passportId);
        BasicResult basicResult = BasicResult.createSuccessResult();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sequenceNumber", lifeOrder.getSequenceNumber());
        basicResult.setResponse(jsonObject);
        return basicResult;
    }
}
