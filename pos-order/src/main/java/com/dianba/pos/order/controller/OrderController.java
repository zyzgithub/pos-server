package com.dianba.pos.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.order.config.OrderURLConstant;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.pojo.OrderItemPojo;
import com.dianba.pos.order.pojo.OrderPojo;
import com.dianba.pos.order.pojo.WarehouseItemPojo;
import com.dianba.pos.order.service.OrderManager;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.service.PassportManager;
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
    @Autowired
    private PassportManager passportManager;

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
        List<OrderItemPojo> orderItems = JsonHelper.toList(items, OrderItemPojo.class);
        if (orderItems.size() < 0) {
            return BasicResult.createFailResult("订单商品为空！" + items);
        }
        Map<Integer, OrderTypeEnum> orderTypeEnumMap = new HashMap<>();
        orderTypeEnumMap.put(OrderTypeEnum.SCAN_ORDER_TYPE.getKey(), OrderTypeEnum.SCAN_ORDER_TYPE);
        orderTypeEnumMap.put(OrderTypeEnum.POS_EXTENDED_ORDER_TYPE.getKey(), OrderTypeEnum.POS_EXTENDED_ORDER_TYPE);
        //TODO REMOVE THIS
        if (OrderTypeEnum.POS_SCAN_ORDER_TYPE.getKey() == orderType) {
            orderType = OrderTypeEnum.SCAN_ORDER_TYPE.getKey();
        }
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
            throw new PosIllegalArgumentException("订单类型非法！" + orderType);
        }
    }

    /**
     * 一键采购下单
     *
     * @param passportId  通行证ID
     * @param warehouseId 仓库id
     * @param itemSets    JSONObject类型，{"10000":"2"} key为商品ID:value是商品采购数量。
     */
    @ResponseBody
    @RequestMapping("create_purchase_order")
    public BasicResult createOrder(HttpServletRequest request
            , Long passportId, Long warehouseId, String itemSets) throws Exception {
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.PURCHASE_ORDER_TYPE;
        Passport merchantPassport = passportManager.getPassportInfoByCashierId(passportId);
        if (merchantPassport == null) {
            throw new PosNullPointerException("商家不存在！");
        }
        BasicResult basicResult = orderManager.prepareCreateOrder(merchantPassport.getId(), orderTypeEnum);
        if (basicResult.isSuccess()) {
            List<WarehouseItemPojo> warehouseItemPojos = JsonHelper.toList(itemSets, WarehouseItemPojo.class);
            String sequenceNumber = basicResult.getResponse().getString("sequenceNumber");
            JSONObject jsonObject = new JSONObject();
            for (WarehouseItemPojo itemPojo : warehouseItemPojos) {
                jsonObject.put(itemPojo.getId() + "", itemPojo.getCount());
            }
            basicResult = orderManager.generatePurchaseOrder(merchantPassport.getId()
                    , sequenceNumber, warehouseId, jsonObject);
            if (basicResult.isSuccess()) {
                LifeOrder lifeOrder = orderManager.getLifeOrder(sequenceNumber);
                basicResult.setResponse(JSONObject.parseObject(JSONObject.toJSON(lifeOrder).toString()));
            }
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
     * pos端根据商家ID获取订单列表
     */
    @ResponseBody
    @RequestMapping("get_order")
    public BasicResult getOrderForPos(Long passportId, Integer orderType, Integer orderStatus
            , Integer pageNum, Integer pageSize) {
        return orderManager.getOrderForPos(passportId, orderType, orderStatus, pageNum, pageSize);
    }

    /**
     * 同步离线订单
     */
    @ResponseBody
    @RequestMapping("sync_offline_order")
    public BasicResult syncOffLineOrder(HttpServletRequest request, String orders) {
        List<OrderPojo> orderPojos;
        try {
            orderPojos = JsonHelper.toList(orders, OrderPojo.class);
            if (orders == null || orderPojos.isEmpty()) {
                throw new PosIllegalArgumentException("订单为空！");
            }
        } catch (Exception e) {
            if (e instanceof PosIllegalArgumentException) {
                throw e;
            } else {
                throw new PosIllegalArgumentException("订单参数非法！");
            }
        }
        return orderManager.syncOfflineOrders(orderPojos);
    }
}
