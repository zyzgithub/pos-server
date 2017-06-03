package com.dianba.pos.order.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.pojo.OrderItemPojo;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.metadata.order.OrderEntry;

import java.util.List;
import java.util.Map;

public interface OrderManager {

    String BASE_URL = "order/";

    String GET_ORDER = BASE_URL + "getOrder";

    String PREPARE_CREATE_ORDER = BASE_URL + "prepareCreateOrder";

    String GENERATE_ORDER = BASE_URL + "generateOrder";

    String PAYMENT_ORDER = BASE_URL + "paymentOrder";

    String CONFIRM_ORDER = BASE_URL + "confirmOrder";

    /**
     * 获取订单详情
     */
    OrderEntry getOrder(long orderId);

    /**
     * 获取订单详情，使用orderId
     */
    LifeOrder getLifeOrder(long orderId);

    /**
     * 获取订单详情，使用订单编码
     */
    LifeOrder getLifeOrder(String sequenceNumber);

    /*
     * 预创建订单
     */
    BasicResult prepareCreateOrder(long passportId, OrderTypeEnum orderType);

    /**
     * 生成一个订单
     */
    BasicResult generateOrder(long passportId, String sequenceNumber, String phoneNumber
            , long actualPrice, long totalPrice
            , List<OrderItemPojo> orderItems) throws Exception;

    /**
     * 生成一键采购订单
     */
    BasicResult generatePurchaseOrder(long passportId, String sequenceNumber, Long warehouseId
            , Map<String, Object> itemSet) throws Exception;

    /**
     * 确认支付订单
     */
    BasicResult paymentOrder(long orderId, int transType);

    /**
     * 确认完成订单
     */
    BasicResult confirmOrder(long passportId, long orderId);

    /**
     * 根据商家ID获取订单
     *
     * @param merchantPassportId 商家ID
     * @return
     */
    BasicResult getOrderForMerchant(long merchantPassportId, int pageNum, int pageSize);
}
