package com.dianba.pos.order.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.exception.BusinessException;
import com.dianba.pos.order.po.Order;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.metadata.order.OrderEntry;

import java.util.List;
import java.util.Map;

public interface OrderManager {

    String OFFLINE_ORDER_PREFIX = "[offline_order] 离线订单:";
    String SUPERMARKET_ORDER_PREFIX = "[market_order]  超市订单:";


    String BASE_URL = "order/";

    String GET_ORDER = BASE_URL + "getOrder";

    String PREPARE_CREATE_ORDER = BASE_URL + "prepareCreateOrder";

    String GENERATE_ORDER = BASE_URL + "generateOrder";

    String PAYMENT_ORDER = BASE_URL + "paymentOrder";

    String CONFIRM_ORDER = BASE_URL + "confirmOrder";

    /**
     * 创建订单
     */
    Order createOrderFromSuperMarket(Integer merchantId, Integer cashierId, String mobile
            , String params, Integer createTime, String uuid) throws BusinessException;

    /**
     * 获取订单详情
     */
    OrderEntry getOrder(long orderId);

    /*
     * 预创建订单
     */
    BasicResult prepareCreateOrder(long passportId, String orderType);

    /**
     * 生成一个订单
     */
    BasicResult generateOrder(long passportId, String sequenceNumber
            , OrderTypeEnum orderType, long actualPrice, long totalPrice
            , List<Map<String, Object>> orderItems);

    /**
     * 确认支付订单
     */
    BasicResult paymentOrder(long orderId, int transType);

    /**
     * 确认完成订单
     */
    BasicResult confirmOrder(long passportId, long orderId);
}
