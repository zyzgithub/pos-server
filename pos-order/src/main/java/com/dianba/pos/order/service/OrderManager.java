package com.dianba.pos.order.service;

import com.dianba.pos.order.exception.BusinessException;
import com.dianba.pos.order.po.Order;

public interface OrderManager {

    String offlineOrderPrefix = "[offline_order] 离线订单:";
    String superMarketOrderPrefix = "[market_order]  超市订单:";

    /**
     * 创建订单
     */
    Order createOrderFromSuperMarket(Integer merchantId
            , Integer cashierId, String params, Integer createTime, String uuid) throws BusinessException;
}
