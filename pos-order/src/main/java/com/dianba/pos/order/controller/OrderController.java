package com.dianba.pos.order.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.order.config.OrderURLConstant;
import com.dianba.pos.order.pojo.OrderItemPojo;
import com.dianba.pos.order.pojo.OrderPojo;
import com.dianba.pos.order.service.LifeOrderManager;
import com.xlibao.common.BasicWebService;
import com.xlibao.metadata.order.OrderEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping(OrderURLConstant.ORDER)
public class OrderController extends BasicWebService {

    private static Logger logger = LogManager.getLogger(OrderController.class);

    @Autowired
    private LifeOrderManager orderManager;

    /**
     * POS下单
     *
     * @param request
     * @param passportId  通行证ID
     * @param orderType   订单类型-7：超市，9：增值服务
     * @param totalPrice  实收金额
     * @param items       商品集合
     * @param phoneNumber 充值手机号码(增值服务)
     */
    @ResponseBody
    @RequestMapping("create_order")
    public BasicResult createOrder(HttpServletRequest request
            , long passportId, int orderType, BigDecimal totalPrice, String items
            , @RequestParam(required = false) String phoneNumber) throws Exception {
        List<OrderItemPojo> orderItems = JsonHelper.toList(items, OrderItemPojo.class);
        if (orderItems.size() < 0) {
            return BasicResult.createFailResult("订单商品为空！" + items);
        }
        return orderManager.createOrder(passportId, orderType, totalPrice, phoneNumber, orderItems);
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
        basicResult.setResponse(orderEntry);
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
