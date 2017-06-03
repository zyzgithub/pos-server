package com.dianba.pos.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.order.config.OrderURLConstant;
import com.dianba.pos.order.pojo.OrderPojo;
import com.dianba.pos.order.service.OrderManager;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.metadata.order.OrderEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(OrderURLConstant.ORDER)
public class OrderController extends BasicWebService {

    private static Logger logger = LogManager.getLogger(OrderController.class);

    @Autowired
    private OrderManager orderManager;

    /**
     * POS下单
     *
     * @param request
     * @param passportId  通行证ID
     * @param orderType   订单类型-7：超市，9：增值服务
     * @param actualPrice 应收金额
     * @param totalPrice  实收金额
     * @param items       商品集合
     * @param phoneNumber 充值手机号码(增值服务)
     */
    @ResponseBody
    @RequestMapping("create_order")
    public BasicResult createOrder(HttpServletRequest request
            , long passportId, int orderType, double actualPrice, double totalPrice, String items
            , @RequestParam(required = false) String phoneNumber) throws Exception {
        List<Map<String, Object>> orderItems = JsonHelper.toList(items);
        if (orderItems.size() < 0) {
            return BasicResult.createFailResult("订单商品为空！" + items);
        }
        Map<Integer, OrderTypeEnum> orderTypeEnumMap = new HashMap<>();
        orderTypeEnumMap.put(OrderTypeEnum.POS_SCAN_ORDER_TYPE.getKey(), OrderTypeEnum.POS_SCAN_ORDER_TYPE);
        orderTypeEnumMap.put(OrderTypeEnum.POS_EXTENDED_ORDER_TYPE.getKey(), OrderTypeEnum.POS_EXTENDED_ORDER_TYPE);
        if (orderTypeEnumMap.get(orderType) != null) {
            OrderTypeEnum orderTypeEnum = orderTypeEnumMap.get(orderType);
            BasicResult basicResult = orderManager.prepareCreateOrder(passportId, orderTypeEnum);
            if (basicResult.isSuccess()) {
                String sequenceNumber = basicResult.getResponse().getString("sequenceNumber");
                basicResult = orderManager.generateOrder(passportId, sequenceNumber, phoneNumber
                        , (long) (actualPrice * 100), (long) (totalPrice * 100), orderItems);
            }
            return basicResult;
        } else {
            return BasicResult.createFailResult("订单类型非法！请使用" + orderTypeEnumMap.toString());
        }
    }

    /**
     * 一键采购
     *
     * @param passportId  通行证ID
     * @param warehouseId 仓库id
     * @param itemSets    JSONObject类型，{"10000":"2"} key为商品ID:value是商品采购数量。
     */
    @ResponseBody
    @RequestMapping("create_purchase_order")
    public BasicResult createOrder(HttpServletRequest request
            , Long passportId, Long warehouseId, String itemSets) throws Exception {
//        warehouseId = 100001L;
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.PURCHASE_ORDER_TYPE;
        BasicResult basicResult = orderManager.prepareCreateOrder(passportId, orderTypeEnum);
        if (basicResult.isSuccess()) {
            String sequenceNumber = basicResult.getResponse().getString("sequenceNumber");
            JSONObject jsonObject = JSONObject.parseObject(itemSets);
            return orderManager.generatePurchaseOrder(passportId, sequenceNumber, warehouseId, jsonObject);
        }
        return basicResult;
    }

    /**
     * 获取订单详情
     *
     * @param orderId 订单ID
     */
    @ResponseBody
    @RequestMapping("order_detail")
    public BasicResult getOrderDetail(long orderId) {
        BasicResult basicResult = BasicResult.createSuccessResult();
        OrderEntry orderEntry = orderManager.getOrder(orderId);
        basicResult.setResponse(JSONObject.parseObject(JSON.toJSON(orderEntry).toString()));
        return basicResult;
    }

    /**
     * 根据商家ID获取订单列表
     *
     * @param merchantPassportId 商家ID
     * @return
     */
    @ResponseBody
    @RequestMapping("get_order")
    public BasicResult getOrderForMerchant(long merchantPassportId, int pageNum, int pageSize) {
        return orderManager.getOrderForMerchant(merchantPassportId, pageNum, pageSize);
    }

    /**
     * 同步离线订单
     */
    @ResponseBody
    @RequestMapping("sync_offline_order")
    public BasicResult syncOffLineOrder(List<OrderPojo> orders) {
        //TODO 同步离线订单
        return BasicResult.createSuccessResult();
    }
}
