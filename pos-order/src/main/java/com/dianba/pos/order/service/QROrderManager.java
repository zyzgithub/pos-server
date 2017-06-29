package com.dianba.pos.order.service;

import com.dianba.pos.order.po.LifeOrder;
import com.xlibao.common.constant.payment.PaymentTypeEnum;

import java.math.BigDecimal;

public interface QROrderManager {

    LifeOrder generateQROrder(Long passportId, PaymentTypeEnum paymentTypeEnum, BigDecimal amount, String openId);
}
