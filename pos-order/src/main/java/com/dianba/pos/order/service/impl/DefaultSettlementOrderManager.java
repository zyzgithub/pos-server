package com.dianba.pos.order.service.impl;

import com.dianba.pos.order.mapper.LifeOrderMapper;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.repository.LifeOrderJpaRepository;
import com.dianba.pos.order.service.SettlementOrderManager;
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
import java.util.List;

@Service
public class DefaultSettlementOrderManager implements SettlementOrderManager {

    @Autowired
    private LifeOrderJpaRepository orderJpaRepository;
    @Autowired
    private LifeOrderMapper orderMapper;
    @Autowired
    private PassportManager passportManager;

    @Transactional
    public LifeOrder generateSettlementOrder(Long passportId, PaymentTypeEnum paymentType, BigDecimal amount) {
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        LifeOrder lifeOrder = new LifeOrder();
        lifeOrder.setSequenceNumber(OrderSequenceUtil.generateOrderSequence());
        lifeOrder.setPartnerId(passportId + "");
        lifeOrder.setPartnerUserId(passportId + "");
        lifeOrder.setCreateTime(new Date());
        lifeOrder.setShippingPassportId(merchantPassport.getId());
        //10 POS结算订单
        lifeOrder.setStatus(OrderStatusEnum.ORDER_STATUS_DEFAULT.getKey());
        lifeOrder.setType(OrderTypeEnum.POS_SETTLEMENT_ORDER_TYPE.getKey());
        lifeOrder.setPaymentType("-1");
        lifeOrder.setTransType(paymentType.getKey());
        long price = amount.multiply(BigDecimal.valueOf(100)).longValue();
        lifeOrder.setActualPrice(BigDecimal.valueOf(price));
        lifeOrder.setTotalPrice(BigDecimal.valueOf(price));
        lifeOrder = orderJpaRepository.save(lifeOrder);
        return lifeOrder;
    }


    @Transactional
    public void updateSettlementCashOrderByUserAndDate(Long passportId, String date) {
        List<LifeOrder> lifeOrders = orderMapper.findOrderByPartnerUserAndPaymentTime(passportId, date);
        if (lifeOrders != null && lifeOrders.size() != 0) {
            for (LifeOrder lifeOrder : lifeOrders) {
                lifeOrder.setPaymentType("1");
            }
            orderJpaRepository.save(lifeOrders);
        }
    }
}
