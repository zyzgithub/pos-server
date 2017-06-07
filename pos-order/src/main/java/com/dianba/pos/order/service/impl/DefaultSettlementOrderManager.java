package com.dianba.pos.order.service.impl;

import com.dianba.pos.order.mapper.LifeOrderMapper;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.repository.LifeOrderJpaRepository;
import com.dianba.pos.order.service.SettlementOrderManager;
import com.dianba.pos.order.util.OrderSequenceUtil;
import com.xlibao.common.constant.order.OrderStatusEnum;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.metadata.order.OrderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class DefaultSettlementOrderManager implements SettlementOrderManager {

    @Autowired
    private LifeOrderJpaRepository orderJpaRepository;
    @Autowired
    private LifeOrderMapper orderMapper;

    @Transactional
    public LifeOrder generateSettlementOrder(Long passportId, PaymentTypeEnum paymentType, BigDecimal amount) {
        LifeOrder lifeOrder = new LifeOrder();
        lifeOrder.setSequenceNumber(OrderSequenceUtil.generateOrderSequence());
        lifeOrder.setPartnerId(passportId + "");
        lifeOrder.setPartnerUserId(passportId + "");
        lifeOrder.setCreateTime(new Date());
        //10 POS结算订单
        lifeOrder.setStatus(OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey());
        lifeOrder.setType(OrderTypeEnum.POS_SETTLEMENT_ORDER_TYPE.getKey());
        lifeOrder.setPaymentType("-1");
        lifeOrder.setTransType(paymentType.getKey());
        long price = amount.multiply(BigDecimal.valueOf(100)).longValue();
        lifeOrder.setActualPrice(price);
        lifeOrder.setTotalPrice(price);
        lifeOrder = orderJpaRepository.save(lifeOrder);
        return lifeOrder;
    }

    public OrderEntry findSettlementOrderByDay(Long passportId) {
        return orderMapper.findSettlementOrder4Today(passportId);
    }
}
