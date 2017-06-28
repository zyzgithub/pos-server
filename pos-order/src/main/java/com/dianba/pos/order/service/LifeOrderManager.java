package com.dianba.pos.order.service;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.pojo.OrderItemPojo;
import com.dianba.pos.order.pojo.OrderPojo;
import com.dianba.pos.order.vo.LifeOrderVo;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import com.xlibao.metadata.order.OrderEntry;
import java.math.BigDecimal;
import java.util.List;

public interface LifeOrderManager {

    String BASE_URL = "order/";

    String GET_ORDER = BASE_URL + "getOrder";

    String PREPARE_CREATE_ORDER = BASE_URL + "prepareCreateOrder";

    String GENERATE_ORDER = BASE_URL + "generateOrder";

    String PAYMENT_ORDER = BASE_URL + "paymentOrder";

    String CONFIRM_ORDER = BASE_URL + "confirmOrder";

    /**
     * 获取订单详情-2.0
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
     * 预创建订单-2.0
     */
    BasicResult prepareCreateOrder(long passportId, OrderTypeEnum orderType);

    /**
     * 生成一个订单-2.0
     */
    BasicResult generateOrder(long passportId, String sequenceNumber, String phoneNumber
            , long actualPrice
            , List<OrderItemPojo> orderItems) throws Exception;

    /**
     * 确认支付订单-2.0
     */
    BasicResult paymentOrder(Long orderId, PaymentTypeEnum paymentTypeEnum);

    /**
     * 确认完成订单-2.0
     */
    BasicResult confirmOrder(long passportId, long orderId);

    /**
     * 创建订单
     */
    BasicResult createOrder(Long passportId, Integer orderType, BigDecimal actualPrice
            , String phoneNumber
            , List<OrderItemPojo> orderItems) throws Exception;

    /**
     * 批量保存离线订单
     *
     * @param orders
     * @return
     */
    BasicResult syncOfflineOrders(List<OrderPojo> orders);


    /**
     * 获取订单详情，使用商家ID
     */
    BasicResult getOrderForPos(Long passportId, Integer orderType, Integer orderStatus
            , Integer pageNum, Integer pageSize);

    BasicResult getMerchantProfitInfo(Long merchantId, String phone);

    /**
     * POS端交易记录
     * @param merchantId
     * @param enterType
     * @param createTime
     * @return
     */
    BasicResult findOrderTransactionRecord(Long merchantId,Integer enterType,String createTime);


}
