package com.dianba.pos.order.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.pojo.OrderItemPojo;
import com.dianba.pos.order.pojo.OrderPojo;
import com.dianba.pos.order.vo.LifeOrderVo;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
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
    LifeOrderVo getLifeOrder(long orderId);

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
    BasicResult paymentOrder(Long orderId, PaymentTypeEnum paymentTypeEnum);

    /**
     * 确认完成订单
     */
    BasicResult confirmOrder(long passportId, long orderId);

    /**
     * 批量保存离线订单
     * @param orders
     * @return
     */
    BasicResult syncOfflineOrders(List<OrderPojo> orders);


    /**
     * 获取订单详情，使用商家ID
     */
    BasicResult getOrderForPos(Long passportId, Integer orderType, Integer orderStatus
            , Integer pageNum, Integer pageSize);

    BasicResult getMerchantProfitInfo(Long merchantId,String phone);
}
