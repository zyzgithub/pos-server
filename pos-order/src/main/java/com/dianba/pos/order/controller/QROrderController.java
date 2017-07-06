package com.dianba.pos.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosAccessDeniedException;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.order.config.OrderURLConstant;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.service.QROrderManager;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
@RequestMapping(OrderURLConstant.QR_ORDER)
public class QROrderController {

    private static Logger logger = LogManager.getLogger(QROrderController.class);

    @Autowired
    private QROrderManager qrOrderManager;

    @ResponseBody
    @RequestMapping(value = "create_order", method = RequestMethod.POST)
    public BasicResult createQROrder(Long passportId, String paymentType
            , BigDecimal amount, String openId) throws Exception {
        PaymentTypeEnum paymentTypeEnum;
        if (PaymentTypeEnum.ALIPAY.getKey().equals(paymentType)) {
            paymentTypeEnum = PaymentTypeEnum.ALIPAY;
            if (amount.compareTo(BigDecimal.valueOf(0.01)) > 0) {
                throw new PosAccessDeniedException("交易超限！支付宝单笔交易限额1000！");
            }
        } else if (PaymentTypeEnum.WEIXIN_JS.getKey().equals(paymentType)) {
            paymentTypeEnum = PaymentTypeEnum.WEIXIN_JS;
            if (amount.compareTo(BigDecimal.valueOf(0.03)) > 0) {
                throw new PosAccessDeniedException("交易超限！微信单笔交易限额3000！");
            }
        } else {
            throw new PosIllegalArgumentException("支付类型非法！");
        }
        logger.info("扫码订单创建：passportId:" + passportId + " paymentType:"
                + paymentType + " amount:" + amount + " openId:" + openId);
        LifeOrder lifeOrder = qrOrderManager.generateQROrder(passportId, paymentTypeEnum, amount, openId);
        BasicResult basicResult = BasicResult.createSuccessResult();
        if (lifeOrder != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sequenceNumber", lifeOrder.getSequenceNumber());
            basicResult.setResponse(jsonObject);
        } else {
            basicResult = BasicResult.createFailResult("订单创建失败！");
        }
        return basicResult;
    }
}
