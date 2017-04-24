package com.wm.controller.order;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.wm.service.impl.supermarket.SuperMarketServiceImpl;
import com.wm.util.*;
import jodd.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alipay.service.AlipayService;
import com.base.enums.OrderStateEnum;
import com.base.exception.BusinessException;
import com.wm.controller.order.dto.OrderFromMerchantDTO;
import com.wm.controller.order.dto.OrderFromSuperMarketDTO;
import com.wm.entity.address.AddressEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.LogisticsEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.OrderEntityVo;
import com.wm.entity.user.WUserEntity;
import com.wm.service.address.AddressServiceI;
import com.wm.service.courier.CourierServiceI;
import com.wm.service.deduct.DeductLogServiceI;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.message.WmessageServiceI;
import com.wm.service.order.DineInOrderServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PrintServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.PayServiceI;
import com.wm.service.supermarket.SuperMarketServiceI;
import com.wm.service.user.WUserServiceI;

/**
 * @author wuyong
 * @version V1.0
 * @Title: Controller
 * @Description: 订单
 * @date 2015-01-07 10:00:57
 */
@Controller
@RequestMapping("ci/orderController")
public class OrderController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderServiceI orderService;
    @Autowired
    private PayServiceI payService;
    @Autowired
    private OrderStateServiceI orderStateService;
    @Autowired
    private AddressServiceI addressService;
    @Autowired
    private WUserServiceI wUserService;
    @Autowired
    private CourierServiceI courierService;
    @Autowired
    private PrintServiceI printService;
    @Autowired
    private DeductLogServiceI deductLogService;
    @Autowired
    private DineInOrderServiceI dineInOrderService;
    @Autowired
    private MenuServiceI menuService;
    @Autowired
    private WmessageServiceI messageService;
    @Autowired
    private SuperMarketServiceI superMarketService;


    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 生成未支付订单
     *
     * @param userId         用户ID
     * @param cityId         城市ID
     * @param merchantId     商家ID
     * @param mobile         用户手机
     * @param realname       接收人姓名
     * @param address        送餐地址
     * @param params         菜单参数集合
     * @param request
     * @param timeRemark送餐时间
     * @param saleType       订餐类型 1为外卖 2 为堂食
     * @return
     */
    @RequestMapping(params = "createOrder")
    @ResponseBody
    public AjaxJson createOrder(String mobile, String realname, String address,
                                String params, String title, String timeRemark,
                                HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        int userId = 0;
        int merchantId = 0;
        int saleType = 1;
        if (request.getParameter("userId") != null
                && !"".equals(request.getParameter("userId"))) {
            userId = Integer.parseInt(request.getParameter("userId"));
        }
        if (request.getParameter("merchantId") != null
                && !"".equals(request.getParameter("merchantId"))) {
            merchantId = Integer.parseInt(request.getParameter("merchantId"));
        }
        if (request.getParameter("saleType") != null
                && !"".equals(request.getParameter("saleType"))) {
            saleType = Integer.parseInt(request.getParameter("saleType"));
        }

        j = orderService.verifyMenuQuantity(params, userId);// 验证菜品库存是否还有

        if (j.isSuccess()) {// 判断菜品库存是否还有

            int orderId = orderService.createOrder(userId, merchantId, mobile,
                    realname, address, params, title, "normal", saleType,
                    timeRemark);

            if (orderId == 0) {
                j.setMsg("订单金额非法");
                j.setStateCode("01");
                j.setSuccess(false);
            } else {
                orderStateService.createOrderState(orderId);// 订单生成状态信息
                Map<String, Object> objs = new HashMap<String, Object>();
                objs.put("orderId", orderId);
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                list.add(objs);

                j.setObj(list);
                j.setSuccess(true);
            }
        }

        return j;
    }

    @RequestMapping(params = "createOrderFromSuperMarket")
    @ResponseBody
    public AjaxJson createOrderFromSuperMarket(HttpServletRequest request, Integer merchantId,
                                               Integer cashierId, String params, String version, String uuid) {
        AjaxJson j = new AjaxJson();
        logger.info("开始创建超市订单, merchentId :" + merchantId + ", cashierId:" + cashierId + ", params:" + params);
        try {
            if (org.apache.commons.lang.StringUtils.isBlank(version)) {
                j = AjaxJson.failJson("您的版本号过低, 请先安装最新版本后使用!");
                return j;
            }
            String remark = SuperMarketServiceImpl.superMarketOrderPrefix;
            OrderFromSuperMarketDTO oDto = superMarketService.createOrderFromSuperMarket(merchantId, cashierId, params, null, remark + uuid);
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
     * 获得快递员订单
     */
    @RequestMapping(params = "getCourierOrderListById")
    @ResponseBody
    public AjaxJson getCourierOrderListById(String courierId, String state, String start, String
            num, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();

        List<Map<String, Object>> list = orderService.getCourierOrderListById(courierId, state, start, num);

        j.setObj(list);
        j.setSuccess(true);
        return j;
    }

    /**
     * 获得快递员订单
     *
     * @param courierId  快递员编号
     * @param state      订单状态
     * @param queryParam 查询条件：用户名，电话号码，排号，订单号
     * @param start      起始行
     * @param num        每页显示行数
     * @param request
     * @return
     */
    @RequestMapping(params = "getCourierOrderList")
    @ResponseBody
    public AjaxJson getCourierOrderList(String courierId, String state, String queryParam, Integer start, Integer
            num, String startDate, String endDate) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isNotEmpty(courierId)) {
            if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
                startDate = DateTime.now().toString("yyyy-MM-dd");
                endDate = startDate;
            }
            List<Map<String, Object>> list = orderService.getCourierOrderList(courierId, state, queryParam, start, num, startDate, endDate);
            if (list != null && list.size() > 0) {
                j.setObj(list);
                j.setSuccess(true);
            } else {
                j.setMsg("未找到匹配的订单");
                j.setSuccess(false);
            }
        } else {
            j.setMsg("查询订单失败，快递员ID不允许为空！");
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 获得商家订单
     */
    @RequestMapping(params = "getMerchantOrderListByMerchantId")
    @ResponseBody
    public AjaxJson getMerchantOrderListByMerchantId(String merchantId,
                                                     String state, @RequestParam(defaultValue = "1") Integer
                                                             start, @RequestParam(defaultValue = "10") Integer num) {
        AjaxJson j = new AjaxJson();
        try {
            List<Map<String, Object>> list = orderService.getMerchantOrderListByMerchantId(merchantId, state, start, num);
            j.setObj(list);
            j.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j;
    }

    /**
     * 获取订单详情:商家端
     *
     * @param orderId
     * @return
     */
    @RequestMapping(params = "selectListDetail")
    @ResponseBody
    public AjaxJson selectListDetail(int orderId, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();

        List<Map<String, Object>> list = orderService.selectListDetail(orderId);

        j.setObj(list);
        j.setSuccess(true);
        return j;
    }


    /**
     * 获取订单详情：快递端
     *
     * @param orderId
     * @return
     */
    @RequestMapping(params = "getOrderDetail")
    @ResponseBody
    public AjaxJson getOrderDetail(int orderId, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            List<Map<String, Object>> list = orderService.getOrderDetail(orderId);

            j.setObj(list);
            j.setSuccess(true);
            return j;
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg("获取订单详情失败");
            j.setSuccess(false);
            j.setStateCode("01");
            return j;
        }
    }

    @RequestMapping(params = "getOrderDynamic")
    @ResponseBody
    public AjaxJson getOrderDynamic(int orderId, String state,
                                    HttpServletRequest request) {
        AjaxJson j = new AjaxJson();

        List<Map<String, Object>> list = orderService
                .getOrderDynamicById(orderId);
        j.setObj(list);
        j.setSuccess(true);
        return j;
    }

    /**
     * 买单订单生成
     *
     * @param merchantid
     * @param userid
     * @param mobile
     * @param cardid
     * @param cardMoney
     * @param score
     * @param balance
     * @param alipayBalance
     * @param payType
     * @param price
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "directPayOrder")
    @ResponseBody
    public AjaxJson directPayOrder(int merchantid, int userid, String mobile,
                                   String cardid, int cardMoney, int score, double balance,
                                   double alipayBalance, String payType, double price,
                                   HttpServletRequest request, HttpServletResponse response) throws Exception {
        AjaxJson j = new AjaxJson();
        int orderId = orderService.createDirectPayOrder(userid, merchantid, price);
        if (orderId == 0) {
            j.setMsg("非法支付");
            j.setStateCode("01");
            j.setSuccess(false);
        } else {
            j = payService.directPayOrderPay(orderId, userid, mobile, cardid,
                    cardMoney, score, balance, alipayBalance, payType, request,
                    response);
        }
        return j;
    }

    /**
     * 订单退款
     *
     * @param orderid
     * @param refundReason
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "orderRefund")
    @ResponseBody
    public AjaxJson orderRefund(int orderid, String refundReason) throws Exception {
        AjaxJson j = new AjaxJson();
        orderService.orderRefund(orderid, refundReason);
        return j;
    }

    @RequestMapping(params = "merchantAccept")
    @ResponseBody
    public AjaxJson merchantAccept(int merchantid, int orderid) throws Exception {
        AjaxJson j = new AjaxJson();

        OrderEntity order = orderService.get(OrderEntity.class, orderid);
        if (!orderService.merchantAcceptOrder(order)) {
            j.setStateCode("01");
            j.setMsg("处理失败");
            j.setSuccess(false);
        } else {
            if (OrderEntity.SaleType.TAKEOUT.equals(order.getSaleType())) {
                orderService.pushOrder(orderid);
            }
            printService.print(order, false);
            j.setStateCode("00");
            j.setMsg("打印成功");
            j.setSuccess(true);
        }

        return j;
    }

    /**
     * 商家拒接订单
     *
     * @param merchantid
     * @param orderid
     * @param refundReason
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "merchantNoAccept")
    @ResponseBody
    public AjaxJson merchantNoAccept(int merchantid, int orderid,
                                     String refundReason) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!orderService.kitMerchantUnAcceptOrder(orderid, merchantid, refundReason)) {
            j.setStateCode("01");
            j.setMsg("处理失败。");
        }
        return j;
    }

    /**
     * 用户申请退单
     *
     * @param orderid
     * @param refundReason
     * @return
     */
    @RequestMapping(params = "askRefund")
    @ResponseBody
    public AjaxJson askRefund(int orderid, String refundReason) {
        AjaxJson j = new AjaxJson();

        orderService.askRefund(orderid, refundReason);

        return j;
    }

    /**
     * 商家接受退单
     *
     * @param merchantid
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "acceptRefund")
    @ResponseBody
    public AjaxJson acceptRefund(int merchantid, int orderid) throws Exception {
        AjaxJson j = new AjaxJson();
        j.setMsg("退款失败");
        j.setSuccess(false);
        j.setStateCode("01");
        boolean refundStatus = orderService.acceptRefund(orderid, merchantid);
        if (refundStatus) {
            j.setMsg("退款成功");
            j.setSuccess(refundStatus);
            j.setStateCode("00");
        }
        return j;
    }

    /**
     * 商家拒绝退单
     *
     * @param merchantid
     * @param orderid
     * @return
     */
    @RequestMapping(params = "unacceptRefund")
    @ResponseBody
    public AjaxJson unacceptRefund(int merchantid, int orderid) {
        AjaxJson j = new AjaxJson();
        orderService.unacceptRefund(orderid);
        return j;
    }

    @RequestMapping(params = "deliveryBegin")
    @ResponseBody
    public AjaxJson deliveryBegin(int courierId, int orderId) {
        if (orderService.isSupplyChainOrder(orderId)) {
            // 这个是供应链订单 走供应链的服务接口
            return orderService.supplyOrderDistribution(courierId, orderId, false);
        }
        AjaxJson j = new AjaxJson();
        OrderEntity orderEntity = orderService.get(OrderEntity.class, orderId);
        Map<String, Object> stateMap = new HashMap<String, Object>();
        if (orderEntity == null) {
            j.setObj(stateMap);
            j.setMsg("找不到对应的订单");
            j.setSuccess(false);
            j.setStateCode("01");
            return j;
        }
        // 订单处于申请退款，暂不能配送
        if ("askrefund".equals(orderEntity.getRstate())) {
            stateMap.put("state", orderEntity.getRstate());
            j.setObj(stateMap);
            j.setMsg("该订单处于申请退款中,暂时不能配送");
            j.setSuccess(false);
            j.setStateCode("01");
            return j;
        }
        // 订单处于已退款状态不能配送
        if ("berefund".equals(orderEntity.getRstate())) {
            stateMap.put("state", orderEntity.getRstate());
            j.setObj(stateMap);
            j.setMsg("此订单已退款");
            j.setSuccess(false);
            j.setStateCode("01");
            return j;
        }
        // 订单处于已取消状态不能配送
        if (StringUtils.equals(orderEntity.getState(), OrderStateEnum.CANCEL.getOrderStateEn())) {
            stateMap.put("state", orderEntity.getState());
            j.setObj(stateMap);
            j.setMsg("此订单已取消");
            j.setSuccess(false);
            j.setStateCode("01");
            return j;
        }
        // 订单的快递员id与当前快递员id不等不能配送
        if (courierId != orderEntity.getCourierId().intValue()) {
            stateMap.put("state", "courierIdError");
            j.setObj(stateMap);
            j.setMsg("您没有抢到此单");
            j.setSuccess(false);
            j.setStateCode("01");
            return j;
        }
        if (!orderService.deliveryBegin(orderId, courierId)) {
            j.setObj("error");
            j.setMsg("错误的二维码");
            j.setStateCode("01");
        } else {
            j.setObj("success");
            j.setMsg("配送成功！");
            j.setStateCode("00");
        }
        return j;
    }

    @RequestMapping(params = "confirmOrder")
    @ResponseBody
    public AjaxJson confirmOrder(Integer orderId, Integer userId) throws Exception {
        AjaxJson j = new AjaxJson();
        j.setMsg("暂无信息");
        j.setSuccess(false);
        j.setStateCode("01");
        if (userId == null || userId == 0) {
            j.setMsg("用户id不能为空");
            return j;
        }
        if (orderId == null || orderId == 0) {
            j.setMsg("订单id不能为空");
            return j;
        }
        boolean flag = orderService.confirmOrder(orderId, userId);
        j.setSuccess(flag);
        j.setObj(flag);
        j.setMsg(flag ? "确认成功" : "确认失败");
        return j;
    }

    @RequestMapping(params = "confirmOrders")
    @ResponseBody
    public AjaxJson deliveryDoneOrders(String orderIdsStr, Integer courierId) throws Exception {
        AjaxJson j = new AjaxJson();
        boolean flag = orderService.deliveryDoneOrders(orderIdsStr, courierId);
        if (flag) {
            j.setSuccess(true);
        } else {
            j.setSuccess(false);
            j.setMsg("批量完成订单失败");
            j.setStateCode("01");
        }
        return j;
    }

    @RequestMapping(params = "deliveryDone")
    @ResponseBody
    public AjaxJson deliveryDone(Integer orderId, Integer courierId) throws Exception {
        AjaxJson j = new AjaxJson();
        if (orderService.isSupplyChainOrder(orderId)) {
            j.setMsg("您不能确认供应链订单");
            j.setSuccess(false);
            j.setStateCode("01");
            return j;
        }
        j.setMsg("暂无信息");
        j.setSuccess(false);
        j.setStateCode("01");
        if (courierId == null || courierId == 0) {
            j.setMsg("快递员id不能为空");
            return j;
        }
        if (orderId == null || orderId == 0) {
            j.setMsg("订单id不能为空");
            return j;
        }
        OrderEntity orderEntity = orderService.get(OrderEntity.class, orderId);

        if (orderEntity == null) {
            j.setMsg("找不到对应的订单");
            j.setSuccess(false);
            j.setStateCode("01");
            return j;
        }
        // 订单处于已退款状态不能完成
        if ("berefund".equals(orderEntity.getRstate())) {
            j.setMsg("此订单已退款");
            j.setSuccess(false);
            return j;
        }

        // 订单处于已取消状态不能完成
        if (StringUtils.equals(orderEntity.getState(), "cancel")) {
            j.setMsg("此订单已取消");
            j.setSuccess(false);
            return j;
        }
        // 不是当前快递员配送的不能完成
        if (courierId != orderEntity.getCourierId().intValue()) {
            j.setMsg("此单不是您送的");
            j.setSuccess(false);
            return j;
        }
        boolean result = orderService.deliveryDone(courierId, orderId);
        j.setSuccess(result);
        j.setObj(result);
        j.setMsg(result ? "确认成功" : "确认失败");
        return j;
    }

    /**
     * 快递员代付
     *
     * @param orderid
     * @param courierid
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "courierPayOrder")
    @ResponseBody
    public AjaxJson courierPayOrder(
            @RequestParam Integer orderid,
            @RequestParam Integer courierid,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        AjaxJson j = new AjaxJson();

        OrderEntity order = orderService.get(OrderEntity.class, orderid);

        logger.info("订单状态：" + order.getState() + "----------支付状态：" + order.getPayState());
        //step 1 判断当前快递员 courierid 是否是配送该订单
        if (order.getCourierId().equals(courierid)
                && OrderStateEnum.DELIVERY.getOrderStateEn().equals(order.getState())
                && "unpay".equals(order.getPayState())) {
            //step 2 计算订单需要代付金额以及获取快递员余额
            WUserEntity courier = wUserService.get(WUserEntity.class, courierid);
            if (null != courier) {
                double courierMoney = courier.getMoney();
                double orderNeedPay = (Math.rint(order.getOrigin() * 100) + Math.rint(order.getDeliveryFee() * 100)
                        - Math.rint(order.getCard() * 100) - Math.rint(order.getScoreMoney() * 100)) / 100;

                logger.info("快递员余额：" + courierMoney + "-----------订单应付金额:" + orderNeedPay);
                if (courierMoney >= orderNeedPay) {
                    //快递员代付订单
                    Integer userId = order.getWuser().getId();
                    logger.info("----订单用户id:" + userId);
                    WUserEntity user = wUserService.get(WUserEntity.class, userId);
                    boolean flag = orderService.payOrderByCourier(order, courier, user);
                    if (flag) {
                        j.setMsg("代付成功");
                        j.setSuccess(true);
                    } else {
                        j.setMsg("余额不足");
                        j.setStateCode("01");
                        j.setSuccess(false);
                    }
                    return j;
                } else {
                    j.setMsg("余额不足");
                    j.setStateCode("01");
                    j.setSuccess(false);
                    return j;
                }
            }
        }

        j.setMsg("操作有误");
        j.setStateCode("01");
        j.setSuccess(false);
        return j;
    }


    @RequestMapping(params = "courierAliPayOrder")
    @ResponseBody
    public AjaxJson courierAliPayOrder(@RequestParam Integer orderid,
                                       @RequestParam Integer courierid,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            OrderEntity order = orderService.get(OrderEntity.class, orderid);

            logger.info("订单状态：" + order.getState() + "----------支付状态：" + order.getPayState());
            if (order.getCourierId().equals(courierid)
                    && OrderStateEnum.DELIVERY.getOrderStateEn().equals(order.getState())
                    && "unpay".equals(order.getPayState())) {
                String orderInfo = orderService.aliPayOrderByCourier(order);

                j.setMsg("支付宝支付参数生成成功");
                j.setSuccess(true);
                j.setObj(orderInfo);
            } else {
                j.setMsg("操作有误");
                j.setStateCode("01");
                j.setSuccess(false);
                return j;
            }
        } catch (Exception e) {
            j.setMsg("操作有误");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }


    @RequestMapping(params = "courierWeixinPayOrder")
    @ResponseBody
    public AjaxJson courierWeixinPayOrder(@RequestParam Integer orderid,
                                          @RequestParam Integer courierid,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            OrderEntity order = orderService.get(OrderEntity.class, orderid);

            logger.info("订单状态：" + order.getState() + "----------支付状态：" + order.getPayState());
            if (order.getCourierId().equals(courierid)
                    && OrderStateEnum.DELIVERY.getOrderStateEn().equals(order.getState())
                    && "unpay".equals(order.getPayState())) {
                Map<String, String> wxpay = orderService.weixinPayOrderByCourier(order);
                if (wxpay.get("return_code").equals("SUCCESS")
                        && wxpay.get("result_code").equals("SUCCESS")) {
                    j.setObj(wxpay);
                    j.setSuccess(true);
                    j.setStateCode("00");
                    j.setMsg("微信APP支付参数生成成功");
                } else {
                    j.setSuccess(false);
                    j.setObj(wxpay);
                    j.setStateCode("01");
                    j.setMsg("微信APP支付参数生成失败");
                    logger.error("微信APP支付参数生成失败, weixin return:" + JSON.toJSONString(wxpay));
                }
            } else {
                j.setMsg("操作有误");
                j.setStateCode("01");
                j.setSuccess(false);
                return j;
            }
        } catch (Exception e) {
            j.setMsg("操作有误");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }


    @RequestMapping(params = "createPhoneOrder", method = RequestMethod.POST)
    @ResponseBody
    public AjaxJson createPhoneOrder(@Valid OrderFromMerchantDTO orderDTO, BindingResult result, HttpServletRequest
            request) {
        AjaxJson j = new AjaxJson();

        if (!result.hasErrors()) {
            return createOrderFromMobile(orderDTO, request);
        }

        j.setMsg("提交参数有误");
        j.setStateCode("01");
        j.setSuccess(false);
        return j;
    }

    @RequestMapping(params = "printOrder")
    @ResponseBody
    public AjaxJson printOrder(String orderId, String printType, String extendParams, String realPrint) {
        logger.info("printOrder】orderId:{}, printType:{}, extendParams:{}", orderId, printType, extendParams);
        AjaxJson j = new AjaxJson();
        j.setStateCode("01");
        j.setMsg("打印失败，无此订单" + orderId);
        j.setSuccess(false);
        if (!NumberUtils.isDigits(orderId)) {
            return j;
        }
        int oId = NumberUtils.toInt(orderId);
        OrderEntity orderEntity = orderService.get(OrderEntity.class, oId);
        if (orderEntity == null) {
            return j;
        }
        if (!StringUtil.isEmpty(printType)) {
            JSONArray extendParamArray = JSON.parseArray(extendParams);
            printService.dineOrderPrint(orderEntity, printType, extendParamArray);
        } else {
            printService.print(orderEntity, "Y".equalsIgnoreCase(realPrint));
        }

        j.setStateCode("00");
        j.setMsg("打印成功");
        j.setSuccess(true);

        return j;
    }

    @RequestMapping(params = "autoPrintOrder")
    @ResponseBody
    public AjaxJson autoPrintOrder(String orderId, String realPrint) {

        logger.info("1.8调用1.5 autoPrintOrder打印 orderId:{}", orderId);
        AjaxJson j = new AjaxJson();
        j.setStateCode("00");
        j.setMsg("打印成功");
        j.setSuccess(true);

        if (!NumberUtils.isDigits(orderId)) {
            return j;
        }

        final OrderEntity orderEntity = orderService.get(OrderEntity.class, NumberUtils.toInt(orderId));
        if (orderEntity == null) {
            logger.error("print order:{} not exists!!!", orderId);
            return j;
        }

        printService.print(orderEntity, "Y".equalsIgnoreCase(realPrint));

        return j;
    }

    /**
     * 远程下单打印
     *
     * @param orderId
     * @return
     */
    @RequestMapping(params = "orderPrint")
    @ResponseBody
    public AjaxJson orderPrint(Integer orderId, String realPrint) {
        AjaxJson j = new AjaxJson();
        j.setStateCode("01");
        j.setMsg("打印失败，无此订单" + orderId);
        j.setSuccess(false);
        if (!printService.orderPrint(orderId, "Y".equalsIgnoreCase(realPrint))) {
            j.setStateCode("01");
            j.setMsg("打印失败");
            j.setSuccess(false);
        } else {
            j.setStateCode("00");
            j.setMsg("打印成功");
            j.setSuccess(true);
        }

        return j;
    }


    /**
     * 根据订单号查询订单并修改订单状态关联快递员
     *
     * @param orderNum
     * @return
     */
    @RequestMapping(params = "queryNumbers")
    @ResponseBody
    public AjaxJson queryNumbers(String orderNum, HttpServletRequest request) {
        int courierId = 0;
        if (request.getParameter("courierId") != null
                && !"".equals(request.getParameter("courierId"))) {
            courierId = Integer.parseInt(request.getParameter("courierId"));
        }
        AjaxJson j = new AjaxJson();
        String sql = "select o.order_num,o.id from `order` o where o.order_num=? and o.state in ('accept','delivery') ";
        int numLeght = orderNum.length();
        if (numLeght <= 8) {
            java.text.DecimalFormat df = new java.text.DecimalFormat("0000");
            orderNum = df.format(Integer.parseInt(orderNum));
            orderNum = new SimpleDateFormat("yyyyMMdd").format(new Date())
                    + orderNum;
        }
        List<Map<String, Object>> orderList = orderService.findForJdbc(sql, orderNum);// 查询订单
        if (orderList == null || orderList.size() == 0) {
            j.setMsg("订单不存在");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }
        if (orderList.size() > 1) {
            j.setMsg("订单超出范围");
            j.setStateCode("01");
            j.setSuccess(false);
        } else {
            int orderId = 0;
            Map<String, Object> map = orderList.get(0);
            orderId = Integer.parseInt(map.get("id").toString());
            OrderEntity order = orderService.get(OrderEntity.class, orderId);
            WUserEntity user = wUserService.get(WUserEntity.class, courierId);
            if (order != null) {
                if ("delivery".equals(order.getState())) {
                    j.setMsg("该订单已经被快递员" + user.getUsername() + "接了");
                    j.setStateCode("01");
                    j.setSuccess(false);
                    return j;
                }
                order.setState("delivery");
                order.setCourierId(courierId);
                order.setDeliveryTime(DateUtils.getSeconds());
                orderService.saveOrUpdate(order);
                orderStateService.deliveryOrderState(orderId);
                messageService.sendMessage(order, "delivery"); // 发送开始配送公众号模版信息
                j.setMsg("接单成功");
                j.setStateCode("00");
            } else {
                j.setMsg("订单不存在");
                j.setStateCode("01");
            }
        }
        return j;
    }

    /**
     * 用户查询当天最新订单详情
     *
     * @return
     * @throws ParseException
     */
    @RequestMapping(params = "newestOrderDetails")
    @ResponseBody
    public AjaxJson newestOrderDetails(String userId) {
        AjaxJson j = new AjaxJson();
        Integer uId = 0;
        if (userId != null && !"".equals(userId)) {
            uId = Integer.parseInt(userId);
        }
        j = orderService.newestOrderDetails(uId);
        return j;

    }

    /**
     * 查询等待快递员接收订单列表
     *
     * @return
     */
    @RequestMapping(params = "selectWaitDeliveryOrder")
    @ResponseBody
    public AjaxJson selectWaitDeliveryOrder(String pageNo, String rowsNo) {
        AjaxJson j = new AjaxJson();
        int page = 0;
        int rows = 10;
        if (pageNo != null && !"".equals(pageNo)) {
            page = Integer.parseInt(pageNo);
        }

        if (rowsNo != null && !"".equals(rowsNo)) {
            rows = Integer.parseInt(rowsNo);
        }

        List<Map<String, Object>> list = orderService.selectWaitDeliveryOrder(page, rows);
        j.setObj(list);
        j.setMsg("操作成功");
        j.setSuccess(true);
        return j;
    }


    /**
     * 根据商家ID和用户手机号码分页查询订单列表
     *
     * @param merchanId
     * @param Moblie
     * @return
     */
    @RequestMapping(params = "getOrderByMerchanAndMoblie")
    @ResponseBody
    public AjaxJson getOrderByMerchanAndMoblie(String merchanId, String Moblie, String pageNo, String rowsNo) {
        AjaxJson j = new AjaxJson();
        int page = 0;
        int rows = 10;
        if (pageNo != null && !"".equals(pageNo)) {
            page = Integer.parseInt(pageNo);
        }
        if (rowsNo != null && !"".equals(rowsNo)) {
            rows = Integer.parseInt(rowsNo);
        }
        List<Map<String, Object>> list = orderService.getOrderByMerchanAndMoblie(merchanId, Moblie, page, rows);
        j.setMsg("操作成功");
        j.setStateCode("00");
        j.setObj(list);
        return j;
    }

    /**
     * 判断商家有没有营业
     *
     * @return
     */
    @RequestMapping(params = "merchantWhetherDoBusiness")
    @ResponseBody
    public AjaxJson merchantWhetherDoBusiness(String merchantId) {
        AjaxJson j = new AjaxJson();
        int mId = 0;
        if (merchantId != null && !"".equals(merchantId)) {
            mId = Integer.parseInt(merchantId);
        }
        if (orderService.merchantWhetherDoBusiness(mId)) {
            j.setMsg("正在营业当中");
            j.setStateCode("00");
            j.setSuccess(true);
        } else {
            j.setMsg("还没有开店");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 根据courierId查询快递员评价排名或查询全部快递排名
     *
     * @return
     */
    @RequestMapping(params = "getCourierRanking")
    @ResponseBody
    public AjaxJson getCourierRanking(int courierId) {
        AjaxJson j = new AjaxJson();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = orderService.getCourierRanking(courierId);
        j.setObj(list);
        j.setMsg("操作成功");
        j.setSuccess(true);
        return j;
    }

    /**
     * 厨房制作完成
     *
     * @return
     */
    @RequestMapping(params = "kitchenMakeAccomplish")
    @ResponseBody
    public AjaxJson kitchenMakeAccomplish(int orderId) {
        AjaxJson j = new AjaxJson();
        orderService.kitchenMakeAccomplish(orderId);
        return j;
    }

    /**
     * 根据商家ID和订单类型查询已经支付订单
     *
     * @param SaleType
     * @param merchanId
     * @return
     */
    @RequestMapping(params = "getOrderBySaleType")
    @ResponseBody
    public AjaxJson getOrderBySaleType(int SaleType, int merchanId) {
        AjaxJson j = new AjaxJson();
        List<Map<String, Object>> list = orderService.getOrderBySaleType(
                SaleType, merchanId, "pay");
        j.setObj(list);
        j.setStateCode("00");
        j.setSuccess(true);
        j.setMsg("操作成功");
        return j;
    }

    /**
     * 根据商家ID和订单状态 分页查询订单
     *
     * @param merchanId
     * @param state
     * @param pageNo
     * @param row
     * @return
     */
    @RequestMapping(params = "getOrderByState")
    @ResponseBody
    public AjaxJson getOrderByState(HttpServletRequest request, int merchanId) {
        AjaxJson j = new AjaxJson();
        String code = "notstart";
        int saleType = 1;
        int codeType = 0;
        int pageNo = 1;
        int row = 100;
        if (request.getParameter("code") != null
                && !"".equals(request.getParameter("code").trim())) {
            code = request.getParameter("code");
        }
        if (request.getParameter("saleType") != null
                && !"".equals(request.getParameter("saleType").trim())) {
            saleType = Integer.parseInt(request.getParameter("saleType"));
        }
        if (request.getParameter("codeType") != null
                && !"".equals(request.getParameter("codeType").trim())) {
            codeType = Integer.parseInt(request.getParameter("codeType"));
        }
        if (request.getParameter("pageNo") != null
                && !"".equals(request.getParameter("pageNo").trim())) {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        }
        if (request.getParameter("row") != null
                && !"".equals(request.getParameter("row").trim())) {
            row = Integer.parseInt(request.getParameter("row"));
        }
        if (row >= 100) {
            row = 100;
        }
        List<Map<String, Object>> list = orderService.getOrderByState(
                merchanId, code, saleType, codeType, pageNo, row);
        j.setObj(list);
        j.setStateCode("00");
        j.setSuccess(true);
        j.setMsg("操作成功");
        return j;
    }

    @RequestMapping(params = "getOrderByStateGroup")
    @ResponseBody
    public AjaxJson getOrderByStateGroup(HttpServletRequest request, int merchanId) {
        AjaxJson j = new AjaxJson();
        int saleType = 1;

        if (request.getParameter("saleType") != null
                && !"".equals(request.getParameter("saleType").trim())) {
            saleType = Integer.parseInt(request.getParameter("saleType"));
        }

        List<Map<String, Object>> list = orderService.getOrderByStateGroup(
                merchanId, saleType);
        j.setObj(list);
        j.setStateCode("00");
        j.setSuccess(true);
        j.setMsg("操作成功");
        return j;
    }

    /**
     * 厨房完成堂食订单
     *
     * @param orderId
     */
    @RequestMapping(params = "completeOrder")
    @ResponseBody
    public AjaxJson completeOrder(int orderId) {
        AjaxJson j = new AjaxJson();
        orderService.completeOrder(orderId);
        j.setStateCode("00");
        j.setSuccess(true);
        j.setMsg("操作成功");
        return j;

    }

    /**
     * 厨房开始制作堂食订单
     *
     * @param orderId
     */
    @RequestMapping(params = "startExecutionOrder")
    @ResponseBody
    public AjaxJson startExecutionOrder(int orderId, String realPrint) {
        AjaxJson j = new AjaxJson();
        orderService.startExecutionOrder(orderId);
        OrderEntity order = orderService.get(OrderEntity.class, orderId);
        printService.print(order, "Y".equalsIgnoreCase(realPrint));
        j.setStateCode("00");
        j.setSuccess(true);
        j.setMsg("操作成功");
        return j;
    }

    /**
     * 根据商家ID查询未支付的外卖普通订单
     *
     * @param MerchantId
     */
    @RequestMapping(params = "getMerchantOrderByOrderPay")
    @ResponseBody
    public AjaxJson getMerchantOrderByOrderPay(Integer merchantId,
                                               Integer start, Integer num) {
        AjaxJson j = new AjaxJson();
        List<Map<String, Object>> list = orderService.getMerchantOrderByOrderPay(merchantId, start, num);
        j.setObj(list);
        j.setMsg("操作成功");
        j.setStateCode("00");
        j.setSuccess(true);
        return j;
    }

    /**
     * 修改外卖普通订单为电话订单
     *
     * @param order
     * @throws Exception
     */
    @RequestMapping(params = "updateOrderByPhoneOrder")
    @ResponseBody
    public AjaxJson updateOrderByPhoneOrder(Integer orderId) throws Exception {
        AjaxJson j = new AjaxJson();

        if (!orderService.verifyMenuRepertoryquantity(orderId)) {// 验证菜单库存是否充足
            j.setMsg("菜单库存不足");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }

        OrderEntity order = orderService.get(OrderEntity.class, orderId);

        if (order == null) {
            j.setMsg("订单不存在");
            j.setStateCode("01");
            j.setSuccess(false);
        } else {
            orderService.updateOrderByPhoneOrder(order);
            if (OrderEntity.SaleType.TAKEOUT.equals(order.getSaleType())) {
                orderService.pushOrder(orderId);
            }
            j.setMsg("修改订单为电话订单成功");
            j.setStateCode("00");
            j.setSuccess(true);
            // 打印电话小票
            printService.print(order, false);

        }

        return j;
    }

    /**
     * 根据类型查询申请取消订单列表或已经取消订单列表
     *
     * @param type       1为申请取消订单列表 2为已经取消订单列表
     * @param merchantId 商家ID
     * @param start      开始记录数
     * @param num        显示条数
     * @return
     */
    @RequestMapping(params = "askRefundOrderList")
    @ResponseBody
    public AjaxJson askRefundOrderList(int type, int merchantId, int start, int num) {
        AjaxJson j = new AjaxJson();
        List<Map<String, Object>> list = orderService.askRefundOrderList(type,
                merchantId, start, num);
        j.setObj(list);
        j.setMsg("操作成功");
        j.setStateCode("00");
        j.setSuccess(true);
        return j;
    }

    /**
     * 查询大于10分钟的未支付订单列表
     *
     * @param merchantId 商家ID
     * @param start      开始记录数
     * @param num        显示条数
     * @return
     */
    @RequestMapping(params = "getOrderByPayState")
    @ResponseBody
    public AjaxJson getOrderByPayState(int merchantId, int start, int num) {
        AjaxJson j = new AjaxJson();

        List<Map<String, Object>> list = orderService.getOrderByPayState(
                merchantId, start, num);
        j.setObj(list);
        j.setStateCode("00");
        j.setSuccess(true);
        j.setMsg("操作成功");
        return j;
    }

    /**
     * 生成最新订单排号
     * @return
     */
//	@RequestMapping(params = "getOrderNum")
//	@ResponseBody
//	public AjaxJson getOrderNum() {
//		AjaxJson j = new AjaxJson();
//		logger.info("--------HTTP请求获取订单排号接口---");
//		orderService.getOrderNum();
//		return j;
//	}

    /**
     * 自由抢单
     *
     * @param courierId
     * @return
     */
    @RequestMapping(params = "scramble")
    @ResponseBody
    public AjaxJson scramble(@RequestParam Integer courierId) {
        AjaxJson j = new AjaxJson();
        try {
            WUserEntity wUserEntity = wUserService.get(WUserEntity.class, courierId);
            if (wUserEntity == null) {
                j.setSuccess(false);
                j.setMsg("找不到该快递员");
                return j;
            }
            if (wUserEntity.getUserState() != null) {
                if (wUserEntity.getUserState().equals(5)) {
                    j.setSuccess(false);
                    j.setMsg("您已被锁定，不能抢单");
                    return j;
                }
            }
            j = orderService.scramble(courierId);
            courierService.saveScrambleLog(courierId, j);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            j.setMsg("抢单失败");
            j.setSuccess(false);
            j.setStateCode("01");
        }
        return j;
    }

    /**
     * 抢特定订单-快递员管理-抢单
     *
     * @param courierId 快递员id
     * @param orderId   订单id
     * @return
     */
    @RequestMapping(params = "scrambleOrder")
    @ResponseBody
    public AjaxJson scrambleOrder(@RequestParam Integer courierId, @RequestParam Integer orderId) {
        AjaxJson j = orderService.scrambleOrder(courierId, orderId);
        courierService.saveScrambleLog(courierId, j);
        return j;
    }

    /**
     * 昨天收入详情
     *
     * @param courierId
     * @param incomeType 分组类型：快餐、咖啡。。。
     * @return
     */
    @RequestMapping(params = "getIncome")
    @ResponseBody
    public AjaxJson getIncome(Integer courierId, String incomeType) {
        AjaxJson j = new AjaxJson();
        // 我的收入,统计时间设置为昨天
        String startDate = DateTime.now().minusDays(1).toString("yyyy-MM-dd");
        String endDate = startDate;
        List<Map<String, Object>> list = deductLogService.getCourierDeductGroups(
                courierId, startDate, endDate, incomeType);
        j.setObj(list);
        j.setSuccess(true);
        return j;
    }

    @RequestMapping(params = "getCourierOrderNumbers")
    @ResponseBody
    public AjaxJson getCourierOrderNumbers(Integer courierId) {
        AjaxJson j = new AjaxJson();
        // 我的收入,统计时间设置为昨天
        Map<String, Object> map = orderService.getCourierOrderNumbers(courierId);
        j.setObj(map);
        j.setSuccess(true);
        return j;
    }

    public AjaxJson createOrderFromMobile(OrderFromMerchantDTO orderDTO, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        //step 1 根据手机号查询，该手机号是否存在
        logger.info("--------电话订单，来源" + orderDTO.getFromType() + ", 电话来源:" + orderDTO.getMobile());
        WUserEntity user = wUserService.getUserByUserNameOrMobile(orderDTO.getMobile(), "user");
        //如果该手机号不存在，插入一条新数据
        if (null == user) {
            logger.info("---------电话订单，电话用户未存在数据库中，准备写入数据库");
            user = new WUserEntity();
            user.setMobile(orderDTO.getMobile());
            user.setUsername(orderDTO.getMobile());
            user.setNickname(orderDTO.getRealname());
            user.setCreateTime(DateUtils.getSeconds());
            user.setIp(IPUtil.getRemoteIp(request));
            user.setIsDelete(WUserEntity.SERVING_STATE);
            wUserService.save(user);
            logger.info("------电话订单,保存新用户成功----");
        }
        AddressEntity address = new AddressEntity();
        address.setUserId(user.getId());
        address.setName(orderDTO.getRealname());
        address.setMobile(orderDTO.getMobile());
        address.setCreateTime(new Date());
        address.setAddressDetail(orderDTO.getAddress());
        address.setIsDefault("N");
        address.setBuildingFloor(orderDTO.getFloor());
        address.setBuildingId(orderDTO.getBuildingId());
        address.setBuildingName(orderDTO.getBuildingName());
        addressService.save(address);

        //step 3 验证菜品库存是否还有
        logger.info("电话订单的菜单详情：" + orderDTO.getParams());
        j = orderService.verifyMenuQuantity(orderDTO.getParams(), 0);
        if (j.isSuccess()) {
            Integer orderId = orderService.createMobileOrder(user, address, orderDTO);
            if (null != orderId) {
                logger.info("-----电话订单生成成功，id--" + orderId);
                //生成订单。电话订单不需经过网上付款步骤
                //生成排号及基本信息并更新
                OrderEntity order = orderService.get(OrderEntity.class, orderId);
                order.setOrderNum(AliOcs.genOrderNum(order.getMerchant().getId().toString()));
                order.setState("accept");
                order.setPayState(orderDTO.getPayState());
                order.setAccessTime(DateUtils.getSeconds());
                order.setIsMerchantDelivery("courier");//电话订单全部都 是快递员配送
                order.setPayTime(orderDTO.getPayTime());
                orderService.updateEntitie(order);
                //打印订单小票
                printService.print(order, false);
                // 销量统计
                menuService.buyCount(orderId);
                //状态日志
                orderStateService.createPhoneOrderState(orderId);
                //推送快递员
                orderService.pushOrder(orderId);

                Map<String, Object> objs = new HashMap<String, Object>();
                objs.put("orderId", orderId);
                objs.put("orderMoney", order.getOrigin());
                objs.put("orderNum", order.getOrderNum());
                objs.put("orderPayId", order.getPayId());
                objs.put("userId", user.getId());
                objs.put("username", user.getUsername());
                objs.put("password", "");

                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                list.add(objs);
                j.setObj(list);
                j.setSuccess(true);
                j.setMsg("下单成功");
                return j;

            } else {
                j.setMsg("生成订单失败");
                j.setStateCode("01");
                j.setSuccess(false);
                return j;
            }

        }
        return j;
    }

    @RequestMapping(params = "findOrderList")
    @ResponseBody
    public AjaxJson findOrderList(HttpServletRequest request,
                                  OrderEntityVo order,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "20") Integer rows) {
        AjaxJson j = new AjaxJson();
        PageList<OrderEntity> list = orderService.findOrderList(order, page, rows);
        j.setObj(list);
        j.setStateCode("00");
        j.setSuccess(true);
        j.setMsg("操作成功");
        return j;
    }

    /**
     * 私厨商家接收订单
     *
     * @param merchantid
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "kitMerchantAccept")
    @ResponseBody
    public AjaxJson kitMerchantAccept(int merchantid, int orderid) throws Exception {
        AjaxJson j = new AjaxJson();
        j.setStateCode("01");
        j.setSuccess(false);
        if ((Integer) merchantid == null) {
            j.setMsg("商家ID不能为空");
            return j;
        }
        if ((Integer) orderid == null) {
            j.setMsg("订单id不能为空");
            return j;
        }

        if (!orderService.kitMerchantAcceptOrder(merchantid, orderid)) {
            j.setMsg("处理失败");
        } else {
            j.setStateCode("00");
            j.setMsg("处理成功");
            j.setSuccess(true);
        }

        return j;
    }

    /**
     * 私厨商家拒绝订单
     *
     * @param merchantid
     * @param orderid
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "kitMerchantUnAccept")
    @ResponseBody
    public AjaxJson kitMerchantUnAccept(int merchantid, int orderid, String refundReason) throws Exception {
        AjaxJson j = new AjaxJson();
        j.setStateCode("01");
        j.setSuccess(false);
        if ((Integer) merchantid == null) {
            j.setMsg("商家ID不能为空");
            return j;
        }
        if ((Integer) orderid == null) {
            j.setMsg("订单id不能为空");
            return j;
        }
        if (refundReason == null || refundReason == "") {
            j.setMsg("拒绝理由不能为空");
            return j;
        }

        if (!orderService.kitMerchantUnAcceptOrder(orderid, merchantid, refundReason)) {
            j.setMsg("处理失败");
        } else {
            j.setStateCode("00");
            j.setMsg("处理成功");
            j.setSuccess(true);
        }

        return j;
    }

    @RequestMapping(params = "syncLogistics")
    @ResponseBody
    public AjaxJson syncLogistics(Long courierId, Long orderId, String status, Long grabTime) {
        AjaxJson json = new AjaxJson();
        json.setObj("gghgh");
        json.setMsg("success");
        return json;
    }

    /**
     * 获取昨日快递员的收入详情
     *
     * @param userId
     * @return
     */
    @RequestMapping(params = "getYesterdayIncome")
    @ResponseBody
    public AjaxJson getYesterdayIncome(@RequestParam Integer userId) {
        AjaxJson json = new AjaxJson();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String date = DateTime.now().minusDays(1).toString("yyyy-MM-dd");
            List<Map<String, Object>> incomeOne = deductLogService.getYesterdayIncome(userId, date);
            List<Map<String, Object>> incomeTwo = deductLogService.getYesterday(userId, date);

            Double orderDeduct = (Double) incomeOne.get(0).get("orderDeduct");
            Double deduct = (Double) incomeTwo.get(0).get("deduct");
            Double reward = ((Double) incomeOne.get(0).get("reward") * 100 + (Double) incomeTwo.get(0).get("reward") * 100) / 100;
            Double total = (orderDeduct * 100 + deduct * 100 + reward * 100) / 100;
            Long orders = Long.valueOf(incomeOne.get(0).get("totalorders").toString());
            Long indirectOrders = Long.valueOf(incomeTwo.get(0).get("totalorders").toString());

            map.put("orderDeduct", orderDeduct);
            map.put("indirectDeduct", deduct);
            map.put("reward", reward);
            map.put("total", total);
            map.put("orders", orders);
            map.put("indirectOrders", indirectOrders);

            json.setObj(map);
            json.setSuccess(true);
            json.setMsg("获取快递员昨日收入成功");
            json.setStateCode("00");
        } catch (Exception e) {
            json.setStateCode("01");
            json.setSuccess(false);
            json.setMsg("获取快递员昨日收入失败");
        }

        return json;
    }

    /**
     * 商家添加众包订单
     *
     * @param id        商家id
     * @param password  支付密码
     * @param longitude 经度
     * @param latitude  纬度
     * @param realname  客户名称
     * @param mobile    客户手机号码
     * @param address   客户地址
     * @param remark    备注
     * @param crowdId   众包类型id(tom_crowdsourcing_type表)
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "createCrowdsourcingOrder")
    @ResponseBody
    public AjaxJson createCrowdsourcingOrder(Integer id, String password, Double longitude,
                                             Double latitude, String realname, String mobile,
                                             String address, String remark, Integer crowdId) throws Exception {
        AjaxJson j = new AjaxJson();
        if (id == null || StringUtil.isEmpty(password) || longitude == null || latitude == null
                || StringUtil.isEmpty(realname) || StringUtil.isEmpty(mobile) || StringUtil.isEmpty(address) || crowdId == null) {

            j.setMsg("输入信息不完整");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        } else {
            j = orderService.createCrowdsourcingOrder(id, password, longitude, latitude, realname, mobile,
                    address, remark, crowdId);
            return j;
        }
    }

    /**
     * 获取众包订单列表
     *
     * @param merchantId 商家id
     * @param page       起始分页
     * @param rows       分页行数
     * @return
     */
    @RequestMapping(params = "getCrowdsourcingOrders")
    @ResponseBody
    public AjaxJson getCrowdsourcingOrders(Integer merchantId, Integer page, Integer rows) {
        AjaxJson j = new AjaxJson();
        if (merchantId == null) {
            j.setMsg("商家id不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        } else if (page == null || rows == null) {
            j.setMsg("分页不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        } else {
            List<Map<String, Object>> orders = orderService.getCrowdsourcingOrders(merchantId, page, rows);
            j.setObj(orders);
            j.setMsg("获取订单列表");
            j.setStateCode("00");
            j.setSuccess(true);
            return j;
        }
    }

    /**
     * 取消众包订单
     *
     * @param id 订单id
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "cancelCrowdsourcingOrder")
    @ResponseBody
    public AjaxJson cancelCrowdsourcingOrder(Integer orderId, Integer merchantId) throws Exception {
        AjaxJson j = new AjaxJson();
        if (orderId != null && merchantId != null) {
            return orderService.cancelCrowdsourcingOrder(orderId, merchantId);
        } else {
            j.setMsg("订单id和商家id不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }
    }

    /**
     * 商家账单统计
     *
     * @param merchantId 商家id
     * @param num        0代表全部订单 ， 1代表近1个月内的订单 ， 15代表近15天内的订单
     * @param page       分页起始
     * @param rows       分页行数
     * @return
     */
    @RequestMapping(params = "getMerchantOrderCount")
    @ResponseBody
    public AjaxJson getMerchantOrderCount(Integer merchantId, Integer num, Integer page, Integer rows) {
        AjaxJson j = new AjaxJson();
        if (merchantId != null && num != null && page != null && rows != null) {
            MerchantEntity merchant = orderService.get(MerchantEntity.class, merchantId);
            if (merchant == null) {
                j.setMsg("商家不存在");
                j.setStateCode("01");
                j.setSuccess(false);
            } else {
                Map<String, Object> map = orderService.getMerchantOrderCount(merchantId, num, page, rows);
                j.setObj(map);
                j.setMsg("商家账单明细");
                j.setStateCode("00");
                j.setSuccess(true);
            }
        } else {
            j.setMsg("需检查参数不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 获取商家每日订单详情
     *
     * @param merchantId 商家id
     * @param time       订单年－月－日
     * @param page       分页起始
     * @param rows       分页行数
     * @return
     */
    @RequestMapping(params = "getMerchantDayOrders")
    @ResponseBody
    public AjaxJson getMerchantDayOrders(Integer merchantId, String time, Integer page, Integer rows) {
        AjaxJson j = new AjaxJson();
        if (merchantId != null && !StringUtil.isEmpty(time) && page != null && rows != null) {
            MerchantEntity merchant = orderService.get(MerchantEntity.class, merchantId);
            if (merchant == null) {
                j.setMsg("商家不存在");
                j.setStateCode("01");
                j.setSuccess(false);
            } else {
                List<Map<String, Object>> list = orderService.getMerchantDayOrders(merchantId, time, page, rows);
                j.setObj(list);
                j.setMsg("商家每日订单详情");
                j.setStateCode("00");
                j.setSuccess(true);
            }
        } else {
            j.setMsg("需检查参数不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 商家自己配送：更改状态(delivery)
     *
     * @param state   订单状态
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(params = "merchantUpdateOrderState")
    @ResponseBody
    public AjaxJson merchantUpdateOrderState(String state, Integer orderId) {
        AjaxJson j = new AjaxJson();
        if (StringUtil.isEmpty(state) || orderId == null) {
            j.setMsg("订单状态和订单号不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }

        if (orderService.merchantUpdateOrderState(state, orderId)) {
            j.setMsg("订单状态修改成功");
            j.setStateCode("00");
            j.setSuccess(true);
            return j;
        } else {
            j.setMsg("订单状态修改失败");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }
    }

    /**
     * 商家端根据排号或订单号模糊查询(当天)
     *
     * @param merchantId 商家id
     * @param value      排号(orderNum)/订单号(payId)
     * @param page       分页起始
     * @param rows       分页行数
     * @return
     */
    @RequestMapping(params = "selectMerchantOrdersByValue")
    @ResponseBody
    public AjaxJson selectMerchantOrdersByValue(Integer merchantId, String value, Integer page, Integer rows) {
        AjaxJson j = new AjaxJson();
        MerchantEntity merchant = orderService.get(MerchantEntity.class, merchantId);
        if (merchant == null) {
            j.setMsg("商家merchantId:[" + merchantId + "不存在");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }
        if (StringUtil.isEmpty(value)) {
            j.setMsg("模糊查询的值不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }
        if (page == null || rows == null) {
            j.setMsg("分页不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        } else {
            j.setObj(orderService.selectMerchantOrdersByValue(merchantId, value, page, rows));
            j.setMsg("操作成功");
            j.setStateCode("00");
            j.setSuccess(true);
            return j;
        }
    }

    /**
     * 判断此订单是否商家自己接单配送
     *
     * @param order
     * @return true表示商家自己配送
     */
    @RequestMapping(params = "isMerchantDelivery")
    @ResponseBody
    public AjaxJson isMerchantDelivery(int merchantid) throws Exception {
        AjaxJson j = new AjaxJson();
        j.setSuccess(false);
        if ((Integer) merchantid == null) {
            j.setMsg("商家id不能为空");
            return j;
        }

        try {
            MerchantEntity merchant = orderService.get(MerchantEntity.class, merchantid);
            if (orderService.isMerchantDelivery(merchant)) {
                j.setMsg("商家自己配送");
                j.setStateCode("00");
                j.setSuccess(true);
            } else {
                j.setMsg("快递员配送");
                j.setStateCode("01");
                j.setSuccess(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return j;
    }

    /**
     * 接收来自1.8余额支付完成后推送给快递员的
     *
     * @param orderId
     * @return true表示商家自己配送
     */
    @RequestMapping(params = "pushOrder")
    @ResponseBody
    public AjaxJson pushOrder(@RequestParam Integer orderId) throws Exception {
        logger.info("接收来自1.8余额支付完成后推送给快递员的订单，orderId:" + orderId);
        AjaxJson j = new AjaxJson();
        j.setSuccess(false);
        try {
            orderService.pushOrder(orderId);
            j.setMsg("推送订单成功");
            j.setStateCode("00");
            j.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            j.setMsg("推送订单失败");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        logger.info("接收来自1.8余额支付完成后推送给快递员的订单，return:" + JSON.toJSONString(j));
        return j;
    }

    /**
     * 闪购订单确认商家发货
     *
     * @param orderId            订单id
     * @param logisticsName      物流名称
     * @param logisticsNumber    物流单号
     * @param logisticsSnapshort 物流快照图片
     * @return
     */
    @RequestMapping(params = "createLogisticsByMerchant")
    @ResponseBody
    public AjaxJson createLogisticsByMerchant(Integer orderId, String logisticsName, String logisticsNumber, String logisticsSnapshot) {
        AjaxJson j = new AjaxJson();
        if (orderId != null && !StringUtil.isEmpty(logisticsName) && !StringUtil.isEmpty(logisticsNumber) && !StringUtil.isEmpty(logisticsSnapshot)) {
            LogisticsEntity logistics = orderService.findUniqueByProperty(LogisticsEntity.class, "orderId", orderId);
            if (logistics != null) {
                j.setMsg("已发货！");
                j.setStateCode("01");
                j.setSuccess(false);
                return j;
            }
            int result = orderService.createLogisticsByMerchant(orderId, logisticsName, logisticsNumber, logisticsSnapshot);
            if (result == 1) {
                j.setMsg("操作成功！");
                j.setStateCode("00");
                j.setSuccess(true);
                return j;
            } else {
                j.setMsg("操作失败！");
                j.setStateCode("01");
                j.setSuccess(false);
                return j;
            }
        } else {
            j.setMsg("请检查传入参数是否为空！");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }
    }

    /**
     * 闪购订单：获得退款/退货详情
     *
     * @param orderId
     * @param state   详情状态： 1 退款       2 退货
     * @return
     */
    @RequestMapping(params = "getRefundDetail")
    @ResponseBody
    public AjaxJson getRefundDetail(Integer orderId, String state) {
        AjaxJson j = new AjaxJson();
        if (orderId != null) {
            OrderEntity order = orderService.get(OrderEntity.class, orderId);
            if (order != null) {
                Map<String, Object> map = orderService.getRefundDetail(order, state);
                j.setObj(map);
                j.setMsg("操作成功！");
                j.setStateCode("00");
                j.setSuccess(true);
            } else {
                j.setMsg("订单有误！！！");
                logger.error("订单orderId[" + orderId + "]不存在！！！");
                j.setStateCode("01");
                j.setSuccess(false);
            }
        } else {
            j.setMsg("订单id不能为空！");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 闪购订单：获得商家发货物流信息
     *
     * @param orderId
     * @param type    物流信息类型 0:商家发货,1:买家退货
     * @return
     */
    @RequestMapping(params = "getLogisticsByOrderId")
    @ResponseBody
    public AjaxJson getLogisticsByOrderId(Integer orderId, String type) {
        AjaxJson j = new AjaxJson();
        if (orderId != null) {
            OrderEntity order = orderService.get(OrderEntity.class, orderId);
            if (order != null) {
                LogisticsEntity logistics = orderService.getLogisticsByOrderId(orderId, "0");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("logistics_name", logistics.getLogisticsName());
                map.put("logistics_number", logistics.getLogisticsNumber());
                String logisticsSnapshot = logistics.getLogisticsSnapshot();
                String[] ls = new String[]{};
                if (logisticsSnapshot != null) {
                    ls = logisticsSnapshot.split(";");
                }
                map.put("logistics_snapshot", ls);
                j.setObj(map);
                j.setMsg("操作成功！");
                j.setStateCode("00");
                j.setSuccess(true);
            } else {
                j.setMsg("订单有误！！！");
                logger.error("订单orderId[" + orderId + "]不存在！！！");
                j.setStateCode("01");
                j.setSuccess(false);
            }
        } else {
            j.setMsg("订单id不能为空！");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 闪购订单：同意退款申请
     *
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(params = "flashOrderAcceptRefundApply")
    @ResponseBody
    public AjaxJson flashOrderAcceptRefundApply(Integer orderId) {
        AjaxJson j = new AjaxJson();
        if (orderId != null) {
            String refundMony = orderService.flashOrderAcceptRefundApply(orderId);
            j.setObj(refundMony);
            j.setMsg("操作成功！");
            j.setStateCode("00");
            j.setSuccess(true);
        } else {
            j.setMsg("订单id不能为空！");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 闪购订单：拒绝退款申请
     *
     * @param orderId 订单id
     * @return
     */
    @RequestMapping(params = "flashOrderUnAcceptRefundApply")
    @ResponseBody
    public AjaxJson flashOrderUnAcceptRefundApply(Integer orderId) {
        AjaxJson j = new AjaxJson();
        if (orderId != null) {
            orderService.flashOrderUnAcceptRefundApply(orderId);
            ;
            j.setMsg("操作成功！");
            j.setStateCode("00");
            j.setSuccess(true);
        } else {
            j.setMsg("订单id不能为空！");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 闪购订单：确认退款
     *
     * @param orderId    订单id
     * @param merchantId 商家id
     * @return
     */
    @RequestMapping(params = "flashOrderAcceptRefund")
    @ResponseBody
    public AjaxJson flashOrderAcceptRefund(Integer orderId, Integer merchantId) {
        AjaxJson j = new AjaxJson();
        if (orderId != null && merchantId != null) {
            Boolean flag = orderService.flashOrderAcceptRefund(orderId, merchantId);
            if (flag == true) {
                j.setMsg("退款成功!");
                j.setStateCode("00");
                j.setSuccess(true);
            } else {
                j.setMsg("退款失败!");
                j.setStateCode("01");
                j.setSuccess(false);
            }
        } else {
            j.setMsg("订单id和商家id不能为空！");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 获取物流名称列表
     *
     * @return
     */
    @RequestMapping(params = "getExpressList")
    @ResponseBody
    public AjaxJson getExpressList() {
        AjaxJson j = new AjaxJson();
        List<Map<String, Object>> list = orderService.getExpressList();
        j.setObj(list);
        j.setMsg("操作成功！");
        j.setStateCode("00");
        j.setSuccess(true);
        return j;
    }

    /**
     * 堂食订单退单
     *
     * @param orderId  订单ID
     * @param opUserId 操作人ID
     * @return 响应消息
     */
    @RequestMapping(params = "dineInOrderRefund")
    @ResponseBody
    public AjaxJson dineInOrderRefund(Integer orderId, Integer opUserId) {
        return dineInOrderService.chargeback(orderId, opUserId);
    }

    /**
     * 将订单设置为分批送订单
     *
     * @return
     */
    @RequestMapping(params = "setAsSplitDeliveryOrder")
    @ResponseBody
    public AjaxJson setOrderAsSplitDeliveryOrder(String orderNum, Integer isSplitDelivery) {
        AjaxJson j = new AjaxJson();
        // 参数检查
        if (isSplitDelivery == null) {
            isSplitDelivery = 1;
        }
        if (orderNum == null || orderNum.isEmpty()) {
            j.setMsg("参数orderNum(订单号)不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }
        try {
            String orderState = isSplitDelivery == 1 ? "splitDelivery" : "delivery";
            int rowsUpdated = this.orderService.setOrderState(orderNum, orderState);
            if (rowsUpdated > 0) {
                j.setMsg("操作成功！");
                j.setStateCode("00");
                j.setSuccess(true);
            } else {
                j.setMsg("操作失败！");
                j.setStateCode("01");
                j.setSuccess(false);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            j.setMsg("出错了");
            j.setStateCode("01");
            j.setSuccess(false);
        }

        return j;
    }

    /**
     * 获取支付宝orderInfo
     *
     * @return
     */
    @RequestMapping(params = "getAlipayOrderInfo")
    @ResponseBody
    public AjaxJson getAlipayOrderInfo(String subject, String body, String price, String orderNo, String notifyUrl) {
        AjaxJson j = new AjaxJson();
        // 检查参数
        if (subject == null || subject.isEmpty()) {
            subject = "供应链商品";
        }
        if (body == null || body.isEmpty()) {
            body = "supply";
        }
        if (price == null || price.isEmpty()) {
            j.setMsg("参数price(价格)不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }
        if (orderNo == null || orderNo.isEmpty()) {
            j.setMsg("参数orderNo(订单号)不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }
        if (notifyUrl == null || notifyUrl.isEmpty()) {
            j.setMsg("参数notifyUrl(回调地址)不能为空");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }
        try {
            String orderInfo = AlipayService.getOrderInfo(subject, body, price, orderNo, notifyUrl);
            j.setObj(orderInfo);
            j.setMsg("操作成功！");
            j.setStateCode("00");
            j.setSuccess(true);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            j.setMsg("操作失败");
            j.setStateCode("01");
            j.setSuccess(false);
        }
        return j;
    }
}

