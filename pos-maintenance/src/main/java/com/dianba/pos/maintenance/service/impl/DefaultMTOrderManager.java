package com.dianba.pos.maintenance.service.impl;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.maintenance.mapper.MTOrderMapper;
import com.dianba.pos.maintenance.service.MTOrderManager;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.passport.po.PosMerchantRate;
import com.dianba.pos.passport.service.PosMerchantRateManager;
import com.dianba.pos.payment.service.PaymentManager;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.TransTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class DefaultMTOrderManager implements MTOrderManager {

    private static Logger logger = LogManager.getLogger(DefaultMTOrderManager.class);

    @Autowired
    private PaymentManager paymentManager;
    @Autowired
    private MTOrderMapper mtOrderMapper;
    @Autowired
    private PosMerchantRateManager posMerchantRateManager;

    public BasicResult fixOffsetAmount() {
        List<LifeOrder> lifeOrders = mtOrderMapper.findNoneOffsetAmountOrders();
        List<LifeOrder> fixedOrders = new ArrayList<>();
        for (LifeOrder lifeOrder : lifeOrders) {
            //对商家余额进行偏移计算
            OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(lifeOrder.getType());
            long offsetAmount = 0;
            if (orderTypeEnum.getKey() == OrderTypeEnum.POS_EXTENDED_ORDER_TYPE.getKey()) {
                if (lifeOrder.getTotalPrice().longValue() > lifeOrder.getActualPrice().longValue()) {
                    offsetAmount = lifeOrder.getTotalPrice().longValue() - lifeOrder.getActualPrice().longValue();
                }
            } else if (orderTypeEnum.getKey() == OrderTypeEnum.SCAN_ORDER_TYPE.getKey()) {
                //进行扣点计算
                PosMerchantRate posMerchantRate = posMerchantRateManager
                        .findByMerchantPassportId(lifeOrder.getShippingPassportId());
                BigDecimal commissionRate = PosMerchantRate.COMMISSION_RATE;
                if (posMerchantRate != null) {
                    if (1 == posMerchantRate.getIsNeed()) {
                        commissionRate = posMerchantRate.getCommissionRate();
                    } else {
                        commissionRate = BigDecimal.ZERO;
                    }
                }
                BigDecimal amount = lifeOrder.getTotalPrice().subtract(
                        lifeOrder.getTotalPrice().multiply(commissionRate)
                ).setScale(0, BigDecimal.ROUND_HALF_UP);
                offsetAmount = amount.longValue();
            } else if (orderTypeEnum.getKey() == OrderTypeEnum.PURCHASE_ORDER_TYPE.getKey()) {
                offsetAmount = -lifeOrder.getTotalPrice().longValue();
            } else if (orderTypeEnum.getKey() == OrderTypeEnum.POS_SETTLEMENT_ORDER_TYPE.getKey()) {
                offsetAmount = lifeOrder.getTotalPrice().longValue();
            }
            if (offsetAmount != 0) {
                //对商家余额进行余额偏移
                TransTypeEnum transTypeEnum = TransTypeEnum.PAYMENT;
                BasicResult basicResult = paymentManager.offsetBalance(lifeOrder.getShippingPassportId()
                        , lifeOrder.getSequenceNumber(), offsetAmount, transTypeEnum);
                if (basicResult.isSuccess()) {
                    fixedOrders.add(lifeOrder);
                } else {
                    logger.info("余额偏移处理失败！订单ID:" + lifeOrder.getId()
                            + "，错误消息：" + basicResult.getMsg());
                }
            }
        }
        return BasicResult.createSuccessResultWithDatas("成功处理未偏移余额单据" + fixedOrders.size() + "条！"
                , fixedOrders);
    }
}
