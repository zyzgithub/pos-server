package com.dianba.pos.payment.service.impl;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.order.service.QROrderManager;
import com.dianba.pos.payment.service.WapPaymentManager;
import com.dianba.pos.payment.service.WeChatPayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultWapPaymentManager implements WapPaymentManager {

    @Autowired
    private QROrderManager qrOrderManager;
    @Autowired
    private WeChatPayManager weChatPayManager;
    @Autowired
    private LifeOrderManager lifeOrderManager;

    @Override
    public BasicResult wechatPay(String sequenceNumber, String spBillCreateIP) throws Exception {
        LifeOrder lifeOrder = lifeOrderManager.getLifeOrder(sequenceNumber, false);
        if (lifeOrder != null) {
            return weChatPayManager.jsPayment(lifeOrder, lifeOrder.getReceiptUserId(), "WEP", spBillCreateIP);
        }
        return BasicResult.createFailResult("订单不存在！");
    }
}
