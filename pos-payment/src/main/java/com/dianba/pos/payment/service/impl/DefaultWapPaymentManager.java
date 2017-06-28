package com.dianba.pos.payment.service.impl;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.service.QROrderManager;
import com.dianba.pos.payment.service.WapPaymentManager;
import com.dianba.pos.payment.service.WeChatPayManager;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DefaultWapPaymentManager implements WapPaymentManager {

    @Autowired
    private QROrderManager qrOrderManager;
    @Autowired
    private WeChatPayManager weChatPayManager;

    public BasicResult wechatPay(Long passportId, String openId, String spBillCreateIP, BigDecimal amount)
            throws Exception {
        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.WEIXIN_JS;
        LifeOrder lifeOrder = qrOrderManager.generateQROrder(passportId, paymentTypeEnum, amount);
        if (lifeOrder != null) {
            return weChatPayManager.jsPayment(lifeOrder, openId, "WEP", spBillCreateIP);
        }
        return BasicResult.createFailResult("订单创建失败！");
    }
}
