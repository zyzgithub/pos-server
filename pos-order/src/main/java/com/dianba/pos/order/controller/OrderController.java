package com.dianba.pos.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.JsonHelper;
import com.dianba.pos.order.config.OrderURLConstant;
import com.dianba.pos.order.exception.BusinessException;
import com.dianba.pos.order.po.Order;
import com.dianba.pos.order.service.OrderManager;
import com.xlibao.common.BasicWebService;
import com.xlibao.common.constant.order.OrderTypeEnum;
import com.xlibao.metadata.order.OrderEntry;
import org.apache.commons.lang.StringUtils;
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

    @ResponseBody
    @RequestMapping("create_order")
    public AjaxJson createOrderFromSuperMarket(HttpServletRequest request
            , @RequestParam(value = "merchantId") Integer merchantId
            , @RequestParam(value = "cashierId") Integer cashierId
            , @RequestParam(value = "mobile", required = false) String mobile
            , String params, String version, String uuid) {
        AjaxJson j = new AjaxJson();
        logger.info("开始创建超市订单, merchentId :" + merchantId + ", cashierId:" + cashierId + ", params:" + params);
        try {
            if (StringUtils.isBlank(version)) {
                j = AjaxJson.failJson("您的版本号过低, 请先安装最新版本后使用!");
                return j;
            }
            String remark = OrderManager.SUPERMARKET_ORDER_PREFIX;
            Order oDto = orderManager.createOrderFromSuperMarket(merchantId, cashierId, mobile
                    , params, null, remark + uuid);
            if (oDto == null || oDto.getOrderId() == null) {
                j.setMsg("创建订单失败");
                j.setStateCode("01");
                j.setSuccess(false);
                j.setObj(oDto);
            } else {
                j.setObj(oDto);
                j.setStateCode("00");
                j.setSuccess(true);
                j.setMsg("创建订单成功");
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            j = AjaxJson.failJson(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg("创建订单失败");
            j.setStateCode("01");
            j.setSuccess(false);
            logger.warn("创建超市订单失败");
        }
        logger.info("创建超市订单, return:{}", JSON.toJSONString(j));
        return j;
    }

    /**
     * @param request
     * @param passportId  通行证ID
     * @param orderType   订单类型-7：超市，8：一键采购，9：增值服务
     * @param actualPrice 应收金额
     * @param totalPrice  实收金额
     * @param items       商品集合
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("create_order1")
    public BasicResult createOrder(HttpServletRequest request
            , long passportId, int orderType, double actualPrice, double totalPrice
            , String items) throws Exception {
        List<Map<String, Object>> orderItems = JsonHelper.toList(items);
        if (orderItems.size() < 0) {
            throw new Exception("订单商品为空！" + items);
        }
        Map<Integer, OrderTypeEnum> orderTypeEnumMap = new HashMap<>();
        orderTypeEnumMap.put(OrderTypeEnum.POS_SCAN_ORDER_TYPE.getKey(), OrderTypeEnum.POS_SCAN_ORDER_TYPE);
        orderTypeEnumMap.put(OrderTypeEnum.POS_EXTENDED_ORDER_TYPE.getKey(), OrderTypeEnum.POS_EXTENDED_ORDER_TYPE);
        orderTypeEnumMap.put(OrderTypeEnum.POS_PURCHASE_ORDER_TYPE.getKey(), OrderTypeEnum.POS_PURCHASE_ORDER_TYPE);
        if (orderTypeEnumMap.get(orderType) != null) {
            OrderTypeEnum orderTypeEnum = orderTypeEnumMap.get(orderType);
            BasicResult basicResult = orderManager.prepareCreateOrder(passportId, orderTypeEnum.getKey() + "");
            if (basicResult.isSuccess()) {
                String sequenceNumber = basicResult.getResponse().getString("sequenceNumber");

                basicResult = orderManager.generateOrder(passportId, sequenceNumber, orderTypeEnum
                        , (long) (actualPrice * 100), (long) (totalPrice * 100), orderItems);
            }
            return basicResult;
        } else {
            throw new Exception("订单类型非法！请使用" + orderTypeEnumMap.toString());
        }
    }

    @ResponseBody
    @RequestMapping("order_detail")
    public BasicResult getOrderDetail() {
        long orderId = getLongParameter("orderId");
        BasicResult basicResult = BasicResult.createSuccessResult();
        OrderEntry orderEntry = orderManager.getOrder(orderId);
        basicResult.setResponse(JSONObject.parseObject(JSON.toJSON(orderEntry).toString()));
        return basicResult;
    }


    public static void main(String[] args) {
        String items = "[{\"itemId\":\"1\"}]";
        JSONArray jsonArray = JSON.parseArray(items);
        System.out.println(jsonArray.toJSONString());
    }
}
