package com.base.constant.order;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wm.entity.order.OrderEntity;
import com.wm.util.ConverDataUtil;

/**
 * 订单处理工具类
 * @author folo
 */
public class OrderUtil {
    private static Logger logger = LoggerFactory.getLogger(OrderUtil.class);

    /**
     * 根据订单信息获取实际支付金额
     * @param order
     * @return
     */
    public static BigDecimal getRealPayMoney(OrderEntity order) {
        return new BigDecimal(order.getOrigin()).add(new BigDecimal(order.getCostLunchBox()))//加上餐盒费
                        .add(new BigDecimal(order.getDeliveryFee()))//加上配送费
                        .subtract(new BigDecimal(order.getScoreMoney()))//减去积分抵扣金额
                        .subtract(new BigDecimal(order.getCard()))//减去优惠券抵扣金额
                        .subtract(new BigDecimal(order.getMemberDiscountMoney()));//减去会员折扣金额
    }
    
    /**
     * 通过payid获取订单固定的信息
     * 2位标识订单来源 OrderSource
       2位标识订单类型 OrderType
       1位标识获取商品方式 GetGoodsWay
       1位标识订单配送类型 DeliveryType
       1位标识预收入状态 PreIncomStatus
     * @param payId
     * @param c
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getTypeByPayId(String payId, Class<?> c){
        if(payId.length() < 24) return null;
        try {
            if(c == OrderSource.class){
                return (T) OrderSource.get(ConverDataUtil.toInt(payId.substring(0, 2)));
            }else if(c == OrderType.class){
                return (T) OrderType.get(ConverDataUtil.toInt(payId.substring(2, 4)));
            } else if(c == GetGoodsWay.class){
                return (T) GetGoodsWay.get(ConverDataUtil.toInt(payId.substring(4, 5)));
            } else if(c == DeliveryType.class){
                return (T) DeliveryType.get(ConverDataUtil.toInt(payId.substring(5, 6)));
            } else if(c == PreIncomStatus.class){
                return (T) PreIncomStatus.get(ConverDataUtil.toInt(payId.substring(6, 7)));
            }else logger.error("通过payId{}获取订单{}失败,没有该类型", payId, c.getName());
        }
        catch (Exception e) {
            logger.warn("通过payId{}获取订单{}失败,异常", payId, c.getName(), e);
        }
        return null;
    }
}
