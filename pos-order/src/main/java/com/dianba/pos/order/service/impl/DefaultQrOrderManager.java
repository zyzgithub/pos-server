package com.dianba.pos.order.service.impl;

import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.repository.LifeOrderJpaRepository;
import com.dianba.pos.order.service.QROrderManager;
import com.dianba.pos.order.util.OrderSequenceUtil;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.service.PassportManager;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class DefaultQrOrderManager implements QROrderManager {

    @Autowired
    private LifeOrderJpaRepository orderJpaRepository;
    @Autowired
    private PassportManager passportManager;

    @Transactional
    public LifeOrder generateQROrder(Long passportId, PaymentTypeEnum paymentType, BigDecimal amount, String openId) {
        Passport merchantPassport = passportManager.findById(passportId);
        LifeOrder lifeOrder = new LifeOrder();
        lifeOrder.setSequenceNumber(OrderSequenceUtil.generateOrderSequence());
        lifeOrder.setPartnerId(passportId + "");
        lifeOrder.setPartnerUserId(passportId + "");
        lifeOrder.setCreateTime(new Date());
        lifeOrder.setShippingPassportId(merchantPassport.getId());
        lifeOrder.setShippingNickName(merchantPassport.getShowName());
        lifeOrder.setReceiptUserId(openId);
        lifeOrder.setStatus(OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey());
        lifeOrder.setType(OrderTypeEnum.SCAN_ORDER_TYPE.getKey());
        lifeOrder.setPaymentType("-1");
        lifeOrder.setTransType(paymentType.getKey());
        long price = amount.multiply(BigDecimal.valueOf(100)).longValue();
        lifeOrder.setActualPrice(BigDecimal.valueOf(price));
        lifeOrder.setTotalPrice(BigDecimal.valueOf(price));
        lifeOrder = orderJpaRepository.save(lifeOrder);
        return lifeOrder;
    }
}
