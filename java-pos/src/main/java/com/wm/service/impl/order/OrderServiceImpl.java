package com.wm.service.impl.order;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.MsgResp;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alipay.refund.FastpayRefundParam;
import com.alipay.refund.nopwd.NopwdFastpayRefundAction;
import com.alipay.refund.nopwd.NopwdFastpayRefundApplyResult;
import com.alipay.service.AlipayService;
import com.base.enums.AppTypeConstants;
import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.beust.jcommander.internal.Lists;
import com.courier_mana.common.Constants;
import com.jpush.SoundFile;
import com.wm.controller.open_api.ValidUtil;
import com.wm.controller.open_api.retail.RetailPortCall;
import com.wm.controller.order.dto.OrderDetail;
import com.wm.controller.order.dto.OrderFromMerchantDTO;
import com.wm.controller.takeout.dto.Shopcart;
import com.wm.controller.takeout.dto.ShopcartDTO;
import com.wm.controller.takeout.vo.AddressDetailVo;
import com.wm.controller.takeout.vo.MenuVo;
import com.wm.controller.takeout.vo.OrderMenuVo;
import com.wm.controller.takeout.vo.OrderMerchantVo;
import com.wm.controller.takeout.vo.OrderVo;
import com.wm.controller.takeout.vo.WeChatRefundVo;
import com.wm.dao.address.AddressDao;
import com.wm.dao.coupons.CouponsDao;
import com.wm.dao.menu.MenuDao;
import com.wm.dao.order.OrderDao;
import com.wm.dao.order.PushedOrderDao;
import com.wm.dto.order.WftRefundRequest;
import com.wm.dto.order.WftRefundResponse;
import com.wm.entity.address.AddressEntity;
import com.wm.entity.coupons.CouponsUserEntity;
import com.wm.entity.menu.MenuEntity;
import com.wm.entity.menu.MenuPromotionEntity;
import com.wm.entity.menu.MenutypeEntity;
import com.wm.entity.merchant.AgentInfoEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.merchant.TomOrderCrowdsourcingEntity;
import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.order.FlashOrderReturnEntity;
import com.wm.entity.order.LogisticsEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.order.OrderEntityVo;
import com.wm.entity.order.PushedOrderEntity;
import com.wm.entity.order.SupplyChainOrderInfoEntity;
import com.wm.entity.orderincome.OrderIncomeEntity;
import com.wm.entity.orderrefund.OrderRefundEntity;
import com.wm.entity.orderstate.OrderStateEntity;
import com.wm.entity.recharge.RechargeEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.comment.CommentServiceI;
import com.wm.service.courier.TlmStatisticsServiceI;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.impl.supermarket.SuperMarketServiceImpl;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.merchant.TpmStatisticsRealtimeServiceI;
import com.wm.service.message.WmessageServiceI;
import com.wm.service.order.DineInOrderTxServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PrintServiceI;
import com.wm.service.order.PushedOrderServiceI;
import com.wm.service.order.SupplyOrderServiceI;
import com.wm.service.order.TomOrderTimerServiceI;
import com.wm.service.order.jpush.JpushServiceI;
import com.wm.service.order.scamble.ScambleAlgorithmServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.orderrefund.OrderRefundServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.recharge.RechargeServiceI;
import com.wm.service.systemconfig.SystemconfigServiceI;
import com.wm.service.transfers.TransfersServiceI;
import com.wm.service.user.CustTypeRuleServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.AliOcs;
import com.wm.util.Dialect;
import com.wm.util.HttpClientUtil;
import com.wm.util.HttpProxy;
import com.wm.util.IDistributedLock;
import com.wm.util.MapUtil;
import com.wm.util.MemcachedDistributedLock;
import com.wm.util.PageList;
import com.wp.ConfigUtil;
import com.wxpay.service.WxPayService;

@Service("orderService")
@Transactional
public class OrderServiceImpl extends CommonServiceImpl implements OrderServiceI {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderStateServiceI orderStateService;
    @Autowired
    private FlowServiceI flowService;
    @Autowired
    private OrderIncomeServiceI orderIncomeService;
    @Autowired
    private MenuServiceI menuService;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private TomOrderTimerServiceI tomOrderTimerServiceI;
    @Autowired
    private CustTypeRuleServiceI custTypeRuleService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private WUserServiceI wUserService;
    @Autowired
    private CommentServiceI commentService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private OrderRefundServiceI orderRefundService;
    @Autowired
    private PushedOrderServiceI pushedOrderService;
    @Autowired
    private TransfersServiceI transfersService;
    @Autowired
    private RechargeServiceI rechargeService;
    @Autowired
    private AttendanceServiceI attendanceService;
    @Autowired
    private CouponsDao couponsDao;
    @Autowired
    private MerchantServiceI merchantService;
    @Autowired
    private PrintServiceI printService;
    @Autowired
    private TlmStatisticsServiceI tlmStatisticsService;
    @Autowired
    private TpmStatisticsRealtimeServiceI tpmStatisticsRealtimeService;
    @Autowired
    private ScambleAlgorithmServiceI scambleAlgorithmService;
    @Autowired
    private SystemconfigServiceI systemconfigService;
    @Autowired
    private DineInOrderTxServiceI dineInOrderTxService;
    @Autowired
    private NopwdFastpayRefundAction nopwdFastpayRefundAction;
    @Autowired
    private PushedOrderDao pushedOrderDao;
    @Autowired
    private JpushServiceI jpushService;
    @Autowired
    private WmessageServiceI messageService;
    @Autowired
    private SupplyOrderServiceI supplyOrderService;


    @Value("${autoScrableStartTime}")
    private String autoScrableStartTime;

    @Value("${autoScrableEndTime}")
    private String autoScrableEndTime;

    @Value("${courierMinDeductPrice}")
    private String courierMinDeductPrice;

    @Value("${redis_password}")
    private String password;

    @Value("${is_using_new_algorithm}")
    private boolean isUsingNewAlgorithm;


    private static final String delOrderTimerSql = "delete from tom_order_timer where order_id=?";

    @Override
    public List<Map<String, Object>> getCourierOrderListById(String courierId, String state, String start, String num) {

        if ("done".equals(state)) {
            state = "'done','confirm','delivery_done','evaluated'";
        } else {
            state = "'" + state + "'";
        }
        String sql = "SELECT distinct CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num,o.time_remark as timeRemark,o.pay_id,o.id,"
                + " o.state,o.pay_state,o.order_type,o.mobile,o.realname,o.address,CONCAT((SELECT s.value FROM "
                + " system_config s WHERE s.code='logo_url'),merchant.logo_url) logo_url,"
                + " o.origin,o.urgent_time, FROM_UNIXTIME(o.delivery_time,'%H:%i') delivery_time FROM "
                + " `order` o LEFT JOIN " + " merchant merchant ON merchant.id=o.merchant_id " + " RIGHT JOIN "
                + " order_menu o_m ON o_m.order_id=o.id LEFT JOIN " + " menu m"
                + " ON m.id=o_m.menu_id WHERE o.courier_id=" + courierId + " AND o.state in(" + state + ") ORDER BY "
                + "o.id LIMIT " + start + "," + num;

        List<Map<String, Object>> courierOrderMap = this.findForJdbc(sql);
        List<Map<String, Object>> courierOrderList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> courierOrderDetail = new ArrayList<Map<String, Object>>();

        Map<String, Object> m = new HashMap<String, Object>();

        for (int i = 0; i < courierOrderMap.size(); i++) {
            m = courierOrderMap.get(i);
            sql = "SELECT m.name,o_m.price,o_m.quantity,o.time_remark as timeRemark,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num FROM "
                    + " `order` o LEFT JOIN " + " merchant merchant ON merchant.id=o.merchant_id " + " RIGHT JOIN "
                    + " order_menu o_m ON o_m.order_id=o.id LEFT JOIN " + " menu m"
                    + " ON m.id=o_m.menu_id WHERE o.courier_id=" + courierId + " AND o.state in(" + state
                    + ") AND o.id=" + m.get("id").toString();
            courierOrderDetail = this.findForJdbc(sql);
            m.put("courierOrderDetail", courierOrderDetail);
            courierOrderList.add(m);
        }

        return courierOrderList;
    }


    /**
     * 对供应链订单进行特殊处理
     *
     * @param orders
     * @return
     */
    private List<Map<String, Object>> handleSupplyChainOrder(List<Map<String, Object>> orders) {
        if (CollectionUtils.isNotEmpty(orders)) {
            for (Map<String, Object> order : orders) {

                Integer orderId = Integer.parseInt(order.get("id").toString());
                if (isSupplyChainOrder(orderId)) {

                    SupplyChainOrderInfoEntity supplyChainOrderInfo = null;
                    try {
                        supplyChainOrderInfo = findUniqueByProperty(SupplyChainOrderInfoEntity.class, "orderId", orderId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (supplyChainOrderInfo != null) {
                        order.put("mobile", supplyChainOrderInfo.getDestMobile());
                        order.put("realname", supplyChainOrderInfo.getDestUserName());
                        order.put("address", supplyChainOrderInfo.getDestAddress());
                        order.put("origin", supplyChainOrderInfo.getTotalMoney());
                        order.put("title", supplyChainOrderInfo.getSrcName());
                        order.put("m_address", supplyChainOrderInfo.getSrcAddress());
                        order.put("m_phone", supplyChainOrderInfo.getSrcMobile());
                        order.put("m_longitude", supplyChainOrderInfo.getSrcLon());
                        order.put("m_latitude", supplyChainOrderInfo.getSrcLat());
                        order.put("u_longitude", supplyChainOrderInfo.getDestLon());
                        order.put("u_latitude", supplyChainOrderInfo.getDestLat());
                        order.put("totalMoney", supplyChainOrderInfo.getTotalMoney());

                        order.put("delivery_fee", "0.0");
                        order.put("real_price", "0.0");
                        order.put("member_discount_money", "0.0");
                        order.put("pay_state", "pay");
                        order.put("rstate", "normal");

                        List<Map<String, Object>> orderDetailMaps = new ArrayList<Map<String, Object>>();
                        List<OrderDetail> orderDetails = JSONArray.parseArray(supplyChainOrderInfo.getOrderDetails(), OrderDetail.class);
                        if (CollectionUtils.isNotEmpty(orderDetails)) {
                            for (OrderDetail orderDetail : orderDetails) {
                                Map<String, Object> orderDetailMap = new HashMap<String, Object>();
                                orderDetailMap.put("name", orderDetail.getName());
                                orderDetailMap.put("price", orderDetail.getPrice());
                                orderDetailMap.put("quantity", orderDetail.getQuantity());
                                orderDetailMap.put("timeRemark", "");
                                orderDetailMap.put("order_num", "");

                                orderDetailMaps.add(orderDetailMap);
                            }
                        }
                        order.put("courierOrderDetail", orderDetailMaps);
                    }
                }
            }
        }

        return orders;
    }

    @Override
    public List<Map<String, Object>> getCourierOrderList(String courierId, String state, String queryParam, Integer start, Integer num, String startDate, String endDate) {
        String sql = "SELECT distinct CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num, right(ifnull(o.time_remark, ''), 5) as timeRemark,o.pay_id,o.card*(-1) card,o.score_money*(-1) score_money ,o.member_discount_money*(-1) member_discount_money, "
                + " o.id,o.state,o.pay_state,o.rstate,o.order_type,o.mobile,o.realname,o.address,merchant.logo_url,o.user_id,"
                + " o.origin,o.urgent_time, FROM_UNIXTIME(o.delivery_time,'%H:%i') delivery_time, from_unixtime(o.create_time,'%m-%d %H:%i') create_day, merchant.title,case WHEN o.remark is null then '' ELSE o.remark end remark, "
                + " o.from_type, o.merchant_member_discount_money*(-1) merchant_member_discount_money, o.cost_lunch_box,"
                + " mi.merchant_source m_source," + " sc.code_name m_sourceName,"
                + " case when o.origin<500 then 1 else 2 end youxian, "
                + " case when o.from_type = 'crowdsourcing' then toc.crowdsourcing_courier_deduct/100 else o.delivery_fee end delivery_fee,  "
                + " merchant.address m_address, merchant.phone m_phone, merchant.longitude m_longitude,merchant.latitude m_latitude, "
                + " case when o.from_type = 'crowdsourcing' then toc.longitude  else (case when a.building_id is null then a.longitude else b.longitude end)  end u_longitude,"
                + " case when o.from_type = 'crowdsourcing' then toc.latitude  else (case when a.building_id is null then a.latitude else b.latitude end) end u_latitude"
                + " FROM `order` o " + " LEFT JOIN merchant merchant ON merchant.id = o.merchant_id "
                + " RIGHT JOIN order_menu o_m ON o_m.order_id = o.id " + " LEFT JOIN menu m ON m.id = o_m.menu_id "
                + " LEFT JOIN user u ON u.id = o.user_id " + " LEFT JOIN address a on a.id = o.user_address_id "
                + " LEFT JOIN 0085_building b on b.id = a.building_id "
                + " LEFT JOIN 0085_user_properties p on p.user_id=u.id "
                + " LEFT JOIN tom_order_crowdsourcing toc ON toc.order_id = o.id"
                + " LEFT JOIN 0085_merchant_info mi on mi.merchant_id= o.merchant_id"
                + " LEFT JOIN sys_code sc on sc.code = 'merchant_source' and sc.code_value = mi.merchant_source"
                + " WHERE o.courier_id=" + courierId + " AND o.state <> 'cancel' ";

        if (StringUtils.isNotEmpty(startDate)) {
            if ("confirm".equals(state)) {
                sql += " and DATE(FROM_UNIXTIME(o.complete_time)) >= '" + startDate + "'";
            } else if ("delivery".equals(state) || "accept".equals(state) || "splitDelivery".equals(state)) {// 配送中列表可以看到未代付的历史记录
                sql += " ";
            } else {
                sql += " and DATE(FROM_UNIXTIME(o.create_time)) >= '" + startDate + "'";
            }
        }
        if (StringUtils.isNotEmpty(endDate)) {
            if ("confirm".equals(state)) {
                sql += " and DATE(FROM_UNIXTIME(o.complete_time)) <= '" + endDate + "'";
            } else if ("delivery".equals(state)) {
                sql += " ";
            } else {
                sql += " and DATE(FROM_UNIXTIME(o.create_time)) <= '" + endDate + "'";
            }
        }

        if (StringUtils.isNotEmpty(state)) {
            if ("done".equals(state)) {
                state = "'done','confirm','delivery_done','evaluated'";
            } else {
                state = "'" + state + "'";
            }
            sql += " AND o.state in(" + state + ")";
        }
        if (StringUtils.isNotEmpty(queryParam)) {
            sql += " and (o.realname like '%" + queryParam + "%' ";
            sql += " or u.username like '%" + queryParam + "%' ";
            sql += " or u.nickname like '%" + queryParam + "%' ";
            sql += " or o.mobile like '%" + queryParam + "%' ";
            sql += " or o.order_num like '%" + queryParam + "%')";
        }
        sql += " ORDER BY o.id desc ";

        List<Map<String, Object>> courierOrderMap = null;
        if (start != null && num != null) {
            courierOrderMap = this.findForJdbc(sql, start, num);
        } else {
            courierOrderMap = this.findForJdbc(sql);
        }
        List<Map<String, Object>> courierOrderList = new ArrayList<Map<String, Object>>();

        sql = "SELECT m.name,o_m.price,o_m.quantity,right(ifnull(o.time_remark, ''), 5) as timeRemark, convert(substring(o.order_num,9), unsigned) order_num FROM `order` o "
                + "LEFT JOIN merchant merchant ON merchant.id = o.merchant_id "
                + "RIGHT JOIN order_menu o_m ON o_m.order_id=o.id "
                + "LEFT JOIN menu m ON m.id = o_m.menu_id WHERE o.id = ?";

        StringBuilder userBuilder = new StringBuilder();
        for (Map<String, Object> m : courierOrderMap) {
            String userId = m.get("user_id").toString();
            if (!"0".equals(userId)) {
                userBuilder.append(userId).append(",");
            }
        }
        Map<Long, Integer> userCount = new HashMap<Long, Integer>();
        Map<Long, Double> userAmount = new HashMap<Long, Double>();

        if (userBuilder.length() > 0) {
            userBuilder = userBuilder.delete(userBuilder.length() - 1, userBuilder.length());

            String userOrderSql = "select user_id, count(*) c from `order` where user_id in(" + userBuilder.toString() + ") group by user_id";
            String userOrderTotalMoneySql = "select user_id, ifnull(sum(origin), 0) total from `order` where user_id in(" + userBuilder.toString() + ") and state = 'confirm' group by user_id";


            List<Map<String, Object>> userOrders = findForJdbc(userOrderSql);
            List<Map<String, Object>> userTotalConsumAmount = this.findForJdbc(userOrderTotalMoneySql);

            for (Map<String, Object> userOrder : userOrders) {
                userCount.put(Long.parseLong(userOrder.get("user_id").toString()), Integer.parseInt(userOrder.get("c").toString()));
            }
            for (Map<String, Object> amount : userTotalConsumAmount) {
                userAmount.put(Long.parseLong(amount.get("user_id").toString()), Double.parseDouble(amount.get("total").toString()));
            }
        }
        for (Map<String, Object> m : courierOrderMap) {
            Integer userId = Integer.parseInt(m.get("user_id").toString());
            String custType = "新"; // 用户类型
            if (userId != 0) {
                Double total = userAmount.get(userId.longValue());
                total = (total == null ? 0 : total.doubleValue()) * 100;
                custType = custTypeRuleService.getCustTypeByConsumeAmount(total);
            }

            m.put("custType", custType);
            // 是否是首单用户
            Integer userOrderCount = userCount.get(userId.longValue());
            m.put("isNewUser", (userOrderCount == null || userOrderCount == 1));
            String fromType = m.get("from_type") == null ? "" : m.get("from_type").toString();
            // 如果不是众包订单,查询订单的菜品细节
            if (StringUtils.equals(fromType, "crowdsourcing")) {
                m.put("courierOrderDetail", new ArrayList<Map<String, Object>>());
            } else {
                List<Map<String, Object>> courierOrderDetail = findForJdbc(sql, new Object[]{m.get("id")});
                m.put("courierOrderDetail", courierOrderDetail);
            }
            Double origin = m.get("origin") == null ? 0.0 : Double.parseDouble(m.get("origin").toString());
            Double deliveryFee = m.get("delivery_fee") == null ? 0.0 : Double.parseDouble(m.get("delivery_fee").toString());
            Double card = m.get("card") == null ? 0.0 : Double.parseDouble(m.get("card").toString());
            Double scoreMoney = m.get("score_money") == null ? 0.0 : Double.parseDouble(m.get("score_money").toString());
            Double memberDiscountMoney = m.get("member_discount_money") == null ? 0.0 : Double.parseDouble(m.get("member_discount_money").toString());
            Double merchantMemberDiscountMoney = m.get("merchant_member_discount_money") == null ? 0.0 : Double.parseDouble(m.get("merchant_member_discount_money").toString());
            Double costLunchBox = m.get("cost_lunch_box") == null ? 0.0 : Double.parseDouble(m.get("cost_lunch_box").toString());
            Double otherCost = (deliveryFee * 100 + card * 100 + scoreMoney * 100 + memberDiscountMoney * 100 + merchantMemberDiscountMoney * 100 + costLunchBox * 100) / 100.0;
            Double totalMoney = (origin * 100 + otherCost * 100) / 100.0;

            // 解决精度损失问题
            m.put("origin", origin);
            m.put("delivery_fee", deliveryFee);
            m.put("card", card);
            m.put("scoreMoney", scoreMoney);
            m.put("memberDiscountMoney", memberDiscountMoney);
            m.put("totalMoney", String.format("%.2f", totalMoney));
            m.put("otherCost", String.format("%.2f", otherCost));
            courierOrderList.add(m);
        }

        // 对供应链订单进行特殊处理
        handleSupplyChainOrder(courierOrderList);
        return courierOrderList;
    }

    @Override
    public List<Map<String, Object>> getMerchantOrderListByMerchantId(String merchantId, String state, Integer start, Integer num) {
        if ("accept".equals(state)) {
            state = "'accept','delivery'";
        } else if ("done".equals(state)) {
            state = "'done','confirm','delivery_done','evaluated'";
        } else if ("unaccept".equals(state)) {
            state = "'unaccept','refund'";
        } else {
            state = "'" + state + "'";
        }

        String sql = "SELECT o.time_remark as timeRemark,o.sale_type,o.user_id,o.urgent_time,o.order_type,o.delivery_fee,o.cost_lunch_box,o.card,o.score_money,"
                + " o.rstate,o.pay_id,o.state,o.pay_state,o.id,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num,m.title,o.origin,o.courier_id,"
                + " o.remark,o.rereason,FROM_UNIXTIME(o.retime,'%Y-%m-%d %H:%i') retime,o.ifCourier,o.mobile,u.mobile courier_mobile, "
                + "	m.cost_delivery, o.is_merchant_delivery,o.flash_order_id, "
                + " FROM_UNIXTIME(o.create_time,'%Y-%m-%d %H:%i') create_time FROM `order` o LEFT JOIN "
                + " merchant m ON o.merchant_id=m.id LEFT JOIN `user` u ON o.courier_id = u.id "
                + " WHERE o.merchant_id=? AND o.from_type not in ('crowdsourcing','supplychain') "
                + " AND o.order_type not in ('merchant_recharge','supermarket_settlement') "
                + " AND o.state in(" + state + ") ORDER BY o.create_time DESC LIMIT ?,? ";

        List<Map<String, Object>> merchantOrderMap = this.findForJdbc(sql, Integer.parseInt(merchantId), start, num);
        List<Map<String, Object>> merchantOrderList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> merchantOrderDetail = new ArrayList<Map<String, Object>>();
        Map<String, Object> m = new HashMap<String, Object>();

        // 招商系统(操作1)：如果没有快递员或快递员没有上班或代理商保证金不足,则商家自己接单
        MerchantInfoEntity mie = this.findUniqueByProperty(MerchantInfoEntity.class, "merchantId",
                Integer.parseInt(merchantId));
        MerchantEntity me = this.get(MerchantEntity.class, Integer.parseInt(merchantId));
        boolean isMerchantDelivery = isMerchantDelivery(me);

        for (int i = 0; i < merchantOrderMap.size(); i++) {
            m = merchantOrderMap.get(i);
            MapUtil.getMapToBean(m);

            // 招商系统(操作2)：如果没有快递员或快递员没有上班或代理商保证金不足,则商家自己接单
            if (mie != null && mie.getMerchantSource() == 2) {
                if ("mobile".equals(m.get("order_type")) || "2".equals(m.get("sale_type"))) {//堂食和电话订单全部都是返回false
                    m.put("isMerchantDelivery", false);
                } else {
                    m.put("isMerchantDelivery", isMerchantDelivery);
                }
            } else {
                if ("merchant".equals(m.get("is_merchant_delivery"))) {
                    // 是否商家配送：merchant商家配送 courier快递员配送
                    m.put("isMerchantDelivery", true);
                } else {
                    m.put("isMerchantDelivery", false);
                }
            }
            Integer orderId = Integer.valueOf(m.get("id").toString());
            //判断是否是闪购订单
            Object flashId = m.get("flash_order_id");
            if (!flashId.equals(-1)) {
                String fsql = "SELECT id FROM flash_order WHERE id=?";
                Map<String, Object> fmap = this.findOneForJdbc(fsql, flashId);
                if (fmap != null && fmap.get("id") != null) {
                    m.put("order_type", "flash_order");//设置订单类型为：闪购订单

                    //闪购信息
                    List<String> flashList = new ArrayList<String>();
                    LogisticsEntity logistics = this.getLogisticsByOrderId(orderId, "0");//物流信息类型 0:商家发货,1:买家退货
                    if ("delivery".equals(m.get("state").toString())) {
                        flashList.add("物流公司：" + logistics.getLogisticsName());
                        flashList.add("快递单号：" + logistics.getLogisticsNumber());
                    } else if ("accept".equals(m.get("state").toString())) {
                        FlashOrderReturnEntity fReturn = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", orderId);
                        if (fReturn != null) {
                            String type = "退款退货";
                            if ('0' == fReturn.getType()) {
                                type = "仅退款";
                            }
                            flashList.add("退款类型：" + type);
                            flashList.add("退款时间：" + fReturn.getUpdateTime().toString().split("\\.")[0]);
                            flashList.add("退款原因：" + fReturn.getRefundDesc());
                        }
                    } else if ("berefund".equals(m.get("rstate"))) {
                        FlashOrderReturnEntity fReturn = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", orderId);
                        if (fReturn != null) {
                            String type = "退款退货";
                            if ('0' == fReturn.getType()) {
                                type = "仅退款";
                            }
                            flashList.add("退款类型：" + type);
                        }
                    }
                    m.put("flash_msg", flashList);
                }
            }

            //餐饮系统堂食订单座位号
            String seat_num = "";
            if ("2".equals(m.get("sale_type").toString())) {
                Map<String, Object> seatmap = this.getDineSeatDetail(orderId);
                if (null != seatmap && !ValidUtil.anyEmpty(seatmap.get("seat_name"))) {
                    seat_num = seatmap.get("seat_name").toString();
                }
            }
            m.put("seat_num", seat_num);

            // 判断是否可退单的堂食订单
            MsgResp chargebackPrecheck = dineInOrderTxService.chargebackPrecheck(Integer.parseInt(m.get("id").toString()));
            m.put("dineInOrderChargebackable", chargebackPrecheck.isSuccess());

            // 查询订单菜品信息
            sql = "SELECT o_m.quantity, "
                    + " IF(o_m.sales_promotion='Y',o_m.promotion_money,o_m.price) price, "
                    + " IF(o_m.sales_promotion='Y',o_m.promotion_money*o_m.quantity,o_m.total_price) total_price, "
                    + " CONCAT(s.value,m.image) image, "
                    + " IF(o_m.sales_promotion='Y',CONCAT(m.name,'(特价)'),m.name) name "
                    + " FROM order_menu o_m, menu m, system_config s "
                    + " WHERE o_m.menu_id=m.id AND o_m.order_id=" + m.get("id").toString() + " AND s.code='logo_url'";
            merchantOrderDetail = this.findForJdbc(sql);
            m.put("merchantOrderDetail", merchantOrderDetail);
            
            //POS订单备注处理
            if(m.get("remark").toString().contains(SuperMarketServiceImpl.offlineOrderPrefix)  
            		|| m.get("remark").toString().contains(SuperMarketServiceImpl.superMarketOrderPrefix)){
            	m.put("remark","");
            }
            
            // 订单总金额 订单金额+配送费+餐盒费
            Object oriObj = m.get("origin");
            Object deliObj = m.get("delivery_fee");
            Object boxObj = m.get("cost_lunch_box");
            Double totalMoney = (Double.parseDouble(oriObj.toString()) * 100
                    + Double.parseDouble(deliObj.toString()) * 100 + Double.parseDouble(boxObj.toString()) * 100)
                    / 100.0;
            // 解决精度损失问题
            m.put("origin", String.format("%.2f", oriObj));
            m.put("delivery_fee", String.format("%.2f", deliObj));
            m.put("cost_lunch_box", String.format("%.2f", boxObj));
            m.put("totalMoney", String.format("%.2f", totalMoney));

            merchantOrderList.add(m);
        }
        return merchantOrderList;
    }

    @Override
    public List<Map<String, Object>> getOrderDetail(int orderId) {
        String sql = "SELECT distinct CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num, right(ifnull(o.time_remark, ''), 5) as timeRemark,o.pay_id,o.card*(-1) card,o.score_money*(-1) score_money ,o.member_discount_money*(-1) member_discount_money, "
                + "o.id,o.state,o.pay_state,o.rstate,o.order_type,o.mobile,o.realname,o.address,merchant.logo_url,o.user_id,"
                + " o.origin,o.urgent_time, FROM_UNIXTIME(o.delivery_time,'%H:%i') delivery_time,merchant.title,case WHEN o.remark is null then '' ELSE o.remark end remark, "
                + " o.from_type, o.merchant_member_discount_money*(-1) merchant_member_discount_money, o.cost_lunch_box,"
                + " mi.merchant_source m_source," + " sc.code_name m_sourceName,"
                + " case when o.origin<500 then 1 else 2 end youxian, "
                + " case when o.from_type = 'crowdsourcing' then toc.crowdsourcing_courier_deduct/100 else o.delivery_fee end delivery_fee,  "
                + " merchant.address m_address, merchant.phone m_phone, merchant.longitude m_longitude,merchant.latitude m_latitude, "
                + " case when o.from_type = 'crowdsourcing' then toc.longitude  else (case when a.building_id is null then a.longitude else b.longitude end)  end u_longitude,"
                + " case when o.from_type = 'crowdsourcing' then toc.latitude  else (case when a.building_id is null then a.latitude else b.latitude end) end u_latitude"
                + " FROM `order` o " + " LEFT JOIN merchant merchant ON merchant.id = o.merchant_id "
                + " RIGHT JOIN order_menu o_m ON o_m.order_id = o.id " + " LEFT JOIN menu m ON m.id = o_m.menu_id "
                + " LEFT JOIN user u ON u.id = o.user_id " + " LEFT JOIN address a on a.id = o.user_address_id "
                + " LEFT JOIN 0085_building b on b.id = a.building_id "
                + " LEFT JOIN 0085_user_properties p on p.user_id=u.id "
                + " LEFT JOIN tom_order_crowdsourcing toc ON toc.order_id = o.id"
                + " LEFT JOIN 0085_merchant_info mi on mi.merchant_id= o.merchant_id"
                + " LEFT JOIN sys_code sc on sc.code = 'merchant_source' and sc.code_value = mi.merchant_source"
                + " WHERE o.id=" + orderId;

        List<Map<String, Object>> orderDetail = this.findForJdbc(sql);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> menuDetail = new ArrayList<Map<String, Object>>();
        Map<String, Object> m = new HashMap<String, Object>();

        if (orderDetail != null && orderDetail.size() > 0) {
            m = orderDetail.get(0);
            MapUtil.getMapToBean(m);
            String fromType = m.get("from_type") == null ? "" : m.get("from_type").toString();
            // 如果不是众包订单,查询订单的菜品细节
            if (StringUtils.equals(fromType, "crowdsourcing")) {
                m.put("courierOrderDetail", new ArrayList<Map<String, Object>>());
            } else {
                // 私厨特殊处理
                if (m.get("m_source") != null && StringUtils.isNotBlank(m.get("m_source").toString())) {
                    int mSource = Integer.parseInt(m.get("m_source").toString());
                    if (mSource == 2) {
                        m.put("from_type", "sichu");
                    }
                }
                sql = "SELECT m.name,o_m.price,o_m.quantity,right(ifnull(o.time_remark, ''), 5) as timeRemark,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num FROM "
                        + " `order` o LEFT JOIN " + " merchant merchant ON merchant.id=o.merchant_id " + " RIGHT JOIN "
                        + " order_menu o_m ON o_m.order_id=o.id LEFT JOIN " + " menu m"
                        + " ON m.id=o_m.menu_id WHERE o.id=?";
                menuDetail = this.findForJdbc(sql, orderId);
                m.put("courierOrderDetail", menuDetail);
            }

            list.add(m);

            Double origin = m.get("origin") == null ? 0.0 : Double.parseDouble(m.get("origin").toString());
            Double deliveryFee = m.get("delivery_fee") == null ? 0.0
                    : Double.parseDouble(m.get("delivery_fee").toString());
            Double card = m.get("card") == null ? 0.0 : Double.parseDouble(m.get("card").toString());
            Double scoreMoney = m.get("score_money") == null ? 0.0
                    : Double.parseDouble(m.get("score_money").toString());
            Double memberDiscountMoney = m.get("member_discount_money") == null ? 0.0
                    : Double.parseDouble(m.get("member_discount_money").toString());
            Double merchantMemberDiscountMoney = m.get("merchant_member_discount_money") == null ? 0.0
                    : Double.parseDouble(m.get("merchant_member_discount_money").toString());
            Double costLunchBox = m.get("cost_lunch_box") == null ? 0.0
                    : Double.parseDouble(m.get("cost_lunch_box").toString());
            Double otherCost = (deliveryFee * 100 + card * 100 + scoreMoney * 100 + memberDiscountMoney * 100
                    + merchantMemberDiscountMoney * 100 + costLunchBox * 100) / 100.0;
            Double totalMoney = (origin * 100 + otherCost * 100) / 100.0;

            // 解决精度损失问题
            m.put("origin", origin);
            m.put("delivery_fee", deliveryFee);
            m.put("card", card);
            m.put("scoreMoney", scoreMoney);
            m.put("memberDiscountMoney", memberDiscountMoney);
            m.put("totalMoney", String.format("%.2f", totalMoney));
            m.put("otherCost", String.format("%.2f", otherCost));
        }
        return list;
    }

    /**
     * scan_discount_money 扫码折扣 member_discount_money 会员折扣
     */
    @Override
    public List<Map<String, Object>> selectListDetail(int orderId) {
        String sql = "SELECT o.time_remark as timeRemark,o.sale_type,o.order_type,o.urgent_time,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num,o.delivery_fee,"
                + "o.pay_id,o.pay_type,o.pay_state,o.state,o.origin,o.credit,o.card,o.score_money,o.online_money+o.credit as 'pay_money',"
                + "FROM_UNIXTIME(`o`.create_time,'%Y-%m-%d %H:%i')`create_time`,"
                + "FROM_UNIXTIME(`o`.pay_time,'%Y-%m-%d %H:%i')`pay_time`,"
                + "FROM_UNIXTIME(`o`.access_time,'%Y-%m-%d %H:%i')`access_time`,"
                + "FROM_UNIXTIME(`o`.delivery_time,'%Y-%m-%d %H:%i')`delivery_time`,"
                + "FROM_UNIXTIME(`o`.complete_time,'%Y-%m-%d %H:%i')`complete_time`,"
                + "IF(o.address is not null,o.address,addr.address_detail) as user_address,o.remark,o.rereason,o.member_discount_money,o.merchant_member_discount_money,o.is_merchant_delivery,"
                + "IF(o.realname is not null,o.realname,u2.nickname) realname,o.cost_lunch_box,o.flash_order_id,IF(o.mobile is not null,o.mobile,u2.mobile) as user_mobile,u2.username,"
                + "u.nickname as courier_name,o.merchant_id,mer.title,mer.address as merchant_address,mer.mobile as merchant_mobile,u3.nickname as merchant_name "
                + "FROM "
                + "`order` o left join merchant mer on o.merchant_id=mer.id left join `user` u on o.courier_id=u.id "
                + "left join (select * from address where is_default = 'Y') addr on addr.user_id=o.user_id "
                + "left join `user` u2 ON u2.id=o.user_id left join `user` u3 ON u3.id=mer.user_id "
                + "WHERE o.id=" + orderId;

        List<Map<String, Object>> orderDetail = this.findForJdbc(sql);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> menuDetail = new ArrayList<Map<String, Object>>();
        Map<String, Object> m = new HashMap<String, Object>();

        if (orderDetail != null && orderDetail.size() > 0) {
            m = orderDetail.get(0);
            MapUtil.getMapToBean(m);

            if ("merchant".equals(m.get("is_merchant_delivery"))) {
                // 是否商家配送：merchant商家配送 courier快递员配送
                m.put("isMerchantDelivery", true);
            } else {
                m.put("isMerchantDelivery", false);
            }

            // 查询扫码支付折扣金额
            sql = "SELECT discount_money FROM scan_discount_log WHERE order_id=?";
            Map<String, Object> smap = this.findOneForJdbc(sql, orderId);
            Double scanMoney = 0.00;
            if (smap != null && smap.size() > 0) {
                scanMoney = Double.parseDouble(smap.get("discount_money").toString()) / 100.0;
                m.put("scan_discount_money", scanMoney.toString()); // discount_money:1元=100
            } else {
                m.put("scan_discount_money", scanMoney);
            }

            // 查询堂食折扣金额
            sql = "SELECT discount_money FROM dine_in_discount_log WHERE order_id=?";
            Map<String, Object> dmap = this.findOneForJdbc(sql, orderId);
            Double dineDiscountMoney = 0.00;
            if (dmap != null && dmap.size() > 0) {
                dineDiscountMoney = Double.parseDouble(dmap.get("discount_money").toString()) / 100.0;
                m.put("dine_discount_money", dineDiscountMoney.toString());
            } else {
                m.put("dine_discount_money", dineDiscountMoney);
            }

            // 判断是否可退单的堂食订单
            MsgResp chargebackPrecheck = dineInOrderTxService.chargebackPrecheck(orderId);
            m.put("dineInOrderChargebackable", chargebackPrecheck.isSuccess());

            //判断是否是闪购订单
            if (!"-1".equals(m.get("flash_order_id").toString())) {
                m.put("order_type", "flash_order");
                LogisticsEntity logistics = this.getLogisticsByOrderId(orderId, "0");//物流信息类型 0:商家发货,1:买家退货
                String lnum = "";
                if (logistics != null) {
                    lnum = logistics.getLogisticsNumber();
                }
                m.put("logistics_number", lnum);

            }

            // 查询菜品详情
            sql = "SELECT IF(o_m.sales_promotion='Y',CONCAT(m.name,'(特价)'),m.name) name,"
                    + "IF(o_m.sales_promotion='Y',o_m.promotion_money,o_m.price) price,"
                    + "o_m.quantity as buy_count,o.time_remark as timeRemark,o.sale_type,"
                    + "CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num "
                    + "FROM `order` o "
                    + "Right join order_menu o_m on o.id=o_m.order_id left join menu m on o_m.menu_id=m.id "
                    + "WHERE o.id=" + orderId;
            menuDetail = this.findForJdbc(sql);
            m.put("menuDetail", menuDetail);

            //餐饮系统堂食订单座位号
            String seat_num = "";
            if ("2".equals(m.get("sale_type").toString())) {
                Map<String, Object> seatmap = this.getDineSeatDetail(orderId);
                if (null != seatmap && !ValidUtil.anyEmpty(seatmap.get("seat_name"))) {
                    seat_num = seatmap.get("seat_name").toString();
                }
            }
            m.put("seat_num", seat_num);

            list.add(m);

            Object oriObj = m.get("origin");
            Object deliObj = m.get("delivery_fee");
            Object boxObj = m.get("cost_lunch_box");
            Double totalMoney = (Double.parseDouble(oriObj.toString()) * 100
                    + Double.parseDouble(deliObj.toString()) * 100 + Double.parseDouble(boxObj.toString()) * 100)
                    / 100.0;
            // 解决精度损失问题
            m.put("origin", oriObj.toString());
            m.put("delivery_fee", deliObj.toString());
            m.put("totalMoney", String.format("%.2f", totalMoney));
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getOrderDynamicById(int orderId) {
        String state = "";
        String sql = "SELECT (SELECT u.username FROM `user` u,merchant m WHERE u.id = m.user_id AND m.id = IFNULL(o.merchant_id,0)"
                + ") merchant_name,(SELECT u.username FROM `user` u WHERE u.id = o.user_id AND o.user_id = IFNULL(o.user_id,0)"
                + ") buyer_name,(SELECT u.username FROM `user` u WHERE u.id = o.courier_id AND o.courier_id = IFNULL(o.courier_id,0)"
                + ") courier_name,(SELECT u.mobile FROM `user` u WHERE u.id = o.courier_id AND o.courier_id = IFNULL(o.courier_id,0)"
                + ") courier_mobile,FROM_UNIXTIME(o.create_time,'%Y-%m-%d %H:%i')`submit_time`,FROM_UNIXTIME(o.pay_time,'%Y-%m-%d %H:%i')`pay_time`,"
                + "FROM_UNIXTIME(o.access_time,'%Y-%m-%d %H:%i')`accept_time`,FROM_UNIXTIME(o.delivery_time,'%Y-%m-%d %H:%i')`delivery_time`,"
                + "FROM_UNIXTIME(o.complete_time,'%Y-%m-%d %H:%i')`complete_time`,o.state,o.time_remark,o.sale_type FROM `order` o WHERE o.id = "
                + orderId;

        List<Map<String, Object>> list = this.findForJdbc(sql);
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        Map<String, Object> m = new HashMap<String, Object>();
        Map<String, Object> m1 = new HashMap<String, Object>();
        Map<String, Object> m2 = new HashMap<String, Object>();
        Map<String, Object> m3 = new HashMap<String, Object>();
        Map<String, Object> m4 = new HashMap<String, Object>();
        state = list.get(0).get("state").toString();

        if (list != null && list.size() > 0) {
            m.put("timeRemark", list.get(0).get("time_remark"));// 返回送餐时间
            if ("unpay".equals(state)) {
                m.put("state", "unpay");
                m.put("submit_time", list.get(0).get("submit_time"));
                m.put("buyer_name", list.get(0).get("buyer_name"));

            }
            if ("pay".equals(state)) {
                m.put("state", "unpay");
                m.put("submit_time", list.get(0).get("submit_time"));
                m.put("buyer_name", list.get(0).get("buyer_name"));

                m1.put("state", "pay");
                m1.put("pay_time", list.get(0).get("pay_time"));
                m1.put("buyer_name", list.get(0).get("buyer_name"));
            }
            if ("accept".equals(state)) {
                m.put("state", "unpay");
                m.put("submit_time", list.get(0).get("submit_time"));
                m.put("buyer_name", list.get(0).get("buyer_name"));

                m1.put("state", "pay");
                m1.put("pay_time", list.get(0).get("pay_time"));
                m1.put("buyer_name", list.get(0).get("buyer_name"));

                m2.put("state", "accept");
                m2.put("accept_time", list.get(0).get("accept_time"));
                m2.put("merchant_name", list.get(0).get("merchant_name"));
            }
            if ("delivery".equals(state)) {
                m.put("state", "unpay");
                m.put("submit_time", list.get(0).get("submit_time"));
                m.put("buyer_name", list.get(0).get("buyer_name"));

                m1.put("state", "pay");
                m1.put("pay_time", list.get(0).get("pay_time"));
                m1.put("buyer_name", list.get(0).get("buyer_name"));

                m2.put("state", "accept");
                m2.put("accept_time", list.get(0).get("accept_time"));
                m2.put("merchant_name", list.get(0).get("merchant_name"));

                m3.put("state", "delivery");
                m3.put("delivery_time", list.get(0).get("delivery_time"));
                m3.put("courier_name", list.get(0).get("courier_name"));
                m3.put("courier_mobile", list.get(0).get("courier_mobile"));
            }
            if ("done".equals(state)) {
                m.put("state", "unpay");
                m.put("submit_time", list.get(0).get("submit_time"));
                m.put("buyer_name", list.get(0).get("buyer_name"));

                m1.put("state", "pay");
                m1.put("pay_time", list.get(0).get("pay_time"));
                m1.put("buyer_name", list.get(0).get("buyer_name"));

                m2.put("state", "accept");
                m2.put("accept_time", list.get(0).get("accept_time"));
                m2.put("merchant_name", list.get(0).get("merchant_name"));

                m3.put("state", "delivery");
                m3.put("delivery_time", list.get(0).get("delivery_time"));
                m3.put("courier_name", list.get(0).get("courier_name"));
                m3.put("courier_mobile", list.get(0).get("courier_mobile"));

                m4.put("state", "done");
                m4.put("complete_time", list.get(0).get("complete_time"));
                m4.put("buyer_name", list.get(0).get("buyer_name"));
            }
            if (m != null && m.size() > 0)
                list1.add(m);
            if (m1 != null && m1.size() > 0)
                list1.add(m1);
            if (m2 != null && m2.size() > 0)
                list1.add(m2);
            if (m3 != null && m3.size() > 0)
                list1.add(m3);
            if (m4 != null && m4.size() > 0)
                list1.add(m4);
        }

        return list1;
    }

    @Override
    public Integer createOrderFromWX(Integer userId, ShopcartDTO shopcart, List<Shopcart> carts) {

        // 生成支付序列号，对于订单列表是唯一的
        String payId = String.valueOf(System.currentTimeMillis());

        // 如果是外卖，查询配送地址
        String realname = "", address = "", mobile = "";
        Integer addressId = 0;
        // 使用积分
        int score = 0;
        double scoreMoney = 0.0;
        // 优惠券红包
        String couponsId = "0";
        double couponsMoney = 0.0;
        // 配送费
        double deliveryFee = 0.0;
        // 查找用户
        WUserEntity user = wUserService.getEntity(UserEntity.class, userId);
        if (null == user)
            return null;
        // 外卖订单
        if (shopcart.getSaleType() == 1) {
            AddressEntity add = addressDao.queryLasted(userId);
            if (null == add)
                return null;
            addressId = add.getId();
            mobile = add.getMobile();
            realname = add.getName();
            if (null != add.getBuildingName())
                address += add.getBuildingName() + " ";
            if (null != add.getBuildingFloor())
                address += add.getBuildingFloor() + " 楼 ";
            address += add.getAddressDetail();

            // 查询该订单是否需要配送费
            Map<String, Object> fee = merchantService.findOneForJdbc("SELECT cost_delivery FROM merchant WHERE id = ?",
                    shopcart.getMerchantId());
            if (null != fee) {
                deliveryFee = Double.valueOf(fee.get("cost_delivery").toString());
            }
        } else {
            realname = shopcart.getUserName();
            mobile = shopcart.getUserMobile();
            address = shopcart.getUserAddress();
        }

        // 计算订单金额
        double origin = 0.0;
        for (Shopcart cart : carts) {
            origin += Math.rint(cart.getPrice() * 100) * cart.getCount();
        }
        origin /= 100;

        // 判断是否使用积分
        if (shopcart.isCredit()) {
            // 判断是否满足积分使用规则
            if (origin >= 10 && user.getScore() >= 100) {
                score = 100;
                scoreMoney = 5.0;
                user.setScore(user.getScore() - score);
                updateEntitie(user);
            }
        }

        // 判断是否使用优惠券红包
        if (shopcart.isCoupons() && null != shopcart.getCouponsId()) {
            CouponsUserEntity coupons = couponsDao.queryCouponsById(shopcart.getCouponsId(), userId);
            if (null != coupons) {
                couponsId = coupons.getId().toString();
                couponsMoney = (coupons.getCouponsMoney() / 100.0);
                coupons.setCouponsNum(coupons.getCouponsNum() - 1);
                couponsDao.updateEntitie(coupons);
            }
        }

        if (origin > 0) {
            // 订单写入数据库
            String sql = "insert into `order`" + "(pay_id, user_id, city_id, merchant_id, state, "
                    + "origin, create_time, mobile, address, realname,  "
                    + "remark, order_type, sale_type, time_remark, user_address_id, invoice, score, score_money, delivery_fee, "
                    + "card_id, card) "
                    + "values(?, ?, 0, ?, 'unpay', ?, unix_timestamp(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

            executeSql(sql, payId, userId, shopcart.getMerchantId(), origin, mobile, address, realname,
                    shopcart.getRemark(), shopcart.getOrderType(), shopcart.getSaleType(), shopcart.getTimeRemark(),
                    addressId, shopcart.getInvoice(), score, scoreMoney, deliveryFee, couponsId, couponsMoney);

            String sqlId = "select last_insert_id() as orderId";
            Map<String, Object> result = findOneForJdbc(sqlId);
            Object obj = result.get("orderId");
            if (null != obj) {
                int orderId = Integer.valueOf(obj.toString());
                // 订单子项写入数据库
                for (Shopcart c : carts) {
                    String sub = "insert into order_menu " + "(menu_id, order_id, price, quantity, "
                            + "total_price, promotion_money, " + "sales_promotion, menu_promotion_id) "
                            + "values(?, ?, ?, ?, ?, ?, ?, ?)";
                    this.executeSql(sub, c.getMenuId(), orderId, c.getPrice(), c.getCount(), c.getTotalPrice(),
                            c.getPromotionPrice(), c.getPromote(), c.getPromoteId());
                }

                return orderId;
            }
        }

        return null;
    }

    @Override
    public Integer createMobileOrder(WUserEntity user, AddressEntity address, OrderFromMerchantDTO orderDTO) {
        // step 1 解析订单菜单项
        List<Map<String, Object>> menus = JSONHelper.toList(orderDTO.getParams());
        if (menus.size() > 0) {
            List<Shopcart> carts = new ArrayList<Shopcart>(menus.size());
            Double costLunchBox = 0.00;
            for (Map<String, Object> map : menus) {
                Integer menuId = Integer.valueOf(map.get("menuId").toString());
                MenuEntity menu = this.get(MenuEntity.class, menuId);
                Integer num = Integer.valueOf(map.get("num").toString());
                if (menu != null) {
                    MenutypeEntity menuType = menu.getMenuType();
                    costLunchBox += menuType.getCostLunchBox() * num;
                }
                MenuVo vo = menuDao.findIsPromotionById(menuId, false);
                if (null == vo)
                    return null;
                Shopcart c = new Shopcart();
                c.setMenuId(menuId);
                c.setCount(num);
                c.setPrice(vo.getPrice());
                c.setPromoteId(vo.getPromotion());
                c.setPromoting(vo.isPromoting());
                carts.add(c);
            }

            String add = "";
            if (null != address.getBuildingName())
                add += address.getBuildingName() + " ";
            if (null != address.getBuildingFloor())
                add += address.getBuildingFloor() + " 楼 ";
            add += address.getAddressDetail();

            // 生成支付序列号，对于订单列表是唯一的
            String payId = String.valueOf(System.currentTimeMillis()) + Thread.currentThread().getId();
            int score = 0;
            double scoreMoney = 0.0;
            // 配送费
            double deliveryFee = 0.0;
            // 查询该订单是否需要配送费
            Map<String, Object> fee = merchantService.findOneForJdbc("SELECT cost_delivery FROM merchant WHERE id = ?",
                    orderDTO.getMerchantId());
            if (null != fee && orderDTO.getSaleType().equals(1)) {
                deliveryFee = Double.valueOf(fee.get("cost_delivery").toString());
            }
            // 计算订单金额
            double origin = 0.0;
            for (Shopcart cart : carts) {
                origin += Math.rint(cart.getPrice() * 100) * cart.getCount();
            }
            origin /= 100;

            if (origin > 0) {
                // 订单写入数据库
                String sql = "insert into `order`" + "(pay_id, user_id, city_id, merchant_id, state, "
                        + "origin, create_time, mobile, address, realname,  "
                        + "remark, order_type, sale_type, time_remark, user_address_id, invoice, score, score_money, from_type, delivery_fee, cost_lunch_box) "
                        + "values(?, ?, 0, ?, 'unpay', ?, unix_timestamp(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

                executeSql(sql, payId, user.getId(), orderDTO.getMerchantId(), origin, address.getMobile(), add,
                        address.getName(), orderDTO.getTitle(), orderDTO.getOrderType(), orderDTO.getSaleType(),
                        orderDTO.getTimeRemark(), address.getId(), orderDTO.getInvoice(), score, scoreMoney,
                        orderDTO.getFromType(), deliveryFee, costLunchBox);

                String sqlId = "select last_insert_id() as orderId";
                Map<String, Object> result = findOneForJdbc(sqlId);
                Object obj = result.get("orderId");
                if (null != obj) {
                    int orderId = Integer.valueOf(obj.toString());
                    // 订单子项写入数据库
                    for (Shopcart c : carts) {
                        String sub = "insert into order_menu " + "(menu_id, order_id, price, quantity, "
                                + "total_price, promotion_money, " + "sales_promotion, menu_promotion_id) "
                                + "values(?, ?, ?, ?, ?, ?, ?, ?)";
                        this.executeSql(sub, c.getMenuId(), orderId, c.getPrice(), c.getCount(), c.getTotalPrice(),
                                c.getPromotionPrice(), c.getPromote(), c.getPromoteId());
                    }

                    return orderId;
                }
            }
        }

        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int createOrder(int userId, int merchantId, String mobile, String realname, String address, String params,
                           String title, String orderType, int saleType, String timeRemark) {

        // 获取城市id
        MerchantEntity merchant = this.get(MerchantEntity.class, merchantId);
        int cityId = merchant.getCityId();

        List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
        Map<String, Object> m = new HashMap<String, Object>();
        int menuId = 0;
        int num = 0;
        int orderid = 0;
        double price = 0.0;
        double origin = 0.0;
        double total_price = 0.0;
        int menuPromotionId = 0;// 促销ID
        String salesPromotion = "N";
        double cost = 0;
        double dough = 0;

        paramList = JSONHelper.toList(params);

        if (params != null && paramList.size() > 0) {
            for (int i = 0; i < paramList.size(); i++) {
                m = paramList.get(i);
                salesPromotion = "N";
                menuId = Integer.valueOf(m.get("menuId").toString());
                num = Integer.valueOf(m.get("num").toString());

                if (m.get("menuPromotionId") != null && !"".equals(m.get("menuPromotionId").toString())) {
                    menuPromotionId = Integer.valueOf(m.get("menuPromotionId").toString());
                }

                if (m.get("salesPromotion") != null && !"".equals(m.get("salesPromotion").toString())) {
                    salesPromotion = m.get("salesPromotion").toString();
                }

                if (m.get("dough") != null && !"".equals(m.get("dough").toString())) {
                    dough = Double.valueOf(m.get("dough").toString());
                }

                cost += dough * num;

                // 根据菜单id获取单价
                MenuEntity menu = menuService.getEntity(MenuEntity.class, menuId);
                price = menu.getPrice();

                if ("Y".equals(salesPromotion)) {
                    MenuPromotionEntity menuPromotion = this.get(MenuPromotionEntity.class, menuPromotionId);
                    price = menuPromotion.getMoney();

                }
                // 计算总金额
                origin += num * price;
            }
            BigDecimal bigdecimal = new BigDecimal(origin);
            origin = bigdecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            BigDecimal bigdecimalCost = new BigDecimal(cost);
            cost = bigdecimalCost.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            if (dough != 0 && cost != 0) {// 判断是否是抢购订单
                if (cost != origin) {
                    return 0;
                }
            }
            // 金额校验
            if (origin <= 0) {
                return 0;
            }

            String sql = "";

            String orderT = orderType;
            String fromT = "gongzhonghao";
            if ("provider-normal".equals(orderType)) {
                orderT = orderType.replace("provider-", "");
                fromT = "GG";
            }
            if (saleType == 1) {
                // 添加订单信息
                sql = "INSERT INTO `order`(user_id,city_id,merchant_id,state,origin,create_time,mobile,address,realname,remark,order_type,sale_type,time_remark,from_type)"
                        + " values(?,?,?,'unpay',?,UNIX_TIMESTAMP(),?,?,?,?,?,?,?,?)";
                this.executeSql(sql, userId, cityId, merchantId, origin, mobile, address, realname, title, orderT,
                        saleType, timeRemark, fromT);

            } else {
                // 添加订单信息
                sql = "INSERT INTO `order`(user_id,city_id,merchant_id,state,origin,create_time,realname,remark,order_type,sale_type,from_type)"
                        + " values(?,?,?,'unpay',?,UNIX_TIMESTAMP(),?,?,?,?,?)";

                this.executeSql(sql, userId, cityId, merchantId, origin, realname, title, orderT, saleType, fromT);

            }

            long time = System.currentTimeMillis();

            sql = "select LAST_INSERT_ID() lastInsertId";
            Map lastInsertIdMap = this.findOneForJdbc(sql);
            orderid = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
            logger.info("========================orderid=" + orderid);

            String payid = RandomStringUtils.random(4, "0123456789") + Long.toString(time + orderid).substring(2);

            sql = "update `order` set pay_id=? where id=?";
            logger.info("========================payid=" + payid);
            this.executeSql(sql, payid, orderid);

            // 添加order_menu关联信息
            for (int i = 0; i < paramList.size(); i++) {
                m = paramList.get(i);
                menuId = Integer.valueOf(m.get("menuId").toString());
                num = Integer.valueOf(m.get("num").toString());

                // 根据菜单id获取单价
                MenuEntity menu = menuService.getEntity(MenuEntity.class, menuId);
                price = menu.getPrice();
                menuPromotionId = 0;
                salesPromotion = "N";// 是否促销，默认为不促销
                Double money = price;

                if (m.get("salesPromotion") != null && !"".equals(m.get("salesPromotion").toString())) {
                    salesPromotion = m.get("salesPromotion").toString();
                }

                // int menuPromotionId=0;//促销ID
                if (menuService.getMenuPromotionByMenuId(menuId).size() > 0 && "Y".equals(salesPromotion)) {
                    List<Map<String, Object>> menuPromotionList = menuService.getMenuPromotionByMenuId(menuId);
                    Map<String, Object> map = menuPromotionList.get(0);
                    if (map != null) {
                        price = Double.valueOf(map.get("money").toString());

                        salesPromotion = "Y";// 改为促销
                        menuPromotionId = Integer.valueOf(m.get("menuPromotionId").toString());
                    }
                }

                // 总价
                total_price = num * price;
                String o_msql = "insert into order_menu (MENU_ID, ORDER_ID, PRICE, QUANTITY, TOTAL_PRICE,promotion_money,sales_promotion,menu_promotion_id) values(?, ?, ?, ?, ?,?,?,?)";
                this.executeSql(o_msql, menuId, orderid, money, num, total_price, price, salesPromotion,
                        menuPromotionId);
            }

            return orderid;
        } else {
            return 0;
        }
    }


    @Override
    public int createDirectPayOrder(int userId, int merchantId, double price) {

        if (price == 0) {
            return 0;
        }

        MerchantEntity merchant = this.get(MerchantEntity.class, merchantId);

        // 添加订单信息
        String sql = "INSERT INTO `order` (user_id,city_id,merchant_id,state,origin,create_time,order_type)"
                + " values(?,?,?,'unpay',?,UNIX_TIMESTAMP(),'direct_pay')";
        this.executeSql(sql, userId, merchant.getCityId(), merchantId, price);
        long time = System.currentTimeMillis();

        sql = "select LAST_INSERT_ID() lastInsertId";
        Map<String, Object> lastInsertIdMap = this.findOneForJdbc(sql);
        int orderid = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
        logger.info("========================orderid=" + orderid);

        String payid = RandomStringUtils.random(4, "0123456789") + Long.toString(time + orderid).substring(2);

        sql = "update `order` set pay_id=? where id=?";
        logger.info("========================payid=" + payid);
        this.executeSql(sql, payid, orderid);

        return orderid;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public int createOpenTswjOrder(WUserEntity user, String out_order_id, String out_order_title, double order_money, Long create_time, String cust_mobile) {
        int cityId = 12;// i玩派的地址 广州
        int merchantId = 0;// 商户id暂时为0
        int orderid = 0;
        // 插入订单
        String sql = "INSERT INTO `order`(user_id,pay_id,city_id,merchant_id,state, "
                + "origin,create_time,mobile,address,realname, "
                + "remark,order_type,sale_type,from_type, access_time, order_num, title)"
                + " values(?,?,?,?,'unpay' ,?,?,?,?,? ,?,?,?,?,?,?,?)";
        this.executeSql(sql, user.getId(), out_order_id, cityId, merchantId, order_money, create_time / 1000,
                cust_mobile, "", user.getNickname(), "i玩派订单" + orderid, "third_part", 1, "tswj",
                System.currentTimeMillis() / 1000, AliOcs.genOrderNum("0"), out_order_title);// this.getOrderNum()
        // 获取插入订单的ID
        sql = "select LAST_INSERT_ID() lastInsertId";
        Map lastInsertIdMap = this.findOneForJdbc(sql);
        orderid = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
        if (logger.isInfoEnabled())
            logger.info("========================tswj orderid=" + orderid);

        return orderid;
    }

    @Override
    public int createRechargeOrder(int userId, double price, String title) {

        if (price == 0) {
            return 0;
        }

        // 添加订单信息
        String sql = "INSERT INTO `order` (user_id,state,origin,create_time,order_type,title)"
                + " values(?,'unpay',?,UNIX_TIMESTAMP(),'recharge',?)";
        this.executeSql(sql, userId, price, title);

        sql = "select LAST_INSERT_ID() lastInsertId";
        Map<String, Object> lastInsertIdMap = this.findOneForJdbc(sql);
        int orderid = Integer.parseInt(lastInsertIdMap.get("lastInsertId").toString());
        logger.info("========================orderid=" + orderid);

        String payid = StringUtil.generateSerialNumber("CZ");

        sql = "update `order` set pay_id=? where id=?";
        logger.info("========================payid=" + payid);
        this.executeSql(sql, payid, orderid);
        RechargeEntity recharge = new RechargeEntity();
        recharge.setOutTradeNo(payid);
        recharge.setPayType(PayEnum.alipay.getEn());
        Double doublePrice = Math.rint(price * 100);
        int totalFee = doublePrice.intValue();
        recharge.setTotalFee(totalFee);
        recharge.setUserId(userId);
        recharge.setCreateTime(DateUtils.getSeconds());
        recharge.setStatus("N");
        rechargeService.save(recharge);
        return orderid;
    }

    @Override
    public void orderRefund(int orderId, String rereason) throws Exception {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        order.setRstate("askrefund");
        order.setRetime(DateUtils.getSeconds());
        order.setRereason(rereason);
        this.saveOrUpdate(order);
        this.refund(orderId, rereason);
    }

    @Override
    public boolean merchantAcceptOrder(OrderEntity order) throws Exception {
        Integer orderId = order.getId();
        if (!"pay".equals(order.getState())) {
            return false;
        }
        boolean isoffline = StringUtils.isNotEmpty(order.getRemark()) && order.getRemark().startsWith("[offline_order]");

        MerchantEntity merchant = order.getMerchant();
        String source = merchantService.getMerchantSource(merchant.getId());
        if (OrderEntity.SaleType.TAKEOUT.equals(order.getSaleType())) {
            logger.info("外卖订单接单[订单id:{},商家id:{},商家来源:{}]", orderId, merchant.getId(), source);
            if (!Constants.MERCHANT_SOURCE_PRIVATE.equals(source) && !isMerchantDelivery(merchant)) {
                order.setIsMerchantDelivery("courier");
                order.setState("accept");
                order.setAccessTime(DateUtils.getSeconds());
                this.updateEntitie(order);
                orderStateService.accessOrderState(orderId);
                messageService.sendMessage(order, "accept");
            }
        } else {
            logger.info("堂食订单接单[订单id:{},商家id:{},商家来源:{}]", orderId, merchant.getId(), source);
            order.setIsMerchantDelivery("courier"); // 是否商家配送：merchant商家配送
            order.setAccessTime(isoffline ? order.getCreateTime() : DateUtils.getSeconds());
            if (!Constants.MERCHANT_SOURCE_PRIVATE.equals(source)) {
                order.setState("confirm");
                order.setCompleteTime(isoffline ? order.getCreateTime() : DateUtils.getSeconds());
                this.updateEntitie(order);
                orderIncomeService.createOrderIncome(order);// 添加堂食金额交易记录
                orderStateService.accessOrderState(orderId);
                messageService.sendMessage(order, "accept");
            } else {
                // 私厨商家
                if (OrderEntity.OrderType.SCAN_ORDER.equals(order.getOrderType())) {
                    order.setState("confirm");
                    order.setCompleteTime(DateUtils.getSeconds());
                    this.updateEntitie(order);
                    orderIncomeService.createOrderIncome(order);// 添加堂食金额交易记录
                    orderStateService.accessOrderState(orderId);
                    messageService.sendMessage(order, "accept");
                }
            }
        }
        return true;
    }

    /**
     * 判断此订单是否商家自己接单配送
     *
     * @param order
     * @return true表示商家自己接单
     */
    @Override
    public boolean isMerchantDelivery(MerchantEntity merchant) {

        if (merchant == null) {
            logger.info("该商家不存在！");
            return true;
        }

        // 判断商家是否绑定快递员,快递员是否接单
        if (!exsistsCanReceiveCouriers(merchant.getId())) {
            // false表示没有快递员抢单,需要商家自己配送
            return true;
        }

        // 获取商家所属的代理商
        WUserEntity wuser = merchant.getWuser();
        Integer userId = wuser.getCreator();
        if (userId != null) {
            WUserEntity duser = this.get(WUserEntity.class, userId);
            if (duser != null) {
                // 判断商家是否是代理商商家
                if ("agent".equals(duser.getUserType())) {
                    logger.info("判断代理商的保证金是否不足user_id" + duser.getId());
                    // 判断代理商的保证金是否不足
                    AgentInfoEntity agentInfo = this.findUniqueByProperty(AgentInfoEntity.class, "userId",
                            duser.getId());
                    Integer agentMinDeposit = Integer.valueOf(systemconfigService.getValByCode("agent_min_deposit")) * 100;//最低保证金额
                    if (agentInfo.getBond() < agentMinDeposit) {// 1元=100
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 商家自己配送：更改状态(delivery)
     */
    @Override
    public boolean merchantUpdateOrderState(String state, Integer orderId) {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (order == null) {
            logger.info("订单orderId:[" + orderId + "]不存在");
            return false;
        }
        if ("delivery".equals(state)) {
            order.setState(state);
            MerchantEntity merchant = order.getMerchant();
            WUserEntity user = merchant.getWuser();
            order.setCourierId(user.getId());
            order.setDeliveryTime(DateUtils.getSeconds());
            this.saveOrUpdate(order);
            orderStateService.deliveryOrderState(orderId);// 更新订单状态
            messageService.sendMessage(order, "delivery"); // 发送开始配送公众号模版信息
        }
        return true;
    }

    /**
     * 私厨商家接收订单
     *
     * @throws Exception
     */
    public boolean kitMerchantAcceptOrder(int merchantid, int orderid) throws Exception {
        OrderEntity order = this.get(OrderEntity.class, orderid);
        if (merchantid != order.getMerchant().getId()) {
            return false;
        }

        if (!order.getState().equals("pay")) {
            return false;
        }
        /**
         * sale_type 1:外卖 2：自提
         */
        int sale_type = order.getSaleType();

        order.setAccessTime(DateUtils.getSeconds());
        if (sale_type == 1) {
            order.setState("accept");
            order.setAccessTime(DateUtils.getSeconds());
            this.saveOrUpdate(order);

            orderStateService.accessOrderState(orderid);
            // 判断如果不是商家自己配送
            if (!isMerchantDelivery(order.getMerchant())) {
                // 外卖则将订单推送给快递员
                order.setIsMerchantDelivery("courier");// 是否商家配送：merchant商家配送,courier快递员配送
                this.updateEntitie(order);
                push2Courier(orderid); // 推送快速员
            }
            printService.print(order, false); // 打印小票
        } else {
            order.setIsMerchantDelivery("courier");
            order.setAccessTime(DateUtils.getSeconds());
            order.setCompleteTime(DateUtils.getSeconds());
            order.setState("confirm");
            orderIncomeService.createOrderIncome(order); // 添加堂食金额交易记录
            this.updateEntitie(order);

            orderStateService.doneOrderState(orderid);

            printService.print(order, false); // 打印小票
        }

        messageService.sendMessage(order, "accept");// 发送微信公众号商家已接单模版信息

        deleteOrderTimerByOrderId(orderid);

        return true;
    }

    /**
     * 私厨商家拒绝订单
     *
     * @throws Exception
     */
    public boolean kitMerchantUnAcceptOrder(int orderId, int merchantid, String refundReason) throws Exception {
        String sql = "select count(id) cont from merchant where id = (select merchant_id  from `order` where id = ?) ";
        Map<String, Object> map = this.findOneForJdbc(sql, orderId);
        if ("0".equals(map.get("cont").toString())) {
            deleteOrderTimerByOrderId(orderId);
            return false;
        }

        OrderEntity order = this.get(OrderEntity.class, orderId);

        // 如果不是已付款-待处理的单的情况，就不往下处理了
        if (!order.getState().equals("pay")) {
            deleteOrderTimerByOrderId(orderId);
            return false;
        }

        if (merchantid != order.getMerchant().getId()) {
            return false;
        }
        order.setRstate("askrefund");
        order.setState("cancel");
        order.setAccessTime(DateUtils.getSeconds());
        this.updateEntitie(order);
        orderStateService.noAcceptOrderState(orderId);

        /**
         * 商家同意退款
         */
        boolean b = acceptRefund(orderId, merchantid);
        if (b) {
            //恢复库存
            menuService.revertRepertory(orderId);
        } else {
            return false;
        }

        return true;
    }

    /**
     * 新需求： 一、12点前，自动匹配给绑定地址的快递员手机上，如果只有一个人匹配，则自动分配给这个快递员；
     * 如果超过一个快递员匹配，则这几个快递员抢（语音会让这几个快递员听到）； 如果是其他地址，由快递员自己选择抢单（语音，网点内的快递员全部听到）
     * 二、12点后，自由抢单，不区分地址，谁抢谁得（语音，网点内的快递员全部听到）
     */
    public void pushOrder(Integer orderId) {
        boolean isSupplyChainOrder = isSupplyChainOrder(orderId);
        if (!isSupplyChainOrder) {
            merchantService.pushOrder(orderId);
        }
        this.push2Courier(orderId);
    }

    /**
     * 获取可以配送订单的众包快递员
     *
     * @param order
     * @return
     */
    @Override
    public List<Integer> findCanReceiveCrowdsourcingCouriers(OrderEntity order) {
        MerchantEntity merchant = order.getMerchant();
        List<Integer> crowdsourcingCourierIds = new ArrayList<Integer>();
        if (merchant == null) {
            logger.error("无法根据确定订单对应的商家，订单ID:{}", order.getId());
        } else {
            if (merchant.getLng() == null || merchant.getLat() == null) {
                logger.warn("无法确定的商家地址，orderId:{}, 经纬度:{},{}", order.getId(), merchant.getLng(), merchant.getLat());
            } else {
                int pushOrderDistance = 1500;
                try {
                    pushOrderDistance = Integer.parseInt(systemconfigService.getValByCode("push_order_distance"));
                } catch (Exception e) {
                    logger.warn("获取众包快递员推单距离（单位：米）设置失败，设置为默认值1500米");
                }
                crowdsourcingCourierIds = scambleAlgorithmService.findNearestCouiriers(merchant.getLng(), merchant.getLat(), pushOrderDistance, Integer.MAX_VALUE);
                logger.info("订单(orderId:" + order.getId() + ")" + ",可以配送订单包括(没有上班)的众包快递员:"
                        + StringUtils.join(crowdsourcingCourierIds, ","));
            }
        }

        // 排除未打卡的众包快递员
        if (CollectionUtils.isNotEmpty(crowdsourcingCourierIds)) {
            Iterator<Integer> iter = crowdsourcingCourierIds.iterator();
            while (iter.hasNext()) {
                Integer courierId = iter.next();
                if (!attendanceService.isOnDuty(courierId)) {
                    iter.remove();
                }
            }
        }
        logger.info("订单(orderId:" + order.getId() + ")" + ",可以配送订单包括(排除下班的)的众包快递员:" + StringUtils.join(crowdsourcingCourierIds, ","));
        return crowdsourcingCourierIds;
    }

    /**
     * 是否是供应链订单
     *
     * @param orderId
     * @return
     */
    @Override
    public boolean isSupplyChainOrder(Integer orderId) {
        String key = "checkIsSupplyChainOrder_" + orderId;
        Object obj = AliOcs.getObject(key);
        if (obj != null) {
            return (boolean) obj;
        }
        String sql = "select o.id from `order` o where o.id = ? and o.from_type = '" + Constants.SUPPLYCHAIN_ORDER + "'";

        Map<String, Object> orderInfo = findOneForJdbc(sql, orderId);

        obj = !(orderInfo == null || orderInfo.isEmpty());
        AliOcs.set(key, obj, 24 * 60 * 60);
        return (boolean) obj;
    }

    @Override
    public int matchSupplyOrderId(Integer orderId) {
        String sql = "select supply_chain_order_id from supply_chain_order_info where order_id = ?";
        Integer supplyOrderId = findOneForJdbc(sql, Integer.class, orderId);
        return supplyOrderId == null ? 0 : supplyOrderId.intValue();
    }

    @Override
    public AjaxJson supplyOrderDistribution(int courierId, int orderId, boolean isSupplyOrder) {
        if (isSupplyOrder) {
            orderId = matchOrderId(courierId, orderId);
        }
        if (orderId <= 0) {
            return failMessage("错误的订单号");
        }
        String sql = "select ifnull(order_type, 1) orderType , supply_chain_order_id from supply_chain_order_info where order_id = ?";
        Map<String, Object> supplyOrderType = findOneForJdbc(sql, orderId);

        if (supplyOrderType == null || supplyOrderType.isEmpty()) {
            return failMessage("订单不存在");
        }
        logger.info("-------------------------- 订单配送 {}, {}, {} --------------------------",
                supplyOrderType.get("orderType").toString(), supplyOrderType.get("supply_chain_order_id").toString(), orderId);
        String url = SupplyChainNotifyUtils.getNotifyUrl(Integer.parseInt(supplyOrderType.get("orderType").toString()), Constants.DELIVERYING);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courierId", courierId);
        params.put("supplyOrderId", supplyOrderType.get("supply_chain_order_id").toString());

        HttpProxy proxy = HttpProxy.createInstance(url, null, params);
        try {
            String response = proxy.doGet();
            JSONObject parameters = JSONObject.fromObject(response);
            int code = parameters.optInt("code", -1);
            String msg = parameters.optString("msg", "");
            logger.info("-------------------------- 订单配送结果 {}, {}, {} --------------------------", orderId, code, msg);
            if (code != 0 && code != 10010) {
                return failMessage(msg);
            }
            if (!deliveryBegin(orderId, courierId)) {
                return failMessage(msg);
            }
            return successMessage("配送成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return failMessage("配送失败！");
    }

    private AjaxJson failMessage(String message) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(false);
        ajaxJson.setStateCode("01");
        ajaxJson.setMsg(message);

        Map<String, Object> stateMap = new HashMap<String, Object>();
        stateMap.put("state", "");
        ajaxJson.setObj(stateMap);
        return ajaxJson;
    }

    private AjaxJson successMessage(String message) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setStateCode("00");
        ajaxJson.setMsg(message);

        Map<String, Object> stateMap = new HashMap<String, Object>();
        stateMap.put("state", "");
        ajaxJson.setObj(stateMap);

        return ajaxJson;
    }

    /**
     * 是否是供应链订单
     *
     * @param orderId
     * @return
     */
    public boolean isSupplyChainOrder(OrderEntity order) {
        return StringUtils.equals(Constants.SUPPLYCHAIN_ORDER, order.getFromType());
    }

    /**
     * 构建订单最基本的信息
     *
     * @param orderId
     * @return
     */
    private OrderEntity buildBasicOrderInfo(Integer orderId) {
        OrderEntity order = new OrderEntity();
        if (isSupplyChainOrder(orderId)) {
            SupplyChainOrderInfoEntity supplyChainOrderInfo = null;
            try {
                order.setFromType(Constants.SUPPLYCHAIN_ORDER);
                supplyChainOrderInfo = findUniqueByProperty(SupplyChainOrderInfoEntity.class, "orderId", orderId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (supplyChainOrderInfo == null) {
                logger.warn("无法找到供应链订单 orderId:{}对应的其他信息。。。", orderId);
                throw new RuntimeException("无法找到供应链订单信息");
            }
            order.setId(orderId);
            MerchantEntity merchant = new MerchantEntity();
            merchant.setId(supplyChainOrderInfo.getDestId());
            if (supplyChainOrderInfo.getDestLon() != null) {
                merchant.setLng(supplyChainOrderInfo.getDestLon());
            }
            if (supplyChainOrderInfo.getDestLat() != null) {
                merchant.setLat(supplyChainOrderInfo.getDestLat());
            }
            order.setMerchant(merchant);
        } else {
            String sql = "select o.merchant_id, o.from_type, m.longitude lng, m.latitude lat from `order` o left join merchant m on o.merchant_id=m.id where o.id=?";
            Map<String, Object> orderInfo = findOneForJdbc(sql, orderId);
            if (orderInfo == null) {
                logger.warn("order is null. orderId: {}", orderId);
                throw new RuntimeException("order can't find yet");
            }

            if (orderInfo.get("from_type") == null) {
                logger.warn("订单{}对应的商家ID为空", orderId);
                throw new RuntimeException("can not find order's merchant!");
            }

            order.setId(orderId);
            order.setFromType(orderInfo.get("from_type").toString());
            MerchantEntity merchant = new MerchantEntity();
            merchant.setId(Integer.parseInt(orderInfo.get("merchant_id").toString()));
            if (orderInfo.get("lng") != null) {
                merchant.setLng(Double.parseDouble(orderInfo.get("lng").toString()));
            }
            if (orderInfo.get("lat") != null) {
                merchant.setLat(Double.parseDouble(orderInfo.get("lat").toString()));
            }
            order.setMerchant(merchant);
        }
        return order;
    }

    private List<Integer> findCanAcceptOrderUsers(Integer orderId) {
        // 获取可以抢该订单的平台快递员、代理商快递员
        List<Integer> courierIds = new ArrayList<Integer>();
        if (isSupplyChainOrder(orderId)) {
            String sql = "select count(*) from 0085_pushed_order where order_id = ?";
            Integer c = findOneForJdbc(sql, Integer.class, orderId);
            if (c != null && c.intValue() > 0) {
                // 表示已经存在推送记录 本次不再推送
                return courierIds;
            }
            // 此处为查找供应链订单配送的快递员
            SupplyChainOrderInfoEntity supplyChainOrderInfo = findUniqueByProperty(SupplyChainOrderInfoEntity.class, "orderId", orderId);
            if (supplyChainOrderInfo != null) {
                // 供应链商家订单时 寻找商家绑定的快递员 否则寻找司机
                courierIds = findSupplyChainCourier(orderId, supplyChainOrderInfo.getSrcId(), (supplyChainOrderInfo.getOrderType() == Constants.SUPPLYCHAIN_MERCHANT_ORDER ? 5 : 4));
            }
        } else {
            List<Map<String, Object>> courierList = findCanPushCourier(orderId, false);
            if (CollectionUtils.isNotEmpty(courierList)) {
                for (Map<String, Object> courierIdMap : courierList) {
                    courierIds.add(Integer.parseInt(courierIdMap.get("courier_id").toString()));
                }
            }
        }
        return courierIds;
    }


    /**
     * 推送订单给快递员
     *
     * @param orderId
     */
    private void push2Courier(Integer orderId) {
        OrderEntity order = buildBasicOrderInfo(orderId);
        // 非众包类型订单
        if (!StringUtils.equalsIgnoreCase(Constants.CROWDSOURCING_ORDER, order.getFromType())) {
            // 找到可以接单的快递员
            List<Integer> courierIds = findCanAcceptOrderUsers(orderId);

            logger.info("full-time courierIds:" + StringUtils.join(courierIds, ","));
            List<Integer> crowdsourcingCourierIds = new ArrayList<Integer>();
            try {
                // 获取可以抢该订单的众包快递员
                crowdsourcingCourierIds = findCanReceiveCrowdsourcingCouriers(order);
            } catch (Exception e) {
                e.printStackTrace();
            }

            logger.info("crowdsourcingCourierIds:" + StringUtils.join(crowdsourcingCourierIds, ","));
            if (CollectionUtils.isNotEmpty(crowdsourcingCourierIds)) {
                courierIds.addAll(crowdsourcingCourierIds);
            }

            if (courierIds.size() > 0) {
                this.pushBatchCourier(orderId, courierIds);
            } else {
                // 商家未绑定配送员，走传统方式，没有配送员送餐
                logger.warn("商家未绑定配送员或已推送，请联系商家确认! orderId:" + orderId);
            }
        } else { // 众包订单只推送给众包快递员
            List<Integer> courierIds = findCanReceiveCrowdsourcingCouriers(order);
            this.pushBatchCourier(order.getId(), courierIds);
        }
    }

    /**
     * 群推“我的可抢单订单数”
     *
     * @param orderId
     * @param courierList
     */
    private void pushBatchCourier(Integer orderId, List<Integer> courierList) {
        logger.info("快递员:{}匹配该订单{}", StringUtils.join(courierList, ","), orderId);
        logger.info("isUsingNewAlgorithm:{}", isUsingNewAlgorithm);
        if (isUsingNewAlgorithm) {
            scambleAlgorithmService.pushToCouriers(orderId, courierList);
        } else {
            for (Integer courierId : courierList) {
                Integer canScramble = pushedOrderService.canScrambleNum(courierId, false).intValue();
                canScramble++; // 加上当前订单
                pushCanScramble(courierId, orderId, canScramble);
            }
        }
    }


    /**
     * 推送“我的可抢单订单数”
     *
     * @param courierId
     */
    public void pushCanScramble(Integer courierId, Integer orderId, Integer canScramble) {
        //推送消息给快递员
        jpushService.pushNewOrderToCourier(orderId, courierId, canScramble);

        List<PushedOrderEntity> pushedOrders = pushedOrderService.findHql(
                "from PushedOrderEntity where orderId=? and pushedCourier=?", new Object[]{orderId, courierId});
        if (pushedOrders == null || pushedOrders.size() == 0) {
            PushedOrderEntity pushedOrder = new PushedOrderEntity(orderId, courierId);
            pushedOrderService.saveOrUpdate(pushedOrder);
        } else {

            String key = "courier_order_" + orderId;
            IDistributedLock lock = new MemcachedDistributedLock();
            String uuid = null;

            try {
                uuid = lock.tryAcquireLock(key, 2 * 60, 60);
                if (uuid == null) {
                    logger.info("更新推送订单时间失败...");
                    return;
                } else {
                    PushedOrderEntity pushedOrder = pushedOrders.get(0);
                    pushedOrder.setLatestUpdateTime(DateUtils.getSeconds());
                    pushedOrderService.saveOrUpdate(pushedOrder);
                }
            } finally {
                if (uuid != null) {
                    lock.releaseLock(key, uuid);
                }
            }
        }
    }

    /**
     * 查询可抢新订单的快递员，同时匹配商家、用户地址，上班状态才允许接单
     *
     * @param orderId       订单ID
     * @param matchUserAddr 是否匹配用户下单地址
     * @return
     */
    private List<Map<String, Object>> findCanPushCourier(Integer orderId, Boolean matchUserAddr) {
        String sql = "";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (matchUserAddr) {
            sql = " select cm.courier_id from 0085_courier_merchant cm ";
            sql += " left join `order` o on o.merchant_id=cm.merchant_id ";
            sql += " left join `user` u on u.id=cm.courier_id ";
            sql += " where o.id=? ";
            // 2015-9-30 新增需求：快递员余额够支付代付订单
            // sql +=
            // " and u.money>=(case when (o.order_type='mobile' and
            // o.pay_state='unpay') then (o.origin+o.delivery_fee) else 0 end)
            // ";
            sql += " and exists( ";
            sql += " select cb.courier_id from 0085_courier_building cb ";
            sql += " left join 0085_building b on b.id=cb.building_id ";
            sql += " left join address addr on addr.building_id=b.id ";
            sql += " left join `order` o on o.user_address_id=addr.id ";
            sql += " where o.id=? and cb.courier_id=cm.courier_id) ";
            list = this.findForJdbc(sql, new Object[]{orderId, orderId});
        } else {
            sql = "select cm.courier_id from 0085_courier_merchant cm ";
            sql += " left join `order` o on o.merchant_id=cm.merchant_id ";
            sql += " left join `user` u on u.id=cm.courier_id ";
            sql += " where o.id=? ";
            // 2015-9-30 新增需求：快递员余额够支付代付订单
            // sql +=
            // " and u.money>=(case when (o.order_type='mobile' and
            // o.pay_state='unpay') then (o.origin+o.delivery_fee) else 0 end)
            // ";
            list = this.findForJdbc(sql, new Object[]{orderId});
        }
        if (list != null && list.size() > 0) {
            Iterator<Map<String, Object>> it = list.iterator();
            while (it.hasNext()) {
                Map<String, Object> map = it.next();
                Integer courierId = Integer.parseInt(map.get("courier_id").toString());
                if (!attendanceService.isOnDuty(courierId)) {
                    PushedOrderEntity pushedOrder = new PushedOrderEntity(orderId, courierId);
                    pushedOrderService.save(pushedOrder);
                    it.remove();
                }
            }
        }
        return list;
    }

    private List<Integer> findSupplyChainCourier(Integer orderId, Integer bindUserId, int supplychainOrderType) {
        // 找到该订单对应的商家ID
        String sql = "select driver_id from 0085_driver_warehouse where warehouse_id = ?";

        List<Map<String, Object>> couriers = findForJdbc(sql, bindUserId);
        if (couriers == null || couriers.isEmpty()) {
            return Collections.emptyList();
        }
        Iterator<Map<String, Object>> iter = couriers.iterator();

        logger.info("查找供应链快递员，参数：order id " + orderId + "， supply chain order type " + supplychainOrderType + "， couriers " + couriers);

        List<Integer> courierIds = new ArrayList<>();
        if (couriers != null && couriers.size() > 0) {
            iter = couriers.iterator();
            while (iter.hasNext()) {
                Map<String, Object> columnValue = iter.next();
                Integer courierId = Integer.parseInt(columnValue.get("driver_id").toString());
                if (attendanceService.isOnDuty(courierId)) {
                    courierIds.add(courierId);
                }
                PushedOrderEntity pushedOrder = new PushedOrderEntity(orderId, courierId);
                pushedOrderService.save(pushedOrder);
            }
        }
        return courierIds;
    }


    public List<Integer> findCanPushDriver(Integer orderId, Integer bindUserId) {
        String sql = "select merchant_id from `order` where id = ?";

        Integer warehouseId = findOneForJdbc(sql, Integer.class, orderId);
        if (warehouseId == null || warehouseId == 0) {
            return Collections.emptyList();
        }
        sql = "select courier_id from 0085_courier_info where courier_id in "
                + "(select courier_id from 0085_driver_warehouse where warehouse_id = ?) and courier_type = 4";
        List<Map<String, Object>> drivers = findForJdbc(sql, warehouseId);

        List<Integer> driverIds = new ArrayList<>();
        if (drivers != null && drivers.size() > 0) {
            Iterator<Map<String, Object>> iter = drivers.iterator();
            while (iter.hasNext()) {
                Map<String, Object> columnValue = iter.next();
                Integer courierId = Integer.parseInt(columnValue.get("courier_id").toString());
                if (attendanceService.isOnDuty(courierId)) {
                    driverIds.add(courierId);
                    continue;
                }
                PushedOrderEntity pushedOrder = new PushedOrderEntity(orderId, courierId);
                pushedOrderService.save(pushedOrder);
                iter.remove();
            }
        }
        return driverIds;
    }

    @Override
    public boolean merchantNoAcceptOrder(int orderId, int merchantid, String refundReason) throws Exception {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (merchantid != order.getMerchant().getId()) {
            return false;
        }
        order.setState("unaccept");
        order.setAccessTime(DateUtils.getSeconds());
        this.updateEntitie(order);
        this.refund(orderId, refundReason);
        orderStateService.noAcceptOrderState(orderId);
        return true;
    }

    private void refund(int orderId, String refundReason) throws Exception {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        order.setRstate("berefund");
        order.setState("confirm");
        this.updateEntitie(order);

        WUserEntity wuser = order.getWuser();
        /**
         * 所有退款操作积分不返回,需求来源李莹
         */
        // wuser.setScore(wuser.getScore() + order.getScore() -
        // order.getOrigin().intValue());
        // this.updateEntitie(wuser);
        if (order.getCardId() != null && !"0".equals(order.getCardId()) && StringUtils.isNotBlank(order.getCardId())) {
            String sql = "update card set consume='N',order_id=? where user_id=? and order_id=?";
            this.executeSql(sql, 0, order.getWuser().getId(), orderId);
        }

        // 在线支付的金额不再退到余额
        if (order.getCredit() > 0) {
            Double refundMoney = order.getCredit();
            //闪购订单退款金额从退款信息表里面取
            if ("flashsales".equals(order.getOrderType())) {
                FlashOrderReturnEntity flash = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", order.getId());
                if (flash != null) {
                    refundMoney = flash.getRefundAmount();
                } else {
                    logger.error("未找到flashOrderId:[" + order.getFlashOrderId() + "]的闪购退款信息记录！");
                }
            }
            flowService.refundFlowCreate(wuser.getId(), refundMoney, orderId);
        }

        //恢复库存
        menuService.revertRepertory(orderId);
    }

    @Override
    public void askRefund(int orderId, String refundReason) {
        logger.info("用户申请订单取消开始");
        OrderEntity order = this.get(OrderEntity.class, orderId);
        order.setRetime(DateUtils.getSeconds());
        order.setRstate("askrefund");
        order.setRereason(refundReason);
        this.saveOrUpdate(order);

        orderStateService.askedRefundOrderState(orderId);

        if (order != null) {
            Integer merchantUserId = order.getMerchant().getWuser().getId();
            Map<String, String> pushMap = new HashMap<String, String>();
            pushMap.put("appType", AppTypeConstants.APP_TYPE_MERCHANT);
            pushMap.put("orderId", ((Integer) orderId).toString());
            String title = "您有一条订单取消信息";
            pushMap.put("title", title);
            pushMap.put("content", title);
            pushMap.put("voiceFile", SoundFile.SOUND_NEW_REFUND);

            jpushService.push(merchantUserId, pushMap);
        }
    }

    @Override
    public boolean acceptRefund(int orderId, int merchantId) throws Exception {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        boolean refundStatus = false;
        if (order != null) {
            if ("askrefund".equals(order.getRstate()) || "acceptRefundApply".equals(order.getRstate())) {
                OrderRefundEntity orderRefundEntity = this.saveOrderRefund(order, merchantId);
                if (orderRefundEntity != null) {
                    if (orderRefundEntity.getStatus().equals("Y")) {
                        this.refund(orderId, order.getRereason());

                        // 通知快递员推单模块
                        refundNoticeToCourier(orderId);

                        orderStateService.refundSuccessOrderState(orderId);
                        // 修改促销库存和促销销量
                        menuService.subtractMenuPromotion(orderId);
                        refundStatus = true;
                    }
                }
            }
        }
        return refundStatus;
    }

    @Override
    public OrderRefundEntity acceptThirdRefund(int orderId, int merchantId) throws Exception {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        OrderRefundEntity orderRefundEntity = null;
        if (order != null) {
            if (order.getRstate().equals("askrefund")) {
                orderRefundEntity = this.saveOrderRefund(order, merchantId);
                if (orderRefundEntity != null) {
                    if (orderRefundEntity.getStatus().equals("Y")) {
                        this.refund(orderId, order.getRereason());

                        // 通知快递员推单模块
                        refundNoticeToCourier(orderId);

                        orderStateService.refundSuccessOrderState(orderId);
                    }
                }
            }
        }
        return orderRefundEntity;
    }

    /**
     * 保存退款记录
     *
     * @param order
     * @param merchantId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    private OrderRefundEntity saveOrderRefund(OrderEntity order, int merchantId) throws Exception {
        OrderRefundEntity orderRefund = new OrderRefundEntity();
        String outRefundNo = String.valueOf(System.currentTimeMillis());
        double onlineMoney = Math.rint(order.getOnlineMoney() * 100);
        int totalFee = (int) onlineMoney;
        String refundFee = String.valueOf(totalFee);
        //闪购订单退款金额从退款信息表里面取
        if ("flashsales".equals(order.getOrderType())) {
            FlashOrderReturnEntity flash = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", order.getId());
            if (flash != null) {
                refundFee = String.valueOf(flash.getRefundAmount() * 100);
            } else {
                logger.error("未找到flashOrderId:[" + order.getFlashOrderId() + "]的闪购退款信息记录！");
            }
        }
        if (order.getPayType().equals(PayEnum.weixinpay.getEn())) {
            WeChatRefundVo weChatRefundVo = wxPayService.weChatRefund(order.getOutTraceId(), order.getPayId(),
                    outRefundNo, String.valueOf(totalFee), refundFee);
            if (weChatRefundVo != null) {
                orderRefund.setOutRefundNo(weChatRefundVo.getOut_refund_no());
                orderRefund.setOutTradeNo(weChatRefundVo.getOut_trade_no());
                orderRefund.setPayType(order.getPayType());
                orderRefund.setRefundFee(weChatRefundVo.getRefund_fee());
                orderRefund.setTotalFee(weChatRefundVo.getTotal_fee());
                orderRefund.setTransactionId(weChatRefundVo.getTransaction_id());
                orderRefund.setOpUserId(merchantId);
                orderRefund.setCreateTime(DateUtils.getSeconds());
                // 必须return_code和result_code都为SUCCESS
                if (WeChatRefundVo.RETURN_CODE_SUCCESS.equals(weChatRefundVo.getReturn_code())
                        && WeChatRefundVo.RESULT_CODE_SUCCESS.equals(weChatRefundVo.getResult_code())) {
                    orderRefund.setStatus("Y");
                } else {
                    orderRefund.setStatus("N");
                }
            }
        } else if (order.getPayType().equals(PayEnum.balance.getEn())
                || order.getPayType().equals(PayEnum.merchantpay.getEn())) {
            Double credit = order.getCredit() * 100;//实付金额
            int refundCredit = credit.intValue();//退款金额
            //闪购订单退款金额从退款信息表里面取
            if ("flashsales".equals(order.getOrderType())) {
                FlashOrderReturnEntity flash = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", order.getId());
                if (flash != null) {
                    Double refundFlash = flash.getRefundAmount() * 100;
                    refundCredit = refundFlash.intValue();
                } else {
                    logger.error("未找到flashOrderId:[" + order.getFlashOrderId() + "]的闪购退款信息记录！");
                }
            }
            String transactionId = String.valueOf(System.currentTimeMillis());
            orderRefund.setOutRefundNo(outRefundNo);
            orderRefund.setOutTradeNo(order.getPayId());
            orderRefund.setPayType(order.getPayType());
            orderRefund.setRefundFee(refundCredit);
            orderRefund.setTotalFee(credit.intValue());
            orderRefund.setTransactionId(transactionId);
            orderRefund.setOpUserId(merchantId);
            orderRefund.setCreateTime(DateUtils.getSeconds());
            orderRefund.setStatus("Y");
        } else if (PayEnum.wft_pay.getEn().equals(order.getPayType())) {
            orderRefund = wftRefund(order, merchantId);
        } else if (PayEnum.alipay.getEn().equals(order.getPayType())) {
            orderRefund = fastpayRefund(order, merchantId);
        } else {
            // TODO 其他支付方式的退款，暂时先退到账户余额
            logger.warn("订单【" + order.getId() + "】退款，其他方式：" + order.getPayType());
            Double credit = order.getCredit() * 100;
            int totalCredit = credit.intValue();
            int refundCredit = totalCredit;
            if ("flashsales".equals(order.getOrderType())) {
                FlashOrderReturnEntity flash = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", order.getId());
                if (flash != null) {
                    Double refundFlash = flash.getRefundAmount() * 100;
                    refundCredit = refundFlash.intValue();
                } else {
                    logger.error("未找到flashOrderId:[" + order.getFlashOrderId() + "]的闪购退款信息记录！");
                }
            }
            String transactionId = String.valueOf(System.currentTimeMillis());
            orderRefund.setOutRefundNo(outRefundNo);
            orderRefund.setOutTradeNo(order.getPayId());
            orderRefund.setPayType(order.getPayType());
            orderRefund.setRefundFee(refundCredit);
            orderRefund.setTotalFee(totalCredit);
            orderRefund.setTransactionId(transactionId);
            orderRefund.setOpUserId(merchantId);
            orderRefund.setCreateTime(DateUtils.getSeconds());
            orderRefund.setStatus("Y");
            flowService.refundFlowCreate(order.getWuser().getId(), order.getOnlineMoney(), order.getId());
        }
        orderRefundService.save(orderRefund);
        return orderRefund;
    }

    /**
     * 请求威富通退款并生成订单退款bean
     *
     * @param order      订单对象
     * @param merchantId 商家id
     * @return 订单退款bean
     * @throws IOException
     */
    private OrderRefundEntity wftRefund(OrderEntity order, int merchantId) throws IOException {
        String outRefundNo = String.valueOf(System.currentTimeMillis());
        int totalFee = BigDecimal.valueOf(order.getOnlineMoney()).multiply(new BigDecimal(100)).intValue();//实付金额

        Integer refundFee = totalFee;//退款金额
        String payId = order.getPayId();
        String transactionId = order.getOutTraceId();

        //闪购订单退款金额从退款信息表里面取
        if ("flashsales".equals(order.getOrderType())) {
            FlashOrderReturnEntity flash = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", order.getId());
            if (flash != null) {
                Double rfee = flash.getRefundAmount() * 100;
                refundFee = rfee.intValue();
                transactionId = "0";
                
                /*
                 * 闪购订单的第三方支付会产生一个支付订单，退款时取支付订单的pay_id
                 */
                if (StringUtils.isNotEmpty(order.getOutTraceId())) {
                    OrderEntity payOrder = this.get(OrderEntity.class, Integer.valueOf(order.getOutTraceId()));
                    if (payOrder != null) {
                        payId = payOrder.getPayId();
                        transactionId = payOrder.getOutTraceId();
                    }
                }
            } else {
                logger.error("未找到flashOrderId:[" + order.getFlashOrderId() + "]的闪购退款信息记录！");
            }
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("refundNo", outRefundNo));
        params.add(new BasicNameValuePair("tradeNo", payId));
        params.add(new BasicNameValuePair("refundChannel", WftRefundRequest.REFUND_CHANNEL_ORIGINAL));
        params.add(new BasicNameValuePair("totalFee", String.valueOf(totalFee)));
        params.add(new BasicNameValuePair("refundFee", refundFee.toString()));
        params.add(new BasicNameValuePair("transactionId", transactionId));
        logger.info(
                "It's going to request wftRefund. refundNo:{}, tradeNo:{}, refundChannel:{}, totalFee:{}, refundFee:{}, transactionId:{}",
                outRefundNo, order.getPayId(), WftRefundRequest.REFUND_CHANNEL_ORIGINAL, totalFee, refundFee,
                order.getOutTraceId());

        HttpPost httpPost = new HttpPost(ConfigUtil.PAY_REFUND_URL_WFT);
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        WftRefundResponse wftRefundResponse = HttpClientUtil.execute(httpPost, WftRefundResponse.class);

        OrderRefundEntity orderRefund = new OrderRefundEntity();
        orderRefund.setOutTradeNo(payId);
        orderRefund.setOutRefundNo(outRefundNo);
        orderRefund.setPayType(order.getPayType());
        orderRefund.setTransactionId(transactionId);
        orderRefund.setTotalFee(totalFee);
        orderRefund.setRefundFee(Integer.valueOf(refundFee));
        orderRefund.setOpUserId(merchantId);
        orderRefund.setCreateTime(DateUtils.getSeconds());
        if (WftRefundResponse.RESULT_CODE_SUCCESS.equals(wftRefundResponse.getResultCode())) {
            orderRefund.setStatus("Y");
        } else {
            orderRefund.setStatus("N");
            logger.info("order refund fail, id: {}", order.getId());
        }
        return orderRefund;
    }

    /**
     * 请求即时到账批量退款无密接口并生成订单退款记录对象
     *
     * @param order      订单对象
     * @param merchantId 商家id
     * @return 订单退款记录对象
     */
    private OrderRefundEntity fastpayRefund(OrderEntity order, int merchantId) {
        logger.info("fastpay refund for order: {}, out_trace_id: {}", order.getId(), order.getOutTraceId());
        FastpayRefundParam fastpayRefundParam = new FastpayRefundParam(order.getOutTraceId(), BigDecimal.valueOf(order.getOnlineMoney()));
        NopwdFastpayRefundApplyResult result = nopwdFastpayRefundAction.apply(fastpayRefundParam);

        int totalFee = BigDecimal.valueOf(order.getOnlineMoney()).multiply(new BigDecimal(100)).intValue();
        OrderRefundEntity orderRefund = new OrderRefundEntity();
        orderRefund.setOutTradeNo(order.getPayId());
        orderRefund.setOutRefundNo(result.getBatchNo());
        orderRefund.setPayType(order.getPayType());
        orderRefund.setTransactionId(order.getOutTraceId());
        orderRefund.setTotalFee(totalFee);
        orderRefund.setRefundFee(totalFee);
        orderRefund.setOpUserId(merchantId);
        orderRefund.setCreateTime(DateUtils.getSeconds());
        orderRefund.setStatus(result.isSuccess() ? "Y" : "N");

        return orderRefund;
    }

    @Override
    public void unacceptRefund(int orderId) {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        order.setRstate("norefund");
        this.saveOrUpdate(order);
        orderStateService.refundFailedOrderState(orderId);
    }

    @Override
    public boolean deliveryBegin(Integer orderId, Integer courierId) {
        orderDao.setOrderDeliveryState(orderId);

        orderStateService.deliveryOrderState(orderId);
        // 不是供应链订单推送消息
        if (!isSupplyChainOrder(orderId)) {
            OrderEntity order = this.get(OrderEntity.class, orderId);
            messageService.sendMessage(order, "delivery"); // 发送开始配送公众号模版信息
        } else {
            supplyOrderService.noticeSupplyChain(courierId, orderId, Constants.DELIVERYING);
        }
        // 推送“我的配送中订单数”
        List<Map<String, Object>> deliveryList = this.countByStatus(courierId, "delivery");
        if (!CollectionUtils.isEmpty(deliveryList)) {
            Map<String, Object> deliveryMap = deliveryList.get(0);
            Map<String, String> pushMap = new HashMap<String, String>();
            pushMap.put("orderId", orderId.toString());
            pushMap.put("delivery", deliveryMap.get("c").toString());
            String title = "您有一条新的配送中订单";
            pushMap.put("title", title);
            pushMap.put("content", title);
            pushMap.put("voiceFile", "");
            jpushService.push(courierId, pushMap);
        } else {
            logger.warn("courierId:{} deliveryList is empty !!!", courierId);
        }
        return true;
    }

    @Override
    public boolean confirmOrder(Integer orderId, Integer userId) throws Exception {
        OrderEntity order = this.get(OrderEntity.class, orderId);

        if (null != order && order.getWuser().getId().equals(userId)) {

            String confirmState = OrderStateEnum.CONFIRM.getOrderStateEn();
            String orderPreSaveState = order.getState();
            // 状态为已经确认收货，直接返回true
            if (confirmState.equals(orderPreSaveState)) {
                return true;
            }

            String state = order.getPayState();
            // 配送中或已支付或商家已接单的订单才能确认收货
            if (OrderStateEnum.UNPAY.getOrderStateEn().equals(state)) {
                return false;
            }

            order.setState(confirmState);
            order.setRstate("normal");
            order.setPayState("pay");
            order.setCompleteTime(DateUtils.getSeconds());
            this.saveOrUpdate(order);

            orderStateService.doneOrderState(orderId);
            orderIncomeService.createOrderIncome(order);

            messageService.sendMessage(order, "done"); // 发送完成配送公众号模版信息

            // 如果是外卖,插入数据到tlm_statistics_realtime(统计表) 主要更新快递员累计送单量和累计送单总时长
            if (order.getSaleType().equals(1)) {
                // 快递员统计
                Integer courierId = order.getCourierId();
                Long completeSeconds = (order.getCompleteTime() - order.getDeliveryTime()) * 1000L;
                Integer minutes = (int) ((completeSeconds % (1000 * 60 * 60)) / (1000 * 60));
                Integer money = ((int) Math.rint(order.getOrigin() * 100)
                        + (int) Math.rint(order.getDeliveryFee() * 100) - (int) Math.rint(order.getScoreMoney() * 100)
                        - (int) Math.rint(order.getCard() * 100));
                tlmStatisticsService.updateTotalOrder(courierId, minutes, money);
                // 商家统计
                Integer merchantMoney = ((int) Math.rint(order.getOrigin().doubleValue() * 100)
                        + (int) Math.rint(order.getDeliveryFee().doubleValue() * 100)
                        - (int) Math.rint(order.getScoreMoney().doubleValue() * 100)
                        - (int) Math.rint(order.getCard().doubleValue() * 100));
                Long merchantCompleteSeconds = (order.getCompleteTime() - order.getAccessTime()) * 1000L;
                Integer merchantMinutes = (int) ((merchantCompleteSeconds % (1000 * 60 * 60)) / (1000 * 60));
//                tpmStatisticsRealtimeService.createOrUpdateTSR(order.getMerchant().getId(), merchantMoney, merchantMinutes);
                tpmStatisticsRealtimeService.updateStat(order.getMerchant().getId(), merchantMoney, merchantMinutes);
            }

            // 推送“我的已完成订单数”
            List<Map<String, Object>> doneList = this.countByStatus(order.getCourierId(), confirmState);
            if (CollectionUtils.isNotEmpty(doneList) && order.getCourierId() != 0) {
                Map<String, Object> doneMap = doneList.get(0);
                Map<String, String> pushMap = new HashMap<String, String>();
                pushMap.put("orderId", orderId.toString());
                pushMap.put("delivery_done", doneMap.get("c").toString());
                String title = "您有一条新的已确认订单";
                pushMap.put("title", title);
                pushMap.put("content", title);
                pushMap.put("voiceFile", "");
                jpushService.push(order.getCourierId(), pushMap);
            } else {
                logger.warn("courierId:{} doneList is empty or CourierId is 0 ", order.getCourierId());
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean isTransferMobileOrder(OrderEntity order) {
        return "mobile".equals(order.getOrderType()) && "db".equalsIgnoreCase(order.getFromType());
    }

    @Override
    public boolean deliveryDone(Integer courierId, Integer orderId) throws Exception {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (order == null || !courierId.equals(order.getCourierId())) {
            return false;
        }
        String state = order.getState();

        // 状态为已经确认收货，直接返回true
        if (OrderStateEnum.CONFIRM.getOrderStateEn().equals(state)) {
            logger.info("orderid:" + orderId + ", 订单状态:" + state);
            return true;
        }
        // 非代付电话订单， 配送中的订单才能确认收货
        if (!this.isTransferMobileOrder(order) && !OrderStateEnum.DELIVERY.getOrderStateEn().equals(state)
                || !OrderStateEnum.PAY.getOrderStateEn().equals(order.getPayState())) {
            logger.info("orderid:" + orderId + ", 订单状态:" + state + ", 支付状态:" + order.getPayState());
            return false;
        }
        order.setState("confirm");
        order.setRstate("normal");
        order.setPayState("pay");
        order.setCompleteTime(DateUtils.getSeconds());
        logger.info("把订单:" + orderId + "置为确认完成状态.");
        this.saveOrUpdate(order);

        orderStateService.deliveryDoneOrderState(orderId);
        orderIncomeService.createOrderIncome(order);
        messageService.sendMessage(order, "done"); // 发送完成配送公众号模版信息

        // 插入数据到tlm_statistics_realtime(统计表) 主要更新快递员累计送单量和累计送单总时长
        Long completeSeconds = (order.getCompleteTime() - order.getDeliveryTime()) * 1000L;
        Integer minutes = (int) ((completeSeconds % (1000 * 60 * 60)) / (1000 * 60));
        Integer money = ((int) Math.rint(order.getOrigin() * 100) + (int) Math.rint(order.getDeliveryFee() * 100)
                - (int) Math.rint(order.getScoreMoney() * 100) - (int) Math.rint(order.getCard() * 100));
        tlmStatisticsService.updateTotalOrder(courierId, minutes, money);
        // 商家统计
        Integer merchantMoney = ((int) Math.rint(order.getOrigin().doubleValue() * 100)
                + (int) Math.rint(order.getDeliveryFee().doubleValue() * 100)
                - (int) Math.rint(order.getScoreMoney().doubleValue() * 100)
                - (int) Math.rint(order.getCard().doubleValue() * 100));
        Long merchantCompleteSeconds = (order.getCompleteTime() - order.getAccessTime()) * 1000L;
        Integer merchantMinutes = (int) ((merchantCompleteSeconds % (1000 * 60 * 60)) / (1000 * 60));
        // tpmStatisticsRealtimeService.createOrUpdateTSR(order.getMerchant().getId(),
        // merchantMoney, merchantMinutes);
        tpmStatisticsRealtimeService.updateStat(order.getMerchant().getId(), merchantMoney, merchantMinutes);

        String source = merchantService.getMerchantSource(order.getMerchant().getId());
        // ===============================第三方Retail超市需求=======================================//
        if (Constants.MERCHANT_SOURCE_RETAIL.equals(source)) {
            logger.info(">>>>>>>>>>>>>进入一号生活的Retail中间件,进行确认完成订单操作【定时任务自动完成：deliveryDone()】,orderId={}", order.getId());
            RetailPortCall.updateOrder(order.getId());
        }
        // ===============================第三方Retail超市需求=======================================//

        // 推送“我的已完成订单数”
        List<Map<String, Object>> doneList = this.countByStatus(courierId, OrderStateEnum.CONFIRM.getOrderStateEn());
        if (CollectionUtils.isNotEmpty(doneList)) {
            Map<String, Object> doneMap = doneList.get(0);
            Map<String, String> pushMap = new HashMap<String, String>();
            pushMap.put("orderId", orderId.toString());
            pushMap.put("delivery_done", doneMap.get("c").toString());
            String title = "您有一条新的已确认订单";
            pushMap.put("title", title);
            pushMap.put("content", title);
            pushMap.put("voiceFile", "");
            jpushService.push(courierId, pushMap);
        } else {
            logger.warn("courierId:{} doneList is empty !!!", courierId);
        }
        return true;
    }

    @Override
    public boolean supplyOrderFinish(Integer courierId, Integer supplyOrderId) throws Exception {
        // 查询与供应链订单匹配的平台订单
        int orderId = matchOrderId(courierId, supplyOrderId);
        if (orderId <= 0) {
            return false;
        }
        if (!isSupplyChainOrder(orderId)) {
            return false;
        }
        orderDao.setOrderConfirmState(orderId);
        orderStateService.deliveryOrderState(orderId);

        // 推送“我的已完成订单数”
        List<Map<String, Object>> doneList = this.countByStatus(courierId, OrderStateEnum.CONFIRM.getOrderStateEn());
        if (CollectionUtils.isNotEmpty(doneList)) {
            Map<String, Object> doneMap = doneList.get(0);
            Map<String, String> pushMap = new HashMap<String, String>();
            pushMap.put("orderId", String.valueOf(orderId));
            pushMap.put("delivery_done", doneMap.get("c").toString());
            String title = "您有一条新的已确认订单";
            pushMap.put("title", title);
            pushMap.put("content", title);
            pushMap.put("voiceFile", "");
            jpushService.push(courierId, pushMap);
        } else {
            logger.warn("courierId:{} doneList is empty !!!", courierId);
        }
        return true;
    }

    private int matchOrderId(Integer courierId, Integer supplyOrderId) {
        // 获取与供应链匹配的平台订单ID
        String sql = "select order_id from supply_chain_order_info where supply_chain_order_id = ?";
        List<Map<String, Object>> orderIds = findForJdbc(sql, supplyOrderId);
        if (orderIds == null || orderIds.isEmpty()) {
            return 0;
        }
        for (Map<String, Object> orderId : orderIds) {
            sql = "select id from `order` where id = ? and courier_id = ?";
            Integer id = findOneForJdbc(sql, Integer.class, new Object[]{orderId.get("order_id"), courierId});
            if (id != null && id.intValue() > 0) {
                return id.intValue();
            }
        }
        return 0;
    }


    @Override
    public void mobileOrderStateCreate(int orderId) {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        order.setState("accept");
        order.setAccessTime(DateUtils.getSeconds());
        this.saveOrUpdate(order);

        orderStateService.createPhoneOrderState(orderId);
    }


    /**
     * 获取餐饮系统的座位号、就餐人数和人均餐位费信息
     *
     * @param orderId
     * @return
     */
    public Map<String, Object> getDineSeatDetail(Integer orderId) {
        Map<String, Object> seatmap = this.findOneForJdbc(
                "select ms.seat_name, eo.pnumber, convert(eo.seat_pmoney/100/eo.pnumber,DECIMAL(11,2)) pmoney from "
                        + " eatin_order eo left join merchant_seating ms on eo.seat_id=ms.id where eo.order_id=?",
                orderId);
        return seatmap;
    }


    @Override
    public void autoPrint(OrderEntity order) throws Exception {
        MerchantEntity mer = order.getMerchant();
        String source = merchantService.getMerchantSource(mer.getId());
        boolean isoffline = StringUtils.isNotEmpty(order.getRemark()) && order.getRemark().startsWith("[offline_order]");
        if (OrderEntity.SaleType.DINE.equals(order.getSaleType())) {
            String dineOrderPrint = mer.getDineOrderPrint();
            if ("Y".equals(dineOrderPrint) && !isoffline) {
                // 私厨下单不自动打印订单
                if (!Constants.MERCHANT_SOURCE_PRIVATE.equals(source)) {
                    printService.print(order, false);
                } else {
                    logger.warn("order not print because source is private , merchantId={}", mer.getId());
                }
            } else {
                logger.warn("order not print dineOrderPrint is N , merchantId={}", mer.getId());
            }
        } else {
            boolean isMerchantDelivery = isMerchantDelivery(mer);
            if (isMerchantDelivery) {
                tomOrderTimerServiceI.createOrUpdate(order.getId(), order.getMerchant().getId(), order.getCreateTime());
            } else {
                if (!Constants.MERCHANT_SOURCE_PRIVATE.equals(source) && !isoffline) {
                    printService.print(order, false);
                } else {
                    logger.warn("order not print because source is private , merchantId={}", mer.getId());
                }
            }
        }
    }

    @Override
    public void setOrderNum(OrderEntity order) {
        // 判断订单是否已经有排号，无排号的生成排号
        logger.info("---------第三方接口调用生成订单排号");
        if (order.getOrderNum() == null || "".equals(order.getOrderNum())) {
            order.setOrderNum(AliOcs.genOrderNum(order.getMerchant().getId().toString()));// 生成排号
            this.saveOrUpdate(order);
        }
    }

    @Override
    public boolean verifyMenuRepertoryquantity(int orderId) {

        String sql = "select m.menu_id,m.quantity from order_menu m where m.order_id=?";
        List<Map<String, Object>> list = this.findForJdbc(sql, orderId);
        boolean flag = true;
        if (list == null || list.size() == 0) {
            flag = false;
        } else {

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                Integer menuId = Integer.parseInt(map.get("menu_id").toString());
                Integer quantity = Integer.parseInt(map.get("quantity").toString());
                MenuEntity menu = this.get(MenuEntity.class, menuId);
                int number = menu.getTodayRepertory();// 验证今日库存是否足够
                if (number < quantity) {
                    flag = false;
                    return flag;
                }
            }
        }
        return flag;
    }

    @Override
    public AjaxJson newestOrderDetails(int userId) {
        AjaxJson j = new AjaxJson();
        String sql = "select id from `order` o " + "where  o.order_type in ('normal','mobile') "
                + "and o.state in('accept','delivery')" + " and o.user_id=? "
                + "and   FROM_UNIXTIME(o.create_time, '%Y%m%d' )  = FROM_UNIXTIME(UNIX_TIMESTAMP(),'%Y%m%d' )  "
                + "ORDER BY o.id desc limit 0,1";
        List<Map<String, Object>> orderList = findForJdbc(sql, userId);
        if (orderList == null || orderList.size() == 0) {
            j.setMsg("订单不存在");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;
        }

        Map<String, Object> map = orderList.get(0);
        Integer orderId = Integer.parseInt(map.get("id").toString());
        OrderEntity order = get(OrderEntity.class, orderId);
        if (order == null) {
            j.setMsg("订单不存在");
            j.setStateCode("01");
            j.setSuccess(false);
            return j;

        } else {
            Map<String, Object> objs = new HashMap<String, Object>();
            MerchantEntity merchan = order.getMerchant();
            String state = order.getState();
            int time = DateUtils.getSeconds();
            int courierId = order.getCourierId();
            if ("delivery".equals(state)) {
                WUserEntity courierUser = wUserService.get(WUserEntity.class, courierId);
                if (courierUser == null) {
                    j.setMsg("快递员不存在");
                    j.setStateCode("01");
                    j.setSuccess(false);
                }

                String photoUrl = courierUser.getPhotoUrl();
                objs.put("userId", courierUser.getId());// 快递员ID
                objs.put("userName", courierUser.getUsername());// 快递员姓名
                objs.put("photoUrl", systemconfigService.getValByCode("img_url") + photoUrl);// 快递员头像
                objs.put("courierUserMobile", courierUser.getMobile());// 快递员手机
                objs.put("courierRandingList", getCourierRanking(courierUser.getId()));//

                sql = "select total_comment count, total_comment_score/total_comment comment_courier from 0085_courier_statistics_realtime where courier_id=?";
                Map<String, Object> courierCountMap = this.findOneForJdbc(sql, courierUser.getId());

                objs.put("orderCount", courierCountMap.get("count"));
                objs.put("commentCourier", courierCountMap.get("comment_courier"));
            }

            Integer accessTime = order.getAccessTime();
            int Countdown = accessTime + 45 * 60 - time;

            int payTimeCount = time - order.getPayTime();
            int acceptTimeCount = time - order.getAccessTime();
            int deliveryTimeCount = time - order.getDeliveryTime();

            objs.put("payTimeCount", payTimeCount);// 支付时间
            objs.put("acceptTimeCount", acceptTimeCount);// 制作时间
            objs.put("deliveryTimeCount", deliveryTimeCount);// 配送时间
            objs.put("systemDate", DateUtils.getSeconds());// 当前系统秒数

            // 查询获取商家评论总分
            sql = "select total_comment comment_count, total_comment_score/total_comment comment_score from 0085_merchant_statistics_realtime where merchant_id=?";
            List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

            totalList = this.findForJdbc(sql, merchan.getId());
            objs.put("totalList", totalList);// 商家评论总分

            objs.put("acceptTime", Countdown);// 制作时间，返回秒
            objs.put("rstate", order.getRstate());
            objs.put("merchanId", merchan.getId());// 商家ID
            objs.put("title", merchan.getTitle());// 商家标题
            objs.put("address", merchan.getAddress());// 商家地址
            objs.put("merchanMobile", merchan.getMobile());// 商家手机
            objs.put("logoUrl", systemconfigService.getValByCode("img_url") + merchan.getLogoUrl());// 商家图片
            SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            long unixLong = 0;
            String date = "";
            try {
                unixLong = (long) order.getCreateTime();
                date = fm.format(unixLong);
            } catch (Exception e) {
                e.printStackTrace();
            }

            objs.put("orderId", order.getId());// 订单ID
            objs.put("payState", order.getPayState());// 订单支付状态
            objs.put("createTime", date);// 订单创建时间
            objs.put("state", order.getState());// 订单状态
            objs.put("timeRemark", order.getTimeRemark());
            objs.put("order_num", ((StringUtils.isEmpty(order.getOrderNum()) || order.getOrderNum().length() < 8) ? ""
                    : order.getOrderNum().toString().substring(8)));// 订单排号
            objs.put("sale_type", order.getSaleType());
            objs.put("payId", order.getPayId());
            objs.put("orderType", order.getOrderType());
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            list.add(objs);
            j.setSuccess(true);
            j.setObj(list);
            j.setMsg("操作成功");
            j.setStateCode("00");

        }

        return j;
    }

    @Override
    public List<Map<String, Object>> selectWaitDeliveryOrder(int page, int rows) {
        String sql = "select o.id,o.courier_id,o.city_id,o.state,o.address,"
                + "o.merchant_id,o.mobile,o.title,o.order_num,o.order_type,o.sale_type"
                + "o.realname,o.access_time,o.delivery_time,o.time_remark from `order` o " + "where o.pay_state='pay'  "
                + "and o.order_type in('mobile','normal') " + "and o.state='accept' "
                + "and  FROM_UNIXTIME(o.create_time, '%Y%m%d' )  = FROM_UNIXTIME(UNIX_TIMESTAMP(),'%Y%m%d' )";
        List<Map<String, Object>> list = findForJdbcParam(sql, page, rows);
        List<Map<String, Object>> orderlist = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, Object> map = list.get(i);
            int oId = Integer.parseInt(map.get("id").toString());// 订单ID
            int merchantId = Integer.parseInt(map.get("merchant_id").toString());// 商家ID
            result.put("courier_id", map.get("courier_id").toString());
            result.put("state", map.get("state").toString());
            result.put("Useraddress", map.get("address").toString());// 用户地址
            result.put("Usermobile", map.get("mobile").toString());// 用户手机
            result.put("orderTitle", map.get("title").toString());// 订单标题
            int orderNum = Integer.parseInt(map.get("order_num").toString().substring(8));
            result.put("orderNum", orderNum);// 订单排号
            result.put("sale_type", map.get("sale_type").toString());
            result.put("realname", map.get("realname").toString());// 买姓名
            result.put("city_id", map.get("city_id").toString());// 城市ID
            result.put("timeRemark", map.get("time_remark"));// 送餐时间

            long time = System.currentTimeMillis();

            Integer accessTime = Integer.parseInt(map.get("access_time").toString());// 开始制作时间的秒数
            int workpiecesTimeTwo = accessTime + 45 * 60 - ((int) (time / 1000));
            result.put("accessTime", workpiecesTimeTwo);// 配送时间
            sql = "SELECT m.menu_id from order_menu m where m.order_id=?";
            List<Map<String, Object>> orderMenuList = this.findForJdbc(sql, oId);
            List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
            if (orderMenuList.size() > 0) {
                for (int j = 0; j < orderMenuList.size(); j++) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    Map<String, Object> orderMenuMap = orderMenuList.get(j);
                    int menuId = Integer.parseInt(orderMenuMap.get("menu_id").toString());
                    MenuEntity menu = this.get(MenuEntity.class, menuId);
                    m.put("menuName", menu.getName());// 菜名
                    m.put("menuPrice", menu.getPrice());// 价钱
                    m.put("menuImage", systemconfigService.getValByCode("img_url") + menu.getImage());// 菜图片
                    menuList.add(m);

                }

            }
            result.put("menuList", menuList);// 添加菜到集合中
            MerchantEntity merchant = this.get(MerchantEntity.class, merchantId);// 查询商家信息
            result.put("merchantAddress", merchant.getAddress());// 商家地址
            result.put("merchantPhone", merchant.getPhone());// 商家电话
            result.put("merchantMobile", merchant.getMobile());// 商家手机
            result.put("merchantLogo", systemconfigService.getValByCode("img_url") + merchant.getLogoUrl());// 商家LOGO

            orderlist.add(result);
        }

        return orderlist;
    }

    @Override
    public List<Map<String, Object>> getOrderByMerchanAndMoblie(String merchanId, String Moblie, int page, int rows) {
        String sql = "select o.id,o.access_time,o.address,o.card,o.card_id,o.city_id,o.sale_type,"
                + "o.comment_content,o.comment_display,o.comment_service,o.comment_speed,"
                + "o.comment_taste,o.comment_time,o.complete_time,o.courier_id,o.create_time,"
                + "o.credit,o.delivery_done_time,o.delivery_time,o.ifCourier,o.merchant_id,o.mobile,"
                + "o.online_money,o.order_type,o.origin,o.out_trace_id,o.pay_id,o.pay_state,o.pay_time,"
                + "o.pay_type,o.realname,o.remark,o.rereason,o.retime,o.rstate,o.sale_type,o.score,"
                + "o.score_money,o.state,o.time_remark as timeRemark,o.title,o.urgent_time,o.user_id,"
                + "CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num  "
                + "FROM `order` o where o.merchant_id=? and o.mobile=?";
        List<Map<String, Object>> list = findForJdbcParam(sql, page, rows, merchanId, Moblie);
        return list;
    }

    @Override
    public boolean merchantWhetherDoBusiness(int merchantId) {
        boolean flag = false;
        String sql = "SELECT * FROM merchant m "
                + "where m.start_time < TIME_TO_SEC(FROM_UNIXTIME(UNIX_TIMESTAMP(),'%H:%i:%s')) "
                + "and m.end_time > TIME_TO_SEC(FROM_UNIXTIME(UNIX_TIMESTAMP(),'%H:%i:%s')) "
                + "and m.display='Y' and m.id=?";
        List<Map<String, Object>> list = this.findForJdbc(sql, merchantId);
        if (list.size() > 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * 查询用户此订单实际支付金额
     */
    public String getOrderRealMoney(OrderEntity order) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(false);//关闭分组显示
        // 用户实际支付金额
        String totalMoney = numberFormat.format((order.getOrigin() + order.getDeliveryFee() + order.getCostLunchBox() // 订单金额+配送费+餐盒费
                - order.getScoreMoney() - order.getCard() // -积分抵扣-代金券抵扣
                - order.getMemberDiscountMoney() - order.getMerchantMemberDiscountMoney() // -平台会员折扣-商家会员折扣
                - order.getDineInDiscountMoney())); // -堂食折扣
        return totalMoney;
    }

    @Override
    public List<Map<String, Object>> getCourierRanking(int courierId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql = "SELECT a.rowNo,a.commentCourier,a.id,a.username,CONCAT((SELECT s.value FROM  "
                + " system_config s WHERE s.code='user_url'),a.photoUrl) photoUrl,a.orderCount from ( "
                + " SELECT  c.commentCourier,(@rowNum:=@rowNum+1) as rowNo ,c.courier_id,c.id,c.username,c.photoUrl ,c.orderCount from ( "
                + " SELECT AVG(o.comment_courier) as commentCourier,o.courier_id,u.id,u.username,u.photoUrl,COUNT(o.id) as orderCount FROM `order` o JOIN `user` u ON u.id=o.courier_id  GROUP BY o.courier_id ) c ,"
                + " (Select (@rowNum :=0) ) b  ORDER BY c.commentCourier DESC) a ";
        if (courierId > 0) {
            sql += " where a.id=?";
            list = this.findForJdbc(sql, courierId);
        } else {
            list = this.findForJdbc(sql);
        }
        return list;
    }

    @Override
    public AjaxJson verifyMenuQuantity(String params, int userId) {
        List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
        Map<String, Object> m = new HashMap<String, Object>();
        paramList = JSONHelper.toList(params);
        AjaxJson j = new AjaxJson();
        if (params != null && paramList.size() > 0) {
            for (int i = 0; i < paramList.size(); i++) {
                m = paramList.get(i);
                int menuId = Integer.valueOf(m.get("menuId").toString());
                int num = Integer.valueOf(m.get("num").toString());
                String salesPromotion = "N";// 是否促销
                int menuPromotionId = 0;// 促销ID
                if (m.get("salesPromotion") != null && !"".equals(m.get("salesPromotion").toString())) {
                    salesPromotion = m.get("salesPromotion").toString();
                }

                if (m.get("menuPromotionId") != null && !"".equals(m.get("menuPromotionId").toString())) {
                    menuPromotionId = Integer.parseInt(m.get("menuPromotionId").toString());
                }
                MenuEntity menu = menuService.getEntity(MenuEntity.class, menuId);
                // 判断菜品有没有存在
                if (menu == null) {
                    j.setMsg("菜品" + menuId + "不存在");
                    j.setStateCode("01");
                    j.setSuccess(false);
                    return j;
                }

                if (userId > 0 && "Y".equals(salesPromotion) && menuPromotionId > 0) {
                    // 促销量是否足够下单
                    MenuPromotionEntity menuPromotion = this.get(MenuPromotionEntity.class, menuPromotionId);
                    if (menuPromotion.getPromotion_quantity() <= 0 || menuPromotion.getPromotion_quantity() < num) {// 判断促销菜品的剩余促销是足够用户购买
                        j.setMsg(menu.getName() + "促销菜库存不足，还剩" + menuPromotion.getPromotion_quantity() + "份");
                        j.setStateCode("01");
                        j.setSuccess(false);
                        return j;

                    }
                    // 验证促销菜品是否在促销时间
                    List<Map<String, Object>> menuPromotionList = menuService.getMenuPromotionById(menuPromotionId);
                    if (menuPromotionList.size() == 0) {
                        j.setMsg(menu.getName() + "不在促销时间");
                        j.setStateCode("01");
                        j.setSuccess(false);
                        return j;
                    }

                    // 统计用户在该菜品促销时间里购买了多少份菜品
                    String sql = "SELECT ifnull(sum(m.quantity),0) quantity from `order` o JOIN order_menu m on o.id =m.order_id JOIN menu u on m.menu_id=u.id "
                            + "where  o.state in ('pay','accept','done','confirm','evaluated') and o.order_type in ('normal','mobile')"
                            + " and FROM_UNIXTIME(o.pay_time,'%y%m%d')>=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%y%m%d') and m.sales_promotion='Y'"
                            + " and m.menu_id =" + menuId + " and o.user_id=" + userId;

                    List<Map<String, Object>> countList = new ArrayList<Map<String, Object>>();
                    countList = this.findForJdbc(sql);
                    int sumQuantity = Integer.parseInt(countList.get(0).get("quantity").toString());// 用户在促销时间里该菜品购买的份数
                    int residuelimitPromotion = menuPromotion.getLimitQuantity() - sumQuantity; // 用户还能购买的份数
                    if (num > residuelimitPromotion) {
                        j.setMsg(menu.getName() + ":超出限购数,你还能购买" + residuelimitPromotion + "份");
                        j.setStateCode("01");
                        j.setSuccess(false);
                        return j;
                    }
                }
                if (num > menu.getTodayRepertory()) {
                    j.setMsg(menu.getName() + ":库存不足,还剩" + menu.getTodayRepertory() + "份");
                    j.setStateCode("01");
                    j.setSuccess(false);
                    return j;
                }

            }

        }
        j.setMsg("库存充足");
        j.setStateCode("00");
        j.setSuccess(true);
        return j;
    }

    @Override
    public void kitchenMakeAccomplish(int orderId) {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        order.setCookDoneTime(DateUtils.getSeconds());
        order.setCookDoneCode("done_execution");
        this.saveOrUpdate(order);

        orderStateService.cookDoneOrderState(orderId);
    }

    @Override
    public List<Map<String, Object>> getOrderBySaleType(int SaleType, int merchanId, String payState) {
        String sql = "SELECT o.id,o.pay_state,o.pay_id,o.user_id,o.city_id,FROM_UNIXTIME(o.create_time,'%Y-%m-%d %H:%i') create_time,"
                + " o.state,o.origin,o.sale_type,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num,o.time_remark,o.merchant_id  "
                + " from `order` o where o.order_type in ('mobile','normal') and o.pay_state='" + payState
                + "'  and  o.merchant_id=" + merchanId;

        if (SaleType == 1 || SaleType == 2) {
            sql = sql + " and o.sale_type=" + SaleType;
        }
        sql = sql + " ORDER BY o.create_time DESC";
        List<Map<String, Object>> list = this.findForJdbc(sql);
        if (list.size() == 0) {
            return list;
        }
        List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
        String ids = "";
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            int id = Integer.parseInt(map.get("id").toString());

            ids = ids + new StringBuffer().append(id + ",").toString();

        }

        String orderId = ids.substring(0, ids.length() - 1);
        sql = "SELECT o.order_id,o.menu_id,o.quantity,o.price,m.`name`,CONCAT((SELECT s.value FROM  "
                + " system_config s WHERE s.code='menu_url'),m.image) image "
                + " from order_menu o JOIN menu m ON o.menu_id=m.id where o.order_id in (" + orderId + ")";
        List<Map<String, Object>> menuList = this.findForJdbc(sql);

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> dineMap = list.get(i);
            // 订单菜品信息
            List<Map<String, Object>> menu = new ArrayList<Map<String, Object>>();
            for (int j = 0; j < menuList.size(); j++) {
                Map<String, Object> map = menuList.get(j);
                int oId = Integer.parseInt(dineMap.get("id").toString());// 订单ID
                int mId = Integer.parseInt(map.get("order_id").toString());// 菜单表订单ID

                if (oId == mId) {
                    menu.add(map);
                }

            }
            dineMap.put("menulist", menu);
            orderList.add(dineMap);
        }

        return orderList;
    }

    @Override
    public List<Map<String, Object>> getOrderByState(int merchanId, String code, int saleType, int codeType, int pageNo,
                                                     int row) {
        String sql = "SELECT o.id,o.pay_state,o.order_type,FROM_UNIXTIME(o.cook_done_time,'%Y-%m-%d %H:%i') cook_done_time,"
                + " FROM_UNIXTIME(o.pay_time,'%Y-%m-%d %H:%i') pay_time,FROM_UNIXTIME(o.start_time,'%Y-%m-%d %H:%i') start_time,o.pay_id,"
                + " o.user_id,o.city_id,o.cook_done_code,FROM_UNIXTIME(o.create_time,'%Y-%m-%d %H:%i') create_time,"
                + " o.state,o.origin,o.sale_type,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num,o.time_remark,o.merchant_id  "
                + " from `order` o where o.order_type in ('mobile','normal')"
                + " and ( FROM_UNIXTIME(o.pay_time,'%Y%m%d')=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%Y%m%d ') or (o.order_type = 'mobile' and FROM_UNIXTIME(o.create_time,'%Y%m%d ')=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%Y%m%d ') ) )"
                + " and  o.merchant_id=" + merchanId;
        if (codeType == 0) {
            sql = sql + " and o.cook_done_code='" + code + "'";
        } else {
            sql = sql + " and o.cook_done_code in ('start','done_execution')";
        }

        if (saleType == 1) {
            sql = sql + " and o.state='accept' and o.sale_type=1  ORDER BY o.create_time DESC";
        } else {
            sql = sql + " and o.state='confirm' and o.sale_type=2  ORDER BY o.create_time DESC";
        }

        List<Map<String, Object>> list = this.findForJdbc(sql, pageNo, row);
        if (list.size() == 0) {
            return list;
        }
        List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
        String ids = "";
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            int id = Integer.parseInt(map.get("id").toString());

            ids = ids + new StringBuffer().append(id + ",").toString();

        }

        String orderId = ids.substring(0, ids.length() - 1);
        sql = "SELECT o.order_id,o.menu_id,o.quantity,o.price,m.`name`,CONCAT((SELECT s.value FROM  "
                + " system_config s WHERE s.code='menu_url'),m.image) image "
                + " from order_menu o JOIN menu m ON o.menu_id=m.id where o.order_id in (" + orderId + ") ";
        List<Map<String, Object>> menuList = this.findForJdbc(sql);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> dineMap = list.get(i);
            // 订单菜品信息
            List<Map<String, Object>> menu = new ArrayList<Map<String, Object>>();
            for (int j = 0; j < menuList.size(); j++) {
                Map<String, Object> map = menuList.get(j);
                int oId = Integer.parseInt(dineMap.get("id").toString());// 订单ID
                int mId = Integer.parseInt(map.get("order_id").toString());// 菜单表订单ID

                if (oId == mId) {
                    menu.add(map);

                }

            }
            dineMap.put("menulist", menu);
            orderList.add(dineMap);
        }
        return orderList;
    }

    @Override
    public List<Map<String, Object>> getOrderByStateGroup(int merchanId, int saleType) {
        String sql = "SELECT  m.name,sum(o_m.quantity) quantity "
                + "from `order` o,menu m,order_menu o_m where o.id=o_m.order_id and o_m.menu_id=m.id and o.order_type in ('mobile','normal') and FROM_UNIXTIME(o.pay_time,'%Y%m%d ')=FROM_UNIXTIME(UNIX_TIMESTAMP(),'%Y%m%d ')  and  o.merchant_id="
                + merchanId;
        sql = sql + " and o.cook_done_code='notstart'";
        if (saleType == 1) {
            sql = sql + " and o.state='accept' and o.sale_type=1  group by m.id,m.name ORDER BY o.create_time DESC";
        } else {
            sql = sql + " and o.state='confirm' and o.sale_type=2  group by m.id,m.name ORDER BY o.create_time DESC";
        }
        List<Map<String, Object>> orderList = this.findForJdbc(sql);
        return orderList;
    }

    @Override
    public void completeOrder(int orderId) {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        order.setCookDoneCode("accomplish");
        this.saveOrUpdate(order);
        OrderStateEntity orderState = new OrderStateEntity();
        orderState.setDealTime(DateUtils.getSeconds());
        orderState.setDetail(order.getMerchant().getTitle() + "配送完成");
        orderState.setOrderId(orderId);
        orderState.setState("配送完成");
        this.save(orderState);
    }

    @Override
    public void startExecutionOrder(int orderId) {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        order.setCookDoneCode("start");
        order.setStartTime(DateUtils.getSeconds());
        this.saveOrUpdate(order);

        OrderStateEntity orderState = new OrderStateEntity();
        orderState.setDealTime(DateUtils.getSeconds());
        orderState.setDetail(order.getMerchant().getTitle() + "开始制作");
        orderState.setOrderId(orderId);
        orderState.setState("开始制作");
        this.save(orderState);
    }

    @Override
    public List<Map<String, Object>> getOrderMenuByOrderId(int orderId) {
        String sql = "SELECT m.id,m.order_id,m.menu_id,m.quantity," + "m.price,m.total_price,m.state,m.promotion_money,"
                + "m.sales_promotion,m.menu_promotion_id " + "FROM order_menu m " + "where m.order_id=?";
        List<Map<String, Object>> list = this.findForJdbc(sql, orderId);
        return list;
    }

    @Override
    public void autoCompleteOrder(OrderEntity order) throws Exception {
        Integer orderId = order.getId();
        logger.info("把订单orderId:{}置为完成状态, courieId:{}", orderId, order.getCourierId());
        String sql = " update `order` o set o.state='confirm', complete_time = UNIX_TIMESTAMP() where o.id=?";
        this.executeSql(sql, order.getId());

        if ("askrefund".equals(order.getRstate())) {
            // 申请退款中的订单，自动拒绝
            this.unacceptRefund(order.getId());
        } else {
            // 统计快递员实时订单
            // courierStatisticsRealtimeService.statisticsRealtime(order);
            // 统计商家实时订单
            // merchatStatisticsRealtimeService.statisticsRealtime(order);
        }
        orderIncomeService.createOrderIncome(order);
        orderStateService.autoCompleteOrderState(orderId);
        pushedOrderService.deleteAndBackupExpired(orderId);

        sql = "select o.merchant_id as merchantId  from `order` o where o.id = ?";
        Map<String, Object> mer = this.findOneForJdbc(sql, orderId);
        String source = merchantService.getMerchantSource(Integer.parseInt(mer.get("merchantId").toString()));
        // ===============================第三方Retail超市需求=======================================//
        if (Constants.MERCHANT_SOURCE_RETAIL.equals(source)) {
            logger.info(">>>>>>>>>>>>>进入一号生活的Retail中间件,进行确认完成订单操作【定时任务自动完成：autoCompleteOrder()】,orderId={}", orderId);
            RetailPortCall.updateOrder(orderId);
        }
        // ===============================第三方Retail超市需求=======================================//
    }

    @Override
    public List<Map<String, Object>> getMerchantOrderByOrderPay(Integer merchantId, Integer start, Integer num) {

        String sql1 = "SELECT o.time_remark as timeRemark,o.sale_type,o.user_id,o.urgent_time,o.mobile,o.order_type,o.delivery_fee,o.card,o.score_money,"
                + "o.rstate,o.pay_id,o.state,o.pay_state,o.id,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num,m.title,o.origin,o.courier_id,o.remark, m.cost_delivery, "
                + "FROM_UNIXTIME(o.create_time,'%Y-%m-%d %H:%i') create_time FROM " + " `order` o LEFT JOIN "
                + " merchant m ON o.merchant_id=m.id "
                + " WHERE o.sale_type=1 and o.state='unpay' and (o.order_type='normal' or o.order_type='supermarket') and o.merchant_id=?"
                + " ORDER BY o.create_time DESC " + " LIMIT " + start + "," + num;
        String sql2 = "SELECT o_m.quantity,o_m.price,o_m.total_price,CONCAT(s.value,m.image) image,m.name " + "FROM "
                + " order_menu o_m," + " menu m," + " system_config s "
                + "WHERE o_m.menu_id=m.id AND o_m.order_id=? AND s.code='menu_url'";

        List<Map<String, Object>> merchantOrderMap = this.findForJdbc(sql1, merchantId);
        List<Map<String, Object>> merchantOrderList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> merchantOrderDetail = new ArrayList<Map<String, Object>>();
        Map<String, Object> m = new HashMap<String, Object>();

        for (int i = 0; i < merchantOrderMap.size(); i++) {
            m = merchantOrderMap.get(i);
            merchantOrderDetail = this.findForJdbc(sql2, m.get("id").toString());
            m.put("merchantOrderDetail", merchantOrderDetail);
            merchantOrderList.add(m);
            Object oriObj = m.get("origin");
            Object deliObj = m.get("delivery_fee");
            Double totalMoney = (Double.parseDouble(oriObj.toString()) * 100
                    + Double.parseDouble(deliObj.toString()) * 100) / 100.0;
            // 解决精度损失问题
            m.put("origin", oriObj.toString());
            m.put("delivery_fee", deliObj.toString());
            m.put("totalMoney", String.format("%.2f", totalMoney));
        }
        return merchantOrderList;
    }

    @Override
    public void updateOrderByPhoneOrder(OrderEntity order) throws Exception {
        logger.info("-------------商家把未付款的外卖订单转为电话订单， 并生成排号");
        Integer merchantId = order.getMerchant().getId();
        order.setOrderType("mobile");
        order.setOrderNum(AliOcs.genOrderNum(merchantId.toString()));
        this.saveOrUpdate(order);
        // 商家接单
        merchantAcceptOrder(order);
        if (OrderEntity.SaleType.TAKEOUT.equals(order.getSaleType())) {
            pushOrder(order.getId());
        }
        menuService.buyCount(order.getId());
        // 添加商家修改未支付为电话订单记录
        orderStateService.merchantUpdateOrder(order.getId());
    }

    @Override
    public List<Map<String, Object>> askRefundOrderList(int type, int merchantId, int start, int num) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql = "select o.id,o.pay_id,o.pay_type,o.user_id,o.courier_id,o.city_id,o.card_id,"
                + "o.`status`,o.state,o.rstate,o.rereason,FROM_UNIXTIME(o.retime,'%y-%m-%d %H:%i:s') retime,"
                + "o.mobile,o.address,o.online_money,o.origin,FROM_UNIXTIME(o.create_time,'%y-%m-%d %H:%i:s') create_time,"
                + "o.pay_time,o.merchant_id,o.score_money,o.score,o.order_type,"
                + "FROM_UNIXTIME(o.delivery_time,'%y-%m-%d %H:%i:s') delivery_time,"
                + "o.title,o.pay_state,o.sale_type,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num "
                + "from `order` o where o.merchant_id= " + merchantId;

        if (type == 1) {
            sql += " and o.rstate='askrefund' ";
        } else if (type == 2) {
            sql += " and o.rstate='norefund' and o.state='refund' ";
        }

        sql += " ORDER BY o.retime DESC LIMIT " + start + " , " + num;
        List<Map<String, Object>> orderList = this.findForJdbc(sql);
        if (orderList.size() > 0) {
            for (int i = 0; i < orderList.size(); i++) {
                Map<String, Object> map = orderList.get(i);
                int orderId = Integer.valueOf(map.get("id").toString());
                List<Map<String, Object>> orderMenuList = new ArrayList<Map<String, Object>>();
                sql = "select * from order_menu m where m.order_id=" + orderId;
                orderMenuList = this.findForJdbc(sql);
                map.put("orderMenuList", orderMenuList);
                list.add(map);
            }
        }

        return list;
    }

    @Override
    public Boolean cancelUnpayOrder(Integer orderId, Integer userId) {
        logger.info("用户user_id:" + userId + "删除订单order_id:" + orderId);
        String sql = " delete from `order` where id= ? and state='unpay' and  user_id = ? ";
        Integer result = this.commonDao.executeSql(sql, orderId, userId);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean orderRefund(Integer orderId, String rereason, Integer userId) throws Exception {
        Boolean result = false;
        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (order != null && "pay".equals(order.getState()) && order.getWuser().getId().intValue() == userId) {
            this.orderRefund(orderId, rereason);// 验证后调用退款 业务方法
            result = true;

            // ===============================第三方Retail超市需求=======================================//
            String source = merchantService.getMerchantSource(order.getMerchant().getId());
            if (Constants.MERCHANT_SOURCE_RETAIL.equals(source)) {
                logger.info(">>>>>>>>>>>>>进入一号生活的Retail中间件,进行取消订单操作【orderRefund()】,orderId={}", orderId);
                RetailPortCall.cancelOrder(orderId);
            }
            // ===============================第三方Retail超市需求=======================================//
        }
        return result;
    }

    @Override
    public Boolean askRefund(Integer orderId, String refundReason, Integer userId) {
        Boolean result = false;
        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (order != null && ("accept".equals(order.getState()) || "cooked".equals(order.getState())
                || "delivery".equals(order.getState())) && order.getWuser().getId().intValue() == userId) {
            this.askRefund(orderId, refundReason); // 验证后调用申请退单 业务方法
            result = true;
        }
        return result;
    }

    @Override
    public Double getTotalMoney(Long merchantId, Long startTime, Long endTime) {
        String sql = "select sum(origin) from `order` where merchant_id = ? AND create_time BETWEEN ? AND ? ";
        return this.orderDao.findOneForJdbc(sql, Double.class, merchantId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getOrderList(Integer start, Integer pageSize, Integer userId, Integer merchantId,
                                                  String... state) {
        String sql = " SELECT " + " o.id, " + " o.sale_type as saleType, " + "  o.pay_id as payId, "
                + " o.pay_type as payType, " + "    o.pay_state as payState, "
                + " CONVERT(SUBSTRING(o.order_num,9), UNSIGNED)    orderNum, " + "  o.origin, " + " o.state, "
                + " o.rstate, " + " FROM_UNIXTIME( `o`.create_time, '%Y-%m-%d %H:%i' )    `createTime`, "
                + " FROM_UNIXTIME( `o`.pay_time, '%Y-%m-%d %H:%i' )    `payTime`, " + " o.order_type as orderType, "
                + " o.realname as realName, " + "   o.mobile as mobile, " + "   o.address, "
                + "  o.time_remark AS timeRemark, " + " mer.title, " + "    mer.id        AS 'merchantId', "
                + " CONCAT( ( SELECT s.`value` FROM `system_config` s WHERE s.`code` = 'user_url' ), mer.logo_url )  image "
                + " FROM `order` o " + "    LEFT JOIN merchant mer " + "      ON o.merchant_id = mer.id "
                + " WHERE o.order_type IN ('normal','mobile') " + "   AND o.order_type <> 'recharge' ";

        List<Object> params = new ArrayList<Object>();
        if (userId != null) {
            sql += " AND o.user_id = ? ";
            params.add(userId);
        }
        if (merchantId != null) {
            sql += " AND mer.id = ? ";
            params.add(merchantId);
        }

        if (state != null && state.length > 0) {
            sql += " and o.state in ( ";
            for (int i = 0; i < state.length; i++) {
                sql += " ? ";
                if (i != state.length - 1) {
                    sql += ",";
                }
            }
            params.addAll(Arrays.asList(state));
            sql += " ) ";
        }
        sql += "    ORDER BY o.create_time DESC ";
        sql += "    LIMIT " + start + ", " + pageSize;

        List<Map<String, Object>> orderList = this.commonDao.findForJdbc(sql, params.toArray());
        if (orderList != null && orderList.size() > 0) {
            sql = " SELECT  om.id," + "       om.order_id as orderId, " + "       om.menu_id as menuId,"
                    + "       om.quantity,  " + "         om.price," + "          om.total_price as totalPrice,"
                    + "       om.state," + "          om.promotion_money as promotionMoney,"
                    + "       om.sales_promotion as salesPromotion,"
                    + "       om.menu_promotion_id as menuPromotionId," + "       m.name,  "
                    + "       CONCAT(sc.value, m.image )  image " + " FROM order_menu om "
                    + "     LEFT JOIN menu m ON om.menu_id = m.id "
                    + "     LEFT JOIN system_config sc  on sc.code = 'user_url' " + " WHERE om.order_id = ? ";
            for (int i = 0; i < orderList.size(); i++) {
                Map<String, Object> order = orderList.get(i);
                List<Map<String, Object>> menuList = this.commonDao.findForJdbc(sql, order.get("id"));
                order.put("menuList", menuList);
            }
        }
        return orderList;
    }

    @Override
    public List<Map<String, Object>> getOrderByPayState(int merchantId, int start, int num) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
        String sql = "select o.id,o.pay_id,o.pay_type,o.user_id,o.courier_id,o.city_id,"
                + "o.card_id,o.`status`,o.state,o.rstate,o.rereason,"
                + "FROM_UNIXTIME(o.retime,'%y-%m-%d %H:%i:s') retime," + "o.mobile,o.address,o.online_money,o.origin,"
                + "FROM_UNIXTIME(o.create_time,'%y-%m-%d %H:%i:s') create_time,"
                + "o.pay_time,o.merchant_id,o.score_money,o.score,o.order_type,"
                + "FROM_UNIXTIME(o.delivery_time,'%y-%m-%d %H:%i:s') delivery_time,"
                + "o.title,o.pay_state,o.sale_type,CONVERT(SUBSTRING(o.order_num,9), UNSIGNED) order_num"
                + " from `order` o  where o.pay_state='unpay' " + "and (UNIX_TIMESTAMP()-o.create_time)>600 "
                + "and o.user_id not in "
                + "(select r.user_id from `order` r where (UNIX_TIMESTAMP()-r.create_time)>1800 GROUP BY o.user_id) "
                + "and o.merchant_id=" + merchantId + " ORDER  BY o.create_time DESC LIMIT " + start + " , " + num;
        orderList = this.findForJdbc(sql);
        if (orderList.size() > 0) {
            for (int i = 0; i < orderList.size(); i++) {
                Map<String, Object> map = orderList.get(i);
                int orderId = Integer.valueOf(map.get("id").toString());
                List<Map<String, Object>> orderMenuList = new ArrayList<Map<String, Object>>();
                sql = "select * from order_menu m where m.order_id=" + orderId;
                orderMenuList = this.findForJdbc(sql);
                map.put("orderMenuList", orderMenuList);
                list.add(map);
            }
        }

        return list;
    }

    @Override
    public List<Map<String, Object>> countByStatus(Integer courierId, String status) {
        String sql = "select distinct state, count(*) c from `order` where courier_id=? ";
        if (StringUtils.isNotEmpty(status)) {
            sql += " and state='" + status + "'";
        }
        if ("confirm".equals(status)) {
            sql += " and DATE(FROM_UNIXTIME(complete_time)) = curdate() group by state ";
        } else {
            sql += " and DATE(FROM_UNIXTIME(create_time)) = curdate() group by state ";
        }
        return findForJdbc(sql, new Object[]{courierId});
    }


    @Override
    public AjaxJson scramble(Integer courierId) {
        // 是否可以用新的推单算法
        if (isUsingNewAlgorithm) {
            return scrambleNew(courierId);
        } else {
            AjaxJson j = new AjaxJson();
            j.setMsg("抢单失败");
            j.setSuccess(false);
            List<Map<String, Object>> orders = pushedOrderService.canScramble(courierId, false);
            // 随机打乱顺序
            if (orders != null && orders.size() > 0) {
                Collections.shuffle(orders);
                Map<String, Object> pushedOrder = orders.get(0);
                Integer orderId = Integer.parseInt(pushedOrder.get("order_id").toString());
                j = scrambleOrder(courierId, orderId);
                orderStateService.scrambleOrder(courierId, orderId);
            } else {
                logger.warn("该快递员【" + courierId + "】目前没有可抢订单，或者该快递员不在上班时间！");
                j.setMsg("您目前没有可抢订单，或者您还没打卡上班");
            }
            return j;
        }
    }

    public AjaxJson scrambleNew(Integer courierId) {
        AjaxJson j = scambleAlgorithmService.courierScamble(courierId);
        if (!j.isSuccess()) {
            return j;
        }
        List<Map<String, Object>> accptList = this.countByStatus(courierId, "accept");
        if (CollectionUtils.isNotEmpty(accptList)) {
            Map<String, Object> accptMap = accptList.get(0);
            Map<String, String> pushMap = new HashMap<String, String>();
            pushMap.put("orderId", j.getObj().toString());
            pushMap.put("accept", accptMap.get("c").toString());
            String title = "您抢到一个新的订单";
            pushMap.put("title", title);
            pushMap.put("content", title);
            pushMap.put("voiceFile", "");
            jpushService.push(courierId, pushMap);
            logger.info("向快递员" + courierId + "推送已抢单消息成功");
            Integer orderId = Integer.parseInt(j.getObj().toString());
            // 是供应链单通知供应链系统状态
            if (isSupplyChainOrder(orderId)) {
                supplyOrderService.noticeSupplyChain(courierId, orderId, Constants.ACCEPTED);
            }
        } else {
            logger.error("courierId:{} accptList is empty !!!", courierId);
        }

        j.setMsg("抢单成功");
        j.setSuccess(true);
        //tlm_statistics_realtime 插统计数据
        tlmStatisticsService.findFromTlmStatisticsByCourierId(courierId);
        orderStateService.scrambleOrder(courierId, Integer.parseInt(j.getObj().toString()));
        return j;
    }

    @Override
    public AjaxJson scrambleOrder(Integer courierId, Integer orderId) {
        AjaxJson j = new AjaxJson();
        j.setMsg("抢单失败");
        j.setSuccess(false);
        j.setObj(orderId);

        if (isSupplyChainOrder(orderId)) {
            boolean result = supplyOrderService.noticeSupplyChain(courierId, orderId, Constants.ACCEPTED);
            if (!result) { // 通知供应链先进行状态的修改 如果供应链返回结果失败，那么不允许执行下面的流程
                return j;
            }
        }
        String sqltmp = "select id, courier_id, rstate  from `order` where id = ?";
        Map<String, Object> orderAfterSetCourier = this.findOneForJdbc(sqltmp, new Object[]{orderId});

        if (orderAfterSetCourier == null) {
            logger.warn("抢单失败，所抢订单【" + orderId + "】不存在");
            // 不存在的订单记录 将推送记录全部删除
            pushedOrderService.deletePushedOrders(orderId);
            j.setMsg("抢单失败，您没有可抢订单");
            return j;
        } else {
            String orderCourierId = orderAfterSetCourier.get("courier_id") == null ? "0" : orderAfterSetCourier.get("courier_id").toString();
            if (!"0".equals(orderCourierId)) {
                logger.warn("该订单{}已被其他快递员{}抢走!", orderId, orderCourierId);
                pushedOrderService.deletePushedOrders(orderId);
                j.setMsg("抢单失败，所抢订单已被其他快递员抢走");
                return j;
            }
            String restate = orderAfterSetCourier.get("rstate") == null ? "" : orderAfterSetCourier.get("rstate").toString();
            if (StringUtils.equals("berefund", restate)) {
                pushedOrderService.deletePushedOrders(orderId);
                j.setMsg("抢单失败，用户已退款");
                return j;
            }
        }
        Integer deleteRows = 0;
        String key = "courier_order_" + orderId;
        IDistributedLock lock = new MemcachedDistributedLock();
        String uuid = null;
        try {
            uuid = lock.tryAcquireLock(key, 2 * 60, 60);
            if (uuid == null) {
                logger.info("抢单失败，失败原因，无法获取锁");
                return j;
            } else {
                deleteRows = pushedOrderService.deletePushedOrders(orderId);
            }
        } finally {
            if (uuid != null) {
                lock.releaseLock(key, uuid);
            }
        }
        logger.info("抢单临时记录已删除，orderId=" + orderId + ",删除记录数=" + deleteRows);
        if (deleteRows > 0) {
            logger.info("保存快递员" + courierId + "到订单记录中");
            String sqlString = "update `order` set courier_id = ? where id=?";
            int updateRow = this.executeSql(sqlString, new Object[]{courierId, orderId});
            if (updateRow == 0) {
                logger.error("更新订单orderId=" + orderId + "对应的快递员ID：" + courierId + "失败");
            } else {
                orderAfterSetCourier = this.findOneForJdbc(sqltmp, new Object[]{orderId});
                logger.info("orderAfterSetCourier:" + JSON.toJSONString(orderAfterSetCourier) + ",orderId=" + orderId + ", courierId=" + courierId);
            }
            // 推送“我的订单数”
            List<Map<String, Object>> accptList = this.countByStatus(courierId, "accept");
            if (CollectionUtils.isNotEmpty(accptList)) {
                Map<String, Object> accptMap = accptList.get(0);
                Map<String, String> pushMap = new HashMap<String, String>();
                pushMap.put("orderId", orderId.toString());
                pushMap.put("accept", accptMap.get("c").toString());
                String title = "您抢到一条新的订单";
                pushMap.put("title", title);
                pushMap.put("content", title);
                pushMap.put("voiceFile", "");
                jpushService.push(courierId, pushMap);
            } else {
                logger.warn("courierId:{} accptList is empty !!!", courierId);
            }
            j.setMsg("抢单成功");
            j.setSuccess(true);
            // tlm_statistics_realtime 插统计数据
            tlmStatisticsService.findFromTlmStatisticsByCourierId(courierId);
        } else {
            logger.warn("抢单失败，该订单【" + orderId + "】已被其他快递员先抢到！");
            j.setMsg("抢单失败，该订单已被其他快递员先抢到！");
        }
        return j;
    }

    @Override
    public List<Map<String, Object>> getTransOrderByCourierId(Integer courierId, String startDate, String endDate) {
        String sql = "select * from 0085_order_translog where old_courier_id=" + courierId;
        if (StringUtils.isNotEmpty(startDate)) {
            sql += " and DATE(FROM_UNIXTIME(trans_time)) >= '" + startDate + "'";
        }
        if (StringUtils.isNotEmpty(endDate)) {
            sql += " and DATE(FROM_UNIXTIME(trans_time)) <= '" + endDate + "'";
        }
        return this.findForJdbc(sql);
    }

    /**
     * 获取快递员可拿提成的送餐份数
     *
     * @param courierId
     * @param startDate
     * @param endDate
     * @return
     */
    public Long getCourierMenus(Integer courierId, String startDate, String endDate) {
        if (StringUtils.isBlank(courierMinDeductPrice)) {
            courierMinDeductPrice = "9.9";
        }
        String sql = "select sum(om.quantity) from order_menu om ";
        sql += " left join menu m on m.id=om.menu_id ";
        sql += " left join `order` o on o.id=om.order_id ";
        sql += " left join merchant mer on o.merchant_id=mer.id ";
        sql += " left join 0085_merchant_info mi on mi.merchant_id=mer.id ";
        sql += " where o.state in ('confirm','done') and o.rstate<>'berefund' and o.courier_id=? ";
        sql += " and om.price>= case when mi.courier_min_deduct_money is not null then mi.courier_min_deduct_money else ? end  ";
        if (StringUtils.isNotEmpty(startDate)) {
            sql += " and DATE(FROM_UNIXTIME(o.complete_time)) >= '" + startDate + "'";
        }
        if (StringUtils.isNotEmpty(endDate)) {
            sql += " and DATE(FROM_UNIXTIME(o.complete_time)) <= '" + endDate + "'";
        }
        return this.getCountForJdbcParam(sql, new Object[]{courierId, courierMinDeductPrice});
    }

    /**
     * 获取快递员可拿提成的订单数
     *
     * @param courierId
     * @param deductDate
     * @return
     */
    public Long getCourierOrders(Integer courierId, String deductDate) {
        if (StringUtils.isBlank(courierMinDeductPrice)) {
            courierMinDeductPrice = "9.9";
        }
        String sql = "select * from (select distinct state, count(o.id) c, sum(o.origin) s from `order` o ";
        sql += " left join merchant mer on o.merchant_id=mer.id ";
        sql += " left join 0085_merchant_info mi on mi.merchant_id=mer.id ";
        sql += " where courier_id=? and date(from_unixtime(o.create_time))=? and date(from_unixtime(o.complete_time))=? and rstate<>'berefund' ";
        sql += " and o.origin>= case when mi.courier_min_deduct_money is not null then mi.courier_min_deduct_money else ? end ";
        sql += " group by state) st ";
        sql += " where st.state='confirm'";
        List<Map<String, Object>> daylyOrders = this.findForJdbc(sql,
                new Object[]{courierId, deductDate, deductDate, courierMinDeductPrice});
        if (daylyOrders != null && daylyOrders.size() > 0) {
            Map<String, Object> map = daylyOrders.get(0);
            Object orders = map.get("c");
            if (orders != null) {
                return Long.parseLong(orders.toString());
            }
        }
        return 0L;
    }

    @Override
    public Long getCourierNormalOrders(Integer courierId, String deductDate) {
        if (courierId == null || deductDate == null) {
            throw new IllegalArgumentException("courierId == null 或 deductDate == null");
        }
        if (StringUtils.isBlank(courierMinDeductPrice)) {
            courierMinDeductPrice = "9.9";
        }
        String sql = "select * from (select distinct state, count(o.id) c, sum(o.origin) s from `order` o ";
        sql += " left join merchant mer on o.merchant_id=mer.id ";
        sql += " left join 0085_merchant_info mi on mi.merchant_id=mer.id ";
        sql += " where courier_id=? and o.from_type <> 'crowdsourcing' and o.from_type <> 'supplychain'  ";
        sql += " and date(from_unixtime(o.create_time))=? and date(from_unixtime(o.complete_time))=? and rstate<>'berefund' ";
        sql += " and o.origin>= case when mi.courier_min_deduct_money is not null then mi.courier_min_deduct_money else ? end ";
        sql += " group by state) st ";
        sql += " where st.state='confirm'";
        List<Map<String, Object>> daylyOrders = this.findForJdbc(sql,
                new Object[]{courierId, deductDate, deductDate, courierMinDeductPrice});
        if (CollectionUtils.isNotEmpty(daylyOrders)) {
            Map<String, Object> map = daylyOrders.get(0);
            Object orders = map.get("c");
            if (orders != null) {
                logger.info("getCourierNormalOrders courierId:{}, orders: {}", courierId, orders);
                return Long.parseLong(orders.toString());
            } else {
                logger.warn("courierId:{} no orders !!!", courierId);
            }
        } else {
            logger.warn("courierId:{} no orders !!!", courierId);
        }
        return 0L;
    }

    @Override
    public Long getCrowdsourcingDeduct(Integer courierId, String deductDate) {
        if (courierId == null || deductDate == null) {
            throw new IllegalArgumentException("courierId == null 或 deductDate == null");
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " SELECT case when sum(toc.crowdsourcing_courier_deduct)  is null then 0 else sum(toc.crowdsourcing_courier_deduct) end ");
        sql.append(" FROM `order` o ");
        sql.append(" LEFT JOIN tom_order_crowdsourcing toc on o.id=toc.order_id ");
        sql.append(" WHERE courier_id=?");
        sql.append(
                " and date(from_unixtime(o.create_time))=? and date(from_unixtime(o.complete_time))=? and rstate<>'berefund'");
        sql.append(" and o.from_type = 'crowdsourcing'");
        sql.append(" and o.state='confirm'");

        return this.findOneForJdbc(sql.toString(), Long.class, new Object[]{courierId, deductDate, deductDate});
    }

    @Override
    public Integer getCrowdsourcingQuantity(Integer courierId, String deductDate) {
        if (courierId == null || deductDate == null) {
            throw new IllegalArgumentException("courierId == null 或 deductDate == null");
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT count( id ) ");
        sql.append(" FROM  `order` o ");
        sql.append(" WHERE courier_id=?");
        sql.append(
                " and date(from_unixtime(o.create_time))=? and date(from_unixtime(o.complete_time))=? and rstate<>'berefund'");
        sql.append(" and o.from_type = 'crowdsourcing'");
        sql.append(" and o.state='confirm'");
        return this.findOneForJdbc(sql.toString(), Integer.class, new Object[]{courierId, deductDate, deductDate});
    }

    public Long getMerchantMenus(Integer merchantId, String startDate, String endDate) {
        String sql = "select sum(om.quantity) from order_menu om ";
        sql += " left join menu m on m.id=om.menu_id ";
        sql += " left join `order` o on o.id=om.order_id ";
        sql += " left join merchant mer on o.merchant_id=mer.id ";
        sql += " where o.state in ('confirm','done') and o.rstate<>'berefund' and mer.id=? ";
        if (StringUtils.isNotEmpty(startDate)) {
            sql += " and DATE(FROM_UNIXTIME(o.complete_time)) >= '" + startDate + "'";
        }
        if (StringUtils.isNotEmpty(endDate)) {
            sql += " and DATE(FROM_UNIXTIME(o.complete_time)) <= '" + endDate + "'";
        }
        return this.getCountForJdbcParam(sql, new Object[]{merchantId});
    }

    /**
     * 获取可配送该订单的快递员
     *
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> getRelaCourier(Integer orderId) {
        // 负责该商家的所有快递员
        String sql1 = "select m.courier_id from 0085_courier_merchant m ";
        sql1 += " left join `order` o on o.merchant_id = m.merchant_id ";
        sql1 += " where o.id = ? ";
        List<Map<String, Object>> list1 = this.findForJdbc(sql1, new Object[]{orderId});

        // 负责该栋楼的所有快递员
        String sql2 = "select b.courier_id from 0085_courier_building b ";
        sql2 += " left join address a on a.building_id = b.building_id ";
        sql2 += " left join `order` o on o.user_address_id = a.id ";
        sql2 += " where o.id = ? ";
        List<Map<String, Object>> list2 = this.findForJdbc(sql2, new Object[]{orderId});

        if (list2 != null && list2.size() > 0 && list2.retainAll(list1)) {// 取交集
            if (list2.size() > 0) {
                return list2;
            }
        }
        // 如果匹配不到送餐地址，返回负责商家的快递员
        return list1;
    }

    @Override
    public Integer autoScramble(Integer orderId) {
        List<Map<String, Object>> list = this.getRelaCourier(orderId);
        if (list != null && list.size() > 0) {
            // 随机分配订单给某一个快递员
            Collections.shuffle(list);
            Map<String, Object> map = list.get(0);
            Integer courierId = Integer.parseInt(map.get("courier_id").toString());
            OrderEntity order = this.getEntity(OrderEntity.class, orderId);
            order.setCourierId(courierId);
            this.save(order);

            // 推送“我的订单数”
            List<Map<String, Object>> accptList = this.countByStatus(courierId, "accept");
            if (CollectionUtils.isNotEmpty(accptList)) {
                Map<String, Object> accptMap = accptList.get(0);
                Map<String, String> pushMap = new HashMap<String, String>();
                pushMap.put("orderId", orderId.toString());
                pushMap.put("accept", accptMap.get("c").toString());
                String title = "您有一条新的订单";
                pushMap.put("title", title);
                pushMap.put("content", title);
                pushMap.put("voiceFile", SoundFile.SOUND_NEW_ORDER);
                jpushService.push(courierId, pushMap);
            } else {
                logger.warn("courierId:{} accptList is empty!!!", courierId);
            }
            return courierId;
        } else {
            logger.error("未找到合适的快递员负责配送该商家，和该送餐地址");
        }
        return 0;
    }

    /**
     * @param state  订单状态，如果空字串或者null则查所有状态。订单状态：unpay未支付，pay支付成功，accept制作中，done待评价
     *               ，confirm 已完成，refund 退款 delivery 配送中，delivery_done配送完成
     * @param userId 用户id
     * @return
     */
    public List<OrderMerchantVo> queryMineOrderByState(String state, int userId, int page, int rows) {
        StringBuffer sbsql = new StringBuffer();
        String replaceState = "NEED_REPLACE_STATE_STATEMENT_201508241421";
        sbsql.append(
                "SELECT o.id as order_id, o.state, o.origin, o.order_type as orderType, ifnull(o.order_num, '') as orderNum, ")
                .append(" CASE WHEN (SELECT  COUNT(order_id) ").append(" FROM ").append(" 0085_order_comment ")
                .append(" WHERE ").append(" user_id = ? AND order_id = o.id AND comment_display in  ('Y','N') ) > 0")
                .append(" THEN TRUE ELSE FALSE END iscommented , ")
                .append(" from_unixtime(o.create_time, '%Y-%m-%d %H:%i') as create_time, ")
                .append(" from_unixtime(o.pay_time, '%Y-%m-%d %H:%i') as pay_time,  ")
                .append(" from_unixtime(o.comment_time, '%Y-%m-%d %H:%i') as comment_time,  ")
                .append(" from_unixtime(o.retime, '%Y-%m-%d %H:%i') as refund_time, ")
                .append(" m.id as merchant_id, m.title as merchant_title, m.logo_url,  ")
                .append(" o.courier_id as courier_id, o.rstate as refund_state, o.delivery_done_time as delivery_done_time, ")
                .append(" o.delivery_fee as deliveryFee, o.score_money as scoreMoney, o.card as couponsMoney ")
                .append(" from ").append(" `order` o inner join ").append(" merchant m on o.merchant_id = m.id ");

        sbsql.append(
                " where o.user_id = ? and ( o.order_type = 'normal' or o.order_type = 'third_part' or o.order_type = 'scan_order') ");
        sbsql.append(replaceState);// eg. and o.state = ?
        sbsql.append(" order by o.create_time DESC ");// 按订单创建时间反序排列

        List<Map<String, Object>> resList = Lists.newArrayList();
        // 返回的数据集
        List<OrderMerchantVo> orvList = Lists.newArrayList();

        String querySql = "";
        if (StringUtils.isNotBlank(state)) {
            if (StringUtils.equals(state, OrderStateEnum.CUSTOM_RECEIVING.getOrderStateEn())) {
                // 待收货的sql查询
                StringBuffer sbTmp = new StringBuffer();
                sbTmp.append(" and o.state in ('");
                sbTmp.append(OrderStateEnum.PAY.getOrderStateEn());
                sbTmp.append("', '").append(OrderStateEnum.DELIVERY.getOrderStateEn());
                sbTmp.append("', '").append(OrderStateEnum.ACCEPT.getOrderStateEn()).append("') ");
                ;
                querySql = StringUtils.replaceOnce(sbsql.toString(), replaceState, sbTmp.toString());
                resList = this.findForJdbcParam(querySql, page, rows, userId, userId);
                orvList.addAll(composeOrderMerchantVo(resList));
                return orvList;
            } else {
                // 替换 sql中的订单状态条件
                querySql = StringUtils.replaceOnce(sbsql.toString(), replaceState, " and o.state = ? ");
                resList = this.findForJdbcParam(querySql, page, rows, userId, userId, state);
                orvList.addAll(composeOrderMerchantVo(resList));
                return orvList;
            }
        } else {
            // 查询所有订单的状态，包括未评价，已经评价的
            // 删除sql中的订单状态条件
            querySql = StringUtils.remove(sbsql.toString(), replaceState);
            resList = this.findForJdbcParam(querySql, page, rows, userId, userId);
            orvList.addAll(composeOrderMerchantVo(resList));

            return orvList;
        }

    }

    /**
     * 组装VO
     *
     * @param resList
     * @return
     */
    private List<OrderMerchantVo> composeOrderMerchantVo(List<Map<String, Object>> resList) {
        List<OrderMerchantVo> orvList = Lists.newArrayList();
        if (resList != null && resList.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (Map<String, Object> map : resList) {
                OrderMerchantVo omv = new OrderMerchantVo();
                omv.setOrderId(Integer.parseInt(String.valueOf(map.get("order_id"))));
                omv.setOrderNum(map.get("orderNum").toString());
                omv.setOrderState(String.valueOf(map.get("state")));
                omv.setOrderType(String.valueOf(map.get("orderType")));
                if (StringUtils.isNoneBlank(omv.getOrderState())) {
                    omv.setOrderStateCn(OrderStateEnum.getCnNameByEnName(omv.getOrderState()));
                }
                try {
                    omv.setOrderCreateTime(map.get("create_time") == null ? null
                            : (sdf.parse(String.valueOf(map.get("create_time")))));
                    omv.setOrderPayTime(
                            map.get("pay_time") == null ? null : (sdf.parse(String.valueOf(map.get("pay_time")))));
                    omv.setOrderCommentTime(map.get("comment_time") == null ? null
                            : (sdf.parse(String.valueOf(map.get("comment_time")))));
                    omv.setOrderRefundTime(map.get("refund_time") == null ? null
                            : (sdf.parse(String.valueOf(map.get("refund_time")))));
                    // omv.setDeliveryDoneTime(map.get("delivery_done_time") ==
                    // null ? null :
                    // (sdf.parse(String.valueOf(map.get("delivery_done_time")))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                omv.setOrderOrigin(Double.parseDouble(String.valueOf(map.get("origin"))));
                omv.setDeliveryFee(Double.parseDouble(String.valueOf(map.get("deliveryFee"))));
                omv.setScoreMoney(Double.parseDouble(String.valueOf(map.get("scoreMoney"))));
                omv.setCouponsMoney(Double.parseDouble(String.valueOf(map.get("couponsMoney"))));
                omv.setRefundState(String.valueOf(map.get("refund_state")));

                // 快递员id
                omv.setCourierId(Integer.parseInt(String.valueOf(map.get("courier_id"))));

                omv.setMerchantId(Integer.parseInt(String.valueOf(map.get("merchant_id"))));
                omv.setMerchantTitle(StringUtils.defaultString(String.valueOf(map.get("merchant_title")), "0"));
                omv.setMerchantLogoUrl(StringUtils.defaultString(String.valueOf(map.get("logo_url")), "0"));
                // 是否评价
                omv.setEvaluated("1".equals(String.valueOf(map.get("iscommented"))));
                orvList.add(omv);
            }
        }
        return orvList;
    }

    @Override
    public boolean validOrderByOrderId(Integer orderId) {
        // step 1 查询是否超时
        Map<String, Object> map = findOneForJdbc("select o.create_time as createTime from `order` as o where o.id = ?",
                orderId);
        if (null != map) {
            int createTime = (Integer) map.get("createTime");
            // 比较时间，是否在下单的20分钟内
            boolean flag = (System.currentTimeMillis() / 1000 - createTime) / 60.0 > 20;
            if (flag) {
                executeSql("update `order` as o set o.state = ? where o.id=?", "cancel", orderId);
                return !flag;
            }
        } else {
            return false;
        }
        // step 2 判断未支付订单的菜单价格是否变动
        List<Map<String, Object>> list = findForJdbc("select m.menu_id as menuId, "
                        + "m.quantity as quantity, m.price as price " + "from order_menu as m " + "where m.order_id = ?",
                orderId);
        if (null != list && list.size() > 0) {
            for (Map<String, Object> m : list) {
                MenuVo vo = menuDao.findById(((Long) m.get("menuId")).intValue());
                int count = (Integer) m.get("quantity");
                double price = (Double) m.get("price");

                if (count > vo.getRepertory() || price != vo.getPrice()) {
                    executeSql("update `order` as o set o.state = ? where o.id=?", "cancel", orderId);
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 快递员批量确认订单
     *
     * @param orderIdsStr
     * @param courierId
     * @return
     * @throws Exception
     */
    @Override
    public boolean deliveryDoneOrders(String orderIdsStr, Integer courierId) throws Exception {
        String[] orderIds = orderIdsStr.split(",");
        for (int i = 0; i < orderIds.length; i++) {
            if (StringUtils.isNotEmpty(orderIds[i])) {
                int orderId = Integer.parseInt(orderIds[i]);
                boolean result = this.deliveryDone(courierId, orderId);
                if (!result) {
                    throw new BusinessException("批量确认失败！【orderId：" + orderId + "】");
                }
                OrderEntity order = this.get(OrderEntity.class, orderId);
                String source = merchantService.getMerchantSource(order.getMerchant().getId());
                // ===============================第三方Retail超市需求=======================================//
                if (Constants.MERCHANT_SOURCE_RETAIL.equals(source)) {
                    logger.info(">>>>>>>>>>>>>进入一号生活的Retail中间件,进行确认完成订单操作【快递员点确认:deliveryDoneOrders()】,orderId={}", orderId);
                    RetailPortCall.updateOrder(orderId);
                }
                // ===============================第三方Retail超市需求=======================================//
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public OrderVo queryOrderById(Integer orderId, Integer userId) {
        OrderVo vo = orderDao.queryById(orderId, userId);
        if (null != vo) {
            List<OrderMenuVo> menus = orderDao.queryOrderMenusById(orderId);
            vo.setMenus(menus);

            if (OrderStateEnum.DELIVERY.getOrderStateEn().equals(vo.getState())) {
                WUserEntity courier = get(WUserEntity.class, vo.getCourierId());
                vo.setCourierIcon(courier.getPhotoUrl());
                vo.setCouriermobile(courier.getMobile());
                vo.setCourierName(courier.getUsername());
                List<Map<String, Object>> rank = wUserService.getCourierRank(vo.getCourierId(), null, null, false, null,
                        null);
                if (rank.size() > 0)
                    vo.setDeliveryCount(Long.valueOf(rank.get(0).get("total").toString()));
                vo.setScore(commentService.queryCommentScore(0, vo.getCourierId(), Arrays.asList("Y", "S")).getScore());
            }
        }

        return vo;
    }

    /**
     * 获取快递员订单数：我的订单、配送中、已完成、分批送、可抢订单数
     *
     * @param courierId
     * @return
     */
    @Override
    public Map<String, Object> getCourierOrderNumbers(Integer courierId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accept", 0);
        map.put("delivery", 0);
        map.put("delivery_done", 0);
        map.put("splitDelivery", 0);
        // List<Map<String, Object>> list = this.countByStatus(courierId, null);
        List<Map<String, Object>> acceptList = this.countByStatus(courierId, "accept");
        if (acceptList != null && acceptList.size() > 0) {
            Map<String, Object> map1 = acceptList.get(0);
            map.put("accept", map1.get("c"));
        }
        List<Map<String, Object>> deliveryList = this.countByStatus(courierId, "delivery");
        if (deliveryList != null && deliveryList.size() > 0) {
            Map<String, Object> map2 = deliveryList.get(0);
            map.put("delivery", map2.get("c"));
        }
        List<Map<String, Object>> confirmList = this.countByStatus(courierId, "confirm");
        if (confirmList != null && confirmList.size() > 0) {
            Map<String, Object> map3 = confirmList.get(0);
            map.put("delivery_done", map3.get("c"));
        }
        List<Map<String, Object>> splitDeliveryList = this.countByStatus(courierId, "splitDelivery");
        if (splitDeliveryList != null && splitDeliveryList.size() > 0) {
            Map<String, Object> map4 = splitDeliveryList.get(0);
            map.put("splitDelivery", map4.get("c"));
        }

        Integer canScramble = null;
        //是否可以用新的推单算法
        if ("true".equals(isUsingNewAlgorithm)) {
            canScramble = scambleAlgorithmService.canScrambleNum(courierId, false).intValue();
        } else {
            canScramble = pushedOrderService.canScrambleNum(courierId, false).intValue();
        }

        map.put("scramble", canScramble);
        return map;
    }

    @Override
    public void returnCreditAndScore(Integer orderId) {
        boolean update = false;
        // 退还积分和余额
        OrderEntity order = orderDao.get(OrderEntity.class, orderId);
        WUserEntity user = order.getWuser();
        // 余额
        if (order.getCredit() > 0) {
            user.setMoney(user.getMoney() + order.getCredit());
            order.setCredit(0.0);
            update = true;
        }
        // 积分
        if (order.getScore() > 0) {
            user.setScore(user.getScore() + order.getScore());
            order.setScore(0);
            order.setScoreMoney(0.0);
            update = true;
        }

        if (update) {
            this.updateEntitie(order);
            wUserService.updateEntitie(user);
        }
    }

    @Override
    public boolean payOrderByCourier(OrderEntity order, WUserEntity courier, WUserEntity user) throws Exception {
        // step 1 快递员余额扣款
        double needPay = (Math.rint(order.getOrigin() * 100) + Math.rint(order.getDeliveryFee() * 100)
                + Math.rint(order.getCostLunchBox() * 100) - Math.rint(order.getCard() * 100)
                - Math.rint(order.getScoreMoney() * 100));
        double money = Math.rint(courier.getMoney() * 100) - needPay;
        if (money < 0)
            return false;
        needPay /= 100.0;
        // step 2 更新订单状态
        order.setPayState("pay");
        order.setPayTime(DateUtils.getSeconds());
        order.setCredit(needPay);
        order.setPayType("balance");
        updateEntitie(order);
        // step 3 快递员完成配送
        deliveryDone(courier.getId(), order.getId());

        // step 4 写日志
        transfersService.transfersLog(courier, user, needPay, order.getOrderNum());
        orderStateService.deliveryPayOrderState(courier.getUsername(), user.getNickname(), order.getId(), needPay);

        return true;
    }

    @Override
    public String aliPayOrderByCourier(OrderEntity order) throws UnsupportedEncodingException {

        String title = order.getMerchant().getTitle();
        String orderNo = order.getPayId();

        String subject = title;
        String body = title + ",订单号:" + orderNo;

        double needPay = (Math.rint(order.getOrigin() * 100) + Math.rint(order.getDeliveryFee() * 100)
                + Math.rint(order.getCostLunchBox() * 100) - Math.rint(order.getCard() * 100)
                - Math.rint(order.getScoreMoney() * 100));

        needPay /= 100.0;
        return AlipayService.getOrderInfo(subject, body, String.valueOf(needPay), orderNo);
    }

    @Override
    public Map<String, String> weixinPayOrderByCourier(OrderEntity order) throws Exception {
        String outTradeNo = order.getPayId();
        String body = "充值";
        String tradeType = "APP";

        Double needPay = (Math.rint(order.getOrigin() * 100) + Math.rint(order.getDeliveryFee() * 100)
                + Math.rint(order.getCostLunchBox() * 100) - Math.rint(order.getCard() * 100)
                - Math.rint(order.getScoreMoney() * 100));

        return WxPayService.getAppWxPay(ConfigUtil.KDY_APP_ID, ConfigUtil.KDY_MCH_ID, ConfigUtil.KDY_API_KEY, body,
                String.valueOf(needPay.intValue()), outTradeNo, oConvertUtils.getIp(), ConfigUtil.RECHARGE_NOTIFY_URL,
                tradeType);
    }

    @Override
    public List<Map<String, Object>> getOrderNumbersByCourierId(Integer courierId, String state, String beginTime,
                                                                String endTime) {
        String sql = "select COUNT(*) c from `order` where courier_id = ? and state = ? and DATE(FROM_UNIXTIME(pay_time) >= ? and DATE(FROM_UNIXTIME(pay_time) < ?";
        return this.findForJdbc(sql, courierId, state, beginTime, endTime);
    }

    @Override
    public PageList<OrderEntity> findOrderList(OrderEntityVo orderEntity, int page, int rows) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                " select id,pay_id as payId,pay_type as payType,user_id as userId,courier_id as courierId,city_id as cityId,card_id as cardId,status,state,rstate,rereason,retime,realname,mobile,address,"
                        + "online_money as onlineMoney,origin,credit,card,"
                        + "remark,create_time as createTime,pay_time as payTime,comment_content as commentContent,comment_display as commentDisplay,comment_taste as commentTaste,comment_speed as commentSpeed,"
                        + "comment_service as commentService,comment_courier as commentCourier,comment_time as commentTime,merchant_id as merchantId,score_money as scoreMoney,score,order_type as orderType,"
                        + "access_time as accessTime,delivery_time as deliveryTime,complete_time as completeTime,urgent_time as urgentTime,title,ifCourier,delivery_done_time as deliveryDone,pay_state as payState,"
                        + "sale_type as saleType,order_num as orderNum,out_trace_id as outTraceId,time_remark as timeRemark,cook_done_time as cookDoneTime,cook_done_code as cookDoneCode,start_time as startTime,"
                        + "comment_courier_content as commentCourierContent,start_send_time as startSendTime,end_send_time as endSendTime,user_address_id as userAddressId,invoice,from_type as fromType from `order` where 1=1 ");
        if (StringUtil.isNotEmpty(orderEntity.getId())) {
            sql.append(" and id = ");
            sql.append(orderEntity.getId());
        }
        if (StringUtil.isNotEmpty(orderEntity.getPayId())) {
            sql.append(" and pay_id = ");
            sql.append("'");
            sql.append(orderEntity.getPayId());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getPayType())) {
            sql.append(" and pay_type = ");
            sql.append("'");
            sql.append(orderEntity.getPayType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getUserId())) {
            sql.append(" and user_id = ");
            sql.append(orderEntity.getUserId());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCourierId())) {
            sql.append(" and courier_id = ");
            sql.append(orderEntity.getCourierId());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCityId())) {
            sql.append(" and city_id = ");
            sql.append(orderEntity.getCityId());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCardId())) {
            sql.append(" and card_id = ");
            sql.append("'");
            sql.append(orderEntity.getCardId());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getStatus())) {
            sql.append(" and status = ");
            sql.append("'");
            sql.append(orderEntity.getStatus());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getState())) {
            sql.append(" and state = ");
            sql.append("'");
            sql.append(orderEntity.getState());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getRstate())) {
            sql.append(" and rstate = ");
            sql.append("'");
            sql.append(orderEntity.getRstate());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getRetime()) && !orderEntity.getRetime().equals("0")) {
            sql.append(" and retime = ");
            sql.append(orderEntity.getRetime());
        }
        if (StringUtil.isNotEmpty(orderEntity.getRealname())) {
            sql.append(" and realname = ");
            sql.append("'");
            sql.append(orderEntity.getRealname());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getMobile())) {
            sql.append(" and mobile = ");
            sql.append("'");
            sql.append(orderEntity.getMobile());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getAddress())) {
            sql.append(" and address = ");
            sql.append("'");
            sql.append(orderEntity.getAddress());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getOnlineMoney())) {
            sql.append(" and online_money = ");
            sql.append(orderEntity.getOnlineMoney());
        }
        if (StringUtil.isNotEmpty(orderEntity.getOrigin())) {
            sql.append(" and origin = ");
            sql.append(orderEntity.getOrigin());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCredit())) {
            sql.append(" and credit = ");
            sql.append(orderEntity.getCredit());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCard())) {
            sql.append(" and card = ");
            sql.append(orderEntity.getCard());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCreateTime()) && !orderEntity.getCreateTime().equals("0")) {
            sql.append(" and create_time = ");
            sql.append(orderEntity.getCreateTime());
        }
        if (StringUtil.isNotEmpty(orderEntity.getPayTime()) && !orderEntity.getPayTime().equals("0")) {
            sql.append(" and pay_time = ");
            sql.append(orderEntity.getPayTime());
        }
        if (StringUtil.isNotEmpty(orderEntity.getMerchantId())) {
            sql.append(" and merchant_id = ");
            sql.append(orderEntity.getMerchantId());
        }
        if (StringUtil.isNotEmpty(orderEntity.getOrderType())) {
            sql.append(" and order_type = ");
            sql.append("'");
            sql.append(orderEntity.getOrderType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getIfCourier())) {
            sql.append(" and ifCourier = ");
            sql.append("'");
            sql.append(orderEntity.getIfCourier());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getPayState())) {
            sql.append(" and pay_state = ");
            sql.append("'");
            sql.append(orderEntity.getPayState());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getSaleType())) {
            sql.append(" and sale_type = ");
            sql.append("'");
            sql.append(orderEntity.getSaleType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getOrderNum())) {
            sql.append(" and order_num = ");
            sql.append("'");
            sql.append(orderEntity.getOrderNum());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getOutTraceId())) {
            sql.append(" and out_trace_id = ");
            sql.append("'");
            sql.append(orderEntity.getOutTraceId());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getFromType())) {
            sql.append(" and from_type = ");
            sql.append("'");
            sql.append(orderEntity.getFromType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getCompleteStartTime())) {
            sql.append(" and DATE(FROM_UNIXTIME(complete_time)) >= ");
            sql.append("'");
            sql.append(orderEntity.getCompleteStartTime());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getCompleteEndTime())) {
            sql.append(" and DATE(FROM_UNIXTIME(complete_time)) <= ");
            sql.append("'");
            sql.append(orderEntity.getCompleteEndTime());
            sql.append("'");
        }
        List<OrderEntity> list = this.findObjForJdbc(sql.toString(), page, rows, OrderEntity.class);
        Dialect dialect = new Dialect();
        String countSql = dialect.getCountString(sql.toString());
        List<Map<String, Object>> countList = this.findForJdbc(countSql);
        Map<String, Object> map = countList.get(0);
        String count = map.get("count").toString();
        PageList<OrderEntity> pageList = new PageList<OrderEntity>(rows, page, Integer.parseInt(count), list);
        return pageList;
    }

    @Override
    public List<OrderEntityVo> findOrderList(OrderEntityVo orderEntity) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                " select id,pay_id as payId,pay_type as payType,user_id as userId,courier_id as courierId,city_id as cityId,card_id as cardId,status,state,rstate,rereason,retime,realname,mobile,address,"
                        + "online_money as onlineMoney,origin,credit,card,"
                        + "remark,create_time as createTime,pay_time as payTime,comment_content as commentContent,comment_display as commentDisplay,comment_taste as commentTaste,comment_speed as commentSpeed,"
                        + "comment_service as commentService,comment_courier as commentCourier,comment_time as commentTime,merchant_id as merchantId,score_money as scoreMoney,score,order_type as orderType,"
                        + "access_time as accessTime,delivery_time as deliveryTime,complete_time as completeTime,urgent_time as urgentTime,title,ifCourier,delivery_done_time as deliveryDone,pay_state as payState,"
                        + "sale_type as saleType,order_num as orderNum,out_trace_id as outTraceId,time_remark as timeRemark,cook_done_time as cookDoneTime,cook_done_code as cookDoneCode,start_time as startTime,"
                        + "comment_courier_content as commentCourierContent,start_send_time as startSendTime,end_send_time as endSendTime,user_address_id as userAddressId,invoice,from_type as fromType "
                        + "from `order` where 1=1 ");
        if (StringUtil.isNotEmpty(orderEntity.getId())) {
            sql.append(" and id = ");
            sql.append(orderEntity.getId());
        }
        if (StringUtil.isNotEmpty(orderEntity.getPayId())) {
            sql.append(" and pay_id = ");
            sql.append("'");
            sql.append(orderEntity.getPayId());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getPayType())) {
            sql.append(" and pay_type = ");
            sql.append("'");
            sql.append(orderEntity.getPayType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getUserId())) {
            sql.append(" and user_id = ");
            sql.append(orderEntity.getUserId());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCourierId())) {
            sql.append(" and courier_id = ");
            sql.append(orderEntity.getCourierId());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCityId())) {
            sql.append(" and city_id = ");
            sql.append(orderEntity.getCityId());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCardId())) {
            sql.append(" and card_id = ");
            sql.append("'");
            sql.append(orderEntity.getCardId());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getStatus())) {
            sql.append(" and status = ");
            sql.append("'");
            sql.append(orderEntity.getStatus());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getState())) {
            sql.append(" and state = ");
            sql.append("'");
            sql.append(orderEntity.getState());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getRstate())) {
            sql.append(" and rstate = ");
            sql.append("'");
            sql.append(orderEntity.getRstate());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getRetime()) && !orderEntity.getRetime().equals("0")) {
            sql.append(" and retime = ");
            sql.append(orderEntity.getRetime());
        }
        if (StringUtil.isNotEmpty(orderEntity.getRealname())) {
            sql.append(" and realname = ");
            sql.append("'");
            sql.append(orderEntity.getRealname());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getMobile())) {
            sql.append(" and mobile = ");
            sql.append("'");
            sql.append(orderEntity.getMobile());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getAddress())) {
            sql.append(" and address = ");
            sql.append("'");
            sql.append(orderEntity.getAddress());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getOnlineMoney())) {
            sql.append(" and online_money = ");
            sql.append(orderEntity.getOnlineMoney());
        }
        if (StringUtil.isNotEmpty(orderEntity.getOrigin())) {
            sql.append(" and origin = ");
            sql.append(orderEntity.getOrigin());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCredit())) {
            sql.append(" and credit = ");
            sql.append(orderEntity.getCredit());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCard())) {
            sql.append(" and card = ");
            sql.append(orderEntity.getCard());
        }
        if (StringUtil.isNotEmpty(orderEntity.getCreateTime()) && !orderEntity.getCreateTime().equals("0")) {
            sql.append(" and create_time = ");
            sql.append(orderEntity.getCreateTime());
        }
        if (StringUtil.isNotEmpty(orderEntity.getPayTime()) && !orderEntity.getPayTime().equals("0")) {
            sql.append(" and pay_time = ");
            sql.append(orderEntity.getPayTime());
        }
        if (StringUtil.isNotEmpty(orderEntity.getMerchantId())) {
            sql.append(" and merchant_id = ");
            sql.append(orderEntity.getMerchantId());
        }
        if (StringUtil.isNotEmpty(orderEntity.getOrderType())) {
            sql.append(" and order_type = ");
            sql.append("'");
            sql.append(orderEntity.getOrderType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getIfCourier())) {
            sql.append(" and ifCourier = ");
            sql.append("'");
            sql.append(orderEntity.getIfCourier());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getPayState())) {
            sql.append(" and pay_state = ");
            sql.append("'");
            sql.append(orderEntity.getPayState());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getSaleType())) {
            sql.append(" and sale_type = ");
            sql.append("'");
            sql.append(orderEntity.getSaleType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getOrderNum())) {
            sql.append(" and order_num = ");
            sql.append("'");
            sql.append(orderEntity.getOrderNum());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getOutTraceId())) {
            sql.append(" and out_trace_id = ");
            sql.append("'");
            sql.append(orderEntity.getOutTraceId());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getFromType())) {
            sql.append(" and from_type = ");
            sql.append("'");
            sql.append(orderEntity.getFromType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getCompleteStartTime())) {
            sql.append(" and DATE(FROM_UNIXTIME(complete_time)) >= ");
            sql.append("'");
            sql.append(orderEntity.getCompleteStartTime());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(orderEntity.getCompleteEndTime())) {
            sql.append(" and DATE(FROM_UNIXTIME(complete_time)) <= ");
            sql.append("'");
            sql.append(orderEntity.getCompleteEndTime());
            sql.append("'");
        }
        List<OrderEntityVo> list = this.findObjForJdbc(sql.toString(), OrderEntityVo.class);
        return list;
    }

    public List<OrderEntityVo> statisticsOrderRanking(OrderEntityVo order) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                " SELECT m.title,o.realname,count(*) as orderCount FROM `order` o,merchant m where 1=1 and o.merchant_id = m.id  ");
        if (StringUtil.isNotEmpty(order.getId())) {
            sql.append(" and id = ");
            sql.append(order.getId());
        }
        if (StringUtil.isNotEmpty(order.getPayId())) {
            sql.append(" and pay_id = ");
            sql.append("'");
            sql.append(order.getPayId());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getPayType())) {
            sql.append(" and pay_type = ");
            sql.append("'");
            sql.append(order.getPayType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getUserId())) {
            sql.append(" and user_id = ");
            sql.append(order.getUserId());
        }
        if (StringUtil.isNotEmpty(order.getCourierId())) {
            sql.append(" and courier_id = ");
            sql.append(order.getCourierId());
        }
        if (StringUtil.isNotEmpty(order.getCityId())) {
            sql.append(" and city_id = ");
            sql.append(order.getCityId());
        }
        if (StringUtil.isNotEmpty(order.getCardId())) {
            sql.append(" and card_id = ");
            sql.append("'");
            sql.append(order.getCardId());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getStatus())) {
            sql.append(" and status = ");
            sql.append("'");
            sql.append(order.getStatus());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getState())) {
            sql.append(" and state = ");
            sql.append("'");
            sql.append(order.getState());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getRstate())) {
            sql.append(" and rstate = ");
            sql.append("'");
            sql.append(order.getRstate());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getRetime()) && !order.getRetime().equals("0")) {
            sql.append(" and retime = ");
            sql.append(order.getRetime());
        }
        if (StringUtil.isNotEmpty(order.getRealname())) {
            sql.append(" and realname = ");
            sql.append("'");
            sql.append(order.getRealname());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getMobile())) {
            sql.append(" and mobile = ");
            sql.append("'");
            sql.append(order.getMobile());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getAddress())) {
            sql.append(" and address = ");
            sql.append("'");
            sql.append(order.getAddress());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getOnlineMoney())) {
            sql.append(" and online_money = ");
            sql.append(order.getOnlineMoney());
        }
        if (StringUtil.isNotEmpty(order.getOrigin())) {
            sql.append(" and origin = ");
            sql.append(order.getOrigin());
        }
        if (StringUtil.isNotEmpty(order.getCredit())) {
            sql.append(" and credit = ");
            sql.append(order.getCredit());
        }
        if (StringUtil.isNotEmpty(order.getCard())) {
            sql.append(" and card = ");
            sql.append(order.getCard());
        }
        if (StringUtil.isNotEmpty(order.getCreateTime()) && !order.getCreateTime().equals("0")) {
            sql.append(" and create_time = ");
            sql.append(order.getCreateTime());
        }
        if (StringUtil.isNotEmpty(order.getPayTime()) && !order.getPayTime().equals("0")) {
            sql.append(" and pay_time = ");
            sql.append(order.getPayTime());
        }
        if (StringUtil.isNotEmpty(order.getMerchantId())) {
            sql.append(" and merchant_id = ");
            sql.append(order.getMerchantId());
        }
        if (StringUtil.isNotEmpty(order.getOrderType())) {
            sql.append(" and order_type = ");
            sql.append("'");
            sql.append(order.getOrderType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getIfCourier())) {
            sql.append(" and ifCourier = ");
            sql.append("'");
            sql.append(order.getIfCourier());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getPayState())) {
            sql.append(" and pay_state = ");
            sql.append("'");
            sql.append(order.getPayState());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getSaleType())) {
            sql.append(" and sale_type = ");
            sql.append("'");
            sql.append(order.getSaleType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getOrderNum())) {
            sql.append(" and order_num = ");
            sql.append("'");
            sql.append(order.getOrderNum());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getOutTraceId())) {
            sql.append(" and out_trace_id = ");
            sql.append("'");
            sql.append(order.getOutTraceId());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getFromType())) {
            sql.append(" and from_type = ");
            sql.append("'");
            sql.append(order.getFromType());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getCompleteStartTime())) {
            sql.append(" and DATE(FROM_UNIXTIME(complete_time)) >= ");
            sql.append("'");
            sql.append(order.getCompleteStartTime());
            sql.append("'");
        }
        if (StringUtil.isNotEmpty(order.getCompleteEndTime())) {
            sql.append(" and DATE(FROM_UNIXTIME(complete_time)) <= ");
            sql.append("'");
            sql.append(order.getCompleteEndTime());
            sql.append("'");
        }
        List<OrderEntityVo> list = this.findObjForJdbc(sql.toString(), OrderEntityVo.class);
        return list;
    }

    @Override
    public AddressDetailVo queryLastedRestaurantUserInfo(Integer userId) {
        return orderDao.queryLastedUserInfo(userId, 2);
    }

    @Override
    public Integer createQcCodeOrder(String payId, WUserEntity user, String merchantId, Double checkCost,
                                     String saleType, String payType) {
        String sql = "insert into `order` (pay_id, user_id, city_id, merchant_id, state, origin, create_time,"
                + " order_type, sale_type, pay_type, is_merchant_delivery) "
                + " values(?, ?, 0, ?, 'unpay', ?, unix_timestamp(), ?, ?, ?,'courier') ";

        this.executeSql(sql, payId, user.getId(), merchantId, checkCost, "scan_order", saleType, payType);
        String sqlId = "select last_insert_id() as orderId";
        return findOneForJdbc(sqlId, Integer.class);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        String sql = "DELETE FROM  `order` WHERE id =?";
        executeSql(sql, orderId);
    }


    /**
     * 私厨15分钟内定时器退单信息
     */
    @Override
    public List<Map<String, Object>> getOrderTimer() {
        String sql = "select id, order_id orderId , merchant_id merchantId , "
                + " from_unixtime(create_time) createTime, from_type fromType from tom_order_timer";
        return this.findForJdbc(sql);
    }

    /**
     * 删除私厨15分钟内定时器已完成退单
     */
    @Override
    public void deleteOrderTimer(int id) {
        String sql = "delete from tom_order_timer where id = ?";
        this.executeSql(sql, id);
    }

    /*
     * 根据orderId删除退款定时器tom_order_timer的记录
     */
    public int deleteOrderTimerByOrderId(Integer orderId) {
        return this.executeSql(delOrderTimerSql, orderId);
    }

    @Override
    public AjaxJson createCrowdsourcingOrder(Integer id, String password, Double longitude, Double latitude,
                                             String realname, String mobile, String address, String remark, Integer crowdId) throws Exception {
        AjaxJson aj = new AjaxJson();
        MerchantEntity merchant = this.get(MerchantEntity.class, id);
        if (merchant == null) {
            aj.setMsg("商家不存在");
            aj.setStateCode("01");
            aj.setSuccess(false);
            return aj;
        }
        WUserEntity user = merchant.getWuser();

        // 验证支付密码
        if (password.equals(user.getPayPassword())) {

            // 查询众包配送费
            Map<String, Object> crowdMap = this
                    .findOneForJdbc("select id id,name name,delivery_fee deliveryFee,courier_deduct courierDeduct "
                            + "from tom_crowdsourcing_type where id=?", crowdId);
            if (crowdMap == null) {
                aj.setMsg("查询众包配送费为空");
                aj.setStateCode("01");
                aj.setSuccess(false);
                return aj;
            }
            Double deliveryFee = Double.parseDouble(crowdMap.get("deliveryFee").toString());
            Double fee = deliveryFee / 100;

            // 保存订单
            OrderEntity entity = new OrderEntity();
            entity.setRealname(realname);
            entity.setMobile(mobile);
            entity.setAddress(address);
            entity.setRemark(remark);
            entity.setMerchant(merchant);
            entity.setState("accept");
            entity.setPayState("pay");
            entity.setCreateTime(DateUtils.getSeconds());
            entity.setFromType("crowdsourcing");
            entity.setCourierId(0);
            entity.setAddrId(0);
            entity.setCityId(0);
            entity.setRstate("normal");
            entity.setOnlineMoney(0.00);
            entity.setOrigin(0.00);
            entity.setCredit(fee);
            entity.setCard(0.00);
            entity.setPayTime(DateUtils.getSeconds());
            entity.setCommentDisplay("Y");
            entity.setDeliveryFee(fee);
            entity.setPayType("balance");
            entity.setWuser(user);
            entity.setCostLunchBox(0.00);
            entity.setAccessTime(DateUtils.getSeconds());
            String timeRemark = DateTime.now().toString("HH:mm") + "-"
                    + DateTime.now().plusMinutes(30).toString("HH:mm");
            entity.setTimeRemark(timeRemark);
            entity.setMemberDiscountMoney(0.00);
            entity.setMerchantMemberDiscountMoney(0.00);
            entity.setDineInDiscountMoney(0.00);
            entity.setRechargeSrc("0");
            entity.setIsMerchantDelivery("courier");
            entity.setFlashOrderId(-1L);
            this.save(entity);

            // 获取刚刚生成的订单id
            Integer orderId = entity.getId();

            // 生成支付id:pay_id
            long time = System.currentTimeMillis();
            String payid = RandomStringUtils.random(4, "0123456789") + Long.toString(time + orderId).substring(2);
            entity.setPayId(payid);
            this.saveOrUpdate(entity);

            // 商家支付配送费用
            flowService.merchantCrowdsourcing(user, fee, orderId);

            // 将订单信息放入缓存表
            String sql = "INSERT INTO tom_order_timer(merchant_id,order_id,create_time,from_type) VALUES(?,?,?,?)";
            this.executeSql(sql, id, orderId, entity.getCreateTime(), 0);

            // 订单生成排号
            this.setOrderNum(entity);

            // 订单保存入order_menu
            this.executeSql("INSERT INTO order_menu (order_id,menu_id,quantity,price) "
                    + "VALUES(?,(SELECT id FROM menu WHERE is_delete='Y' LIMIT 0,1),0,0)", orderId);

            // 查询众包类型表中信息
            String name = crowdMap.get("name").toString();
            Integer deliveryfee = deliveryFee.intValue();
            Integer courierDeduct = Integer.parseInt(crowdMap.get("courierDeduct").toString());

            // 保存订单到众包表
            TomOrderCrowdsourcingEntity t = new TomOrderCrowdsourcingEntity();
            t.setCrowdsourcingName(name);
            t.setOrderId(orderId);
            t.setCrowdsourcingDeliveryFee(deliveryfee);
            t.setCrowdsourcingCourierDeduct(courierDeduct);
            t.setLongitude(longitude);
            t.setLatitude(latitude);
            this.save(t);

            // 更新订单状态记录表
            orderStateService.accessOrderState(orderId);

            // 快递员抢单
            this.pushOrder(orderId);
            aj.setMsg("下单成功，快递员们正在抢单");
            aj.setStateCode("00");
            aj.setSuccess(true);
            return aj;
        } else {
            aj.setMsg("支付密码错误");
            aj.setStateCode("01");
            aj.setSuccess(false);
            return aj;

        }
    }

    @Override
    public List<Map<String, Object>> getCrowdsourcingOrders(Integer merchantId, Integer page, Integer rows) {

        String sql = "select o.id id,o.pay_id payId,o.realname realname,o.mobile mobile,o.address address,courier_id courierId,"
                + "(SELECT mobile courierMobile from `user` where id=courier_id) courierMobile,"
                + "o.remark remark,o.state state,RIGHT(o.order_num,4) orderNum,t.crowdsourcing_name type,"
                + "FROM_UNIXTIME(o.create_time,'%m-%d %H:%i') createTime,"// 创建时间
                + "CONVERT((o.complete_time-o.delivery_time)/60,DECIMAL) time " // 订单完成总时间
                + "from `order` o , " + "tom_order_crowdsourcing t  " + "where o.id=t.order_id  "
                + "and o.merchant_id=? " + "order by o.create_time desc";

        List<Map<String, Object>> orders = this.findForJdbcParam(sql, page, rows, merchantId);
        return orders;
    }

    @Override
    public AjaxJson cancelCrowdsourcingOrder(Integer orderId, Integer merchantId) throws Exception {
        AjaxJson aj = new AjaxJson();
        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (order == null) {
            aj.setMsg("订单不存在");
            aj.setStateCode("01");
            aj.setSuccess(false);
            return aj;
        }
        deleteOrderTimerByOrderId(orderId);
        MerchantEntity merchant = order.getMerchant();
        if (merchant == null) {
            aj.setMsg("商家不存在");
            aj.setStateCode("01");
            aj.setSuccess(false);
            return aj;
        } else {
            if (order.getState().equals("delivery")) {
                aj.setMsg("订单正在配送中，不能取消！");
                aj.setStateCode("01");
                aj.setSuccess(false);
                return aj;
            }
            if (order.getState().equals("accept")) {

                if (order.getPayState().equals("pay")) {
                    order.setRstate("berefund");// 退款状态
                    order.setAccessTime(DateUtils.getSeconds());
                    order.setState("cancel");// 订单取消状态
                    this.saveOrUpdate(order);
                    orderStateService.noAcceptOrderState(orderId);

                    WUserEntity user = merchant.getWuser();

                    // 运费退回给商家
                    flowService.crowdsourcingRefund(user, orderId);

                    aj.setMsg("订单取消成功");
                    aj.setStateCode("00");
                    aj.setSuccess(true);
                    logger.info("订单：" + orderId + "取消且退款成功");
                    return aj;
                } else {
                    order.setAccessTime(DateUtils.getSeconds());
                    order.setState("cancel");
                    this.saveOrUpdate(order);
                    orderStateService.noAcceptOrderState(orderId);

                    aj.setMsg("订单取消成功");
                    aj.setStateCode("00");
                    aj.setSuccess(true);
                    logger.info("订单：" + orderId + "取消成功");
                    return aj;
                }
            } else {
                aj.setMsg("订单状态不可取消");
                aj.setStateCode("01");
                aj.setSuccess(false);
                logger.info("订单：" + orderId + "状态不可取消");
                return aj;
            }
        }
    }

    @Override
    public Boolean autoCancelCrowdsourcingOrder(Integer orderId, Integer merchantId) throws Exception {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (order == null) {
            logger.info("订单不存在");
            return false;
        }
        MerchantEntity merchant = order.getMerchant();
        if (merchant != null) {
            if (order.getState().equals("accept") && order.getCourierId() == 0) {
                WUserEntity user = merchant.getWuser();

                if (order.getPayState().equals("pay")) {
                    order.setRstate("berefund");// 退款状态
                    order.setAccessTime(DateUtils.getSeconds());
                    order.setState("cancel");// 订单取消状态
                    this.saveOrUpdate(order);
                    orderStateService.noAcceptOrderState(orderId);

                    // 运费退回给商家
                    flowService.crowdsourcingRefund(user, orderId);
                    logger.info("订单：" + orderId + "自动取消且退款成功");
                } else {
                    order.setAccessTime(DateUtils.getSeconds());
                    order.setState("cancel");
                    this.saveOrUpdate(order);
                    orderStateService.noAcceptOrderState(orderId);
                    logger.info("订单：" + orderId + "自动取消成功");
                }
                return true;
            } else {
                // 清除订单缓存表中非待接单的记录
                if (order.getCourierId() != 0) {
                    logger.info("订单：" + orderId + "从缓存表tom_order_timer移除成功");
                    deleteOrderTimerByOrderId(orderId);
                }
                return false;
            }
        } else {
            logger.info("商家不存在");
            return false;
        }
    }

    @Override
    public Integer getOrderNumber(Integer courierId, String time) {
        StringBuilder query = new StringBuilder();
        query.append(" select count(id) ");
        query.append(" from `order`");
        query.append("  where ");
        query.append(" courier_id = ?");
        query.append(" and  ");
        query.append("  ( state = 'accept' or state = 'delivery' ) ");
        query.append("  AND DATE(FROM_UNIXTIME(create_time)) = ? ");
        query.append("  AND from_type <> 'supplychain' ");
        return findOneForJdbc(query.toString(), Integer.class, courierId, time);
    }

    @Override
    public Map<String, Object> getMerchantOrderCount(Integer merchantId, Integer num, Integer page, Integer rows) {
        String sqlStart = null;
        String sql = "SELECT date,money,quantity FROM tom_merchant_order_statistics WHERE merchant_id=? ";
        switch (num) {
            case 0: // 商家全部完成订单总数
                sqlStart = "SELECT SUM(IF(state='confirm',1,0)) sum,";
                break;
            case 1: // 商家近1个月内完成订单总数
                sqlStart = "SELECT SUM(IF(state='confirm',1,0) && "
                        + "create_time>UNIX_TIMESTAMP(DATE_ADD(CURDATE(),INTERVAL -1 DAY))) sum,";
                sql += "AND date>DATE_ADD(CURDATE(),INTERVAL -1 DAY) ";
                break;
            case 15: // 商家近15天内完成订单总数
                sqlStart = "SELECT SUM(IF(state='confirm',1,0) && "
                        + "create_time>UNIX_TIMESTAMP(DATE_ADD(CURDATE(),INTERVAL -15 DAY))) sum,";
                sql += "AND date>DATE_ADD(CURDATE(),INTERVAL -15 DAY) ";
                break;
            default:
                break;
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (sqlStart != null) {
            sqlStart += "SUM(IF(DATE(FROM_UNIXTIME(create_time))=CURDATE() && state='confirm' && rstate='normal',1,0) ) confirm, "
                    + "SUM(IF(DATE(FROM_UNIXTIME(create_time))=CURDATE(),1,0) &&  rstate = 'berefund') berefund "
                    + "FROM `order` where merchant_id=?";
            sql += " ORDER BY date DESC";

            map = this.findOneForJdbc(sqlStart, merchantId); // 返回订单总数、今日完成订单、今日退单
            MapUtil.getMapToBean(map);
            list = this.findForJdbcParam(sql, page, rows, merchantId);
            if (list != null && list.size() > 0) {
                map.put("list", list); // list返回每日订单完成总数、订单扣点后总营收
            }
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> getMerchantDayOrders(Integer merchantId, String time, Integer page, Integer rows) {
        // 查询当日订单
        String sql = "SELECT id,pay_id payId,state,rstate,from_type fromType,origin,delivery_fee deliveryFee "
                + "FROM `order` WHERE DATE(FROM_UNIXTIME(create_time))=? "
                + "AND merchant_id=? ORDER BY create_time DESC ";
        List<Map<String, Object>> list = this.findForJdbcParam(sql, page, rows, time, merchantId);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map = list.get(i);
                Integer orderId = Integer.parseInt(map.get("id").toString());
                OrderEntity order = this.get(OrderEntity.class, orderId);
                if (order != null) {
                    // 判断是否为众包订单
                    String fromType = order.getFromType();
                    if (fromType.equals("crowdsourcing")) {
                        // map.put("menuList", null);
                    } else {
                        // 查询非众包订单的菜品信息
                        sql = "SELECT quantity,menu_id menuId FROM order_menu WHERE order_id=?";
                        List<Map<String, Object>> omList = this.findForJdbc(sql, orderId);
                        if (omList != null && omList.size() > 0) {
                            for (int j = 0; j < omList.size(); j++) {
                                Map<String, Object> menu = omList.get(j);
                                Integer menuId = Integer.parseInt(menu.get("menuId").toString());
                                MenuEntity menuE = this.get(MenuEntity.class, menuId);
                                String name = menuE.getName();
                                menu.put("name", j + 1 + "." + name);
                            }
                            map.put("menuList", omList);
                        } else {
                            // map.put("menuList", null);
                        }
                    }
                }
            }
            return list;
        } else {
            return new ArrayList<Map<String, Object>>();
        }
    }

    @Override
    public List<Map<String, Object>> selectMerchantOrdersByValue(Integer merchantId, String value, Integer page,
                                                                 Integer rows) {
        String sql = "SELECT o.id,m.title,o.state,o.rstate,o.pay_id,o.pay_state, "
                + "FROM_UNIXTIME(o.create_time,'%Y-%m-%d %H:%i') create_time,o.order_type, "
                + "o.sale_type,o.time_remark,SUBSTR(o.order_num FROM 9) order_num  "
                + "FROM `order` o LEFT JOIN `merchant` m ON o.merchant_id=m.id  " + "WHERE o.merchant_id=? "
                + "AND FROM_UNIXTIME(o.create_time,'%Y-%m-%d')=CURDATE()  " + "AND o.state NOT IN ('unpay','cancel') "
                + "AND o.from_type NOT IN ('crowdsourcing') "
                + "AND o.order_type NOT IN ('merchant_recharge','recharge') "
                + "AND (o.pay_id LIKE ? OR SUBSTR(o.order_num FROM 9) LIKE ?) " + "ORDER BY o.create_time DESC";
        List<Map<String, Object>> list = this.findForJdbcParam(sql, page, rows, merchantId, "%" + value + "%",
                "%" + value + "%");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                Object obj = map.get("id");
                if (obj != null) {
                    Integer orderId = Integer.valueOf(obj.toString());
                    OrderEntity order = this.get(OrderEntity.class, orderId);
                    if (order != null) {
                        // 查询订单实际支付金额
                        String orderRealMoney = getOrderRealMoney(order);
                        map.put("orderRealMoney", orderRealMoney);
                        // 判断app首页界面状态(待处理、处理中、已完成)
                        String state = map.get("state").toString();
                        if ("accept".equals(state) || "delivery".equals(state)) {
                            map.put("order_state", "accept");
                        } else if ("done".equals(state) || "confirm".equals(state) || "delivery_done".equals(state)
                                || "evaluated".equals(state)) {
                            map.put("order_state", "done");
                        } else if ("unaccept".equals(state) || "refund".equals(state)) {
                            map.put("order_state", "unaccept");
                        } else {
                            map.put("order_state", state);
                        }
                    } else {
                        logger.info("订单orderId:[" + orderId + "不存在");
                        map.put("orderRealMoney", "");
                    }
                }
            }
        }
        return list;
    }


    /**
     * 根据商家id查看该商家的订单是否有快递员接单
     *
     * @param mercahntId
     * @return
     */
    @Override
    public boolean exsistsCanReceiveCouriers(Integer mercahntId) {
        boolean exsists = false;

        List<Integer> courierIds = new ArrayList<Integer>();

        // 查找商家绑定的快递员
        StringBuilder query = new StringBuilder();
        query.append(" select cm.courier_id courierId ");
        query.append(" from 0085_courier_merchant cm");
        query.append(" where cm.merchant_id = ? ");
        List<Map<String, Object>> courierIdsList = findForJdbc(query.toString(), mercahntId);
        if (CollectionUtils.isNotEmpty(courierIdsList)) {
            for (Map<String, Object> map : courierIdsList) {
                courierIds.add(Integer.parseInt(map.get("courierId").toString()));
            }
        }

        int radis = 1500;
        try {
            radis = Integer.parseInt(systemconfigService.getValByCode("push_order_distance"));
        } catch (Exception e) {
            logger.error("获取众包快递员推单距离（单位：米）设置失败，设置为默认值1500米");
        }
        MerchantEntity merchantEntity = this.get(MerchantEntity.class, mercahntId);

        // 获取众包快递员
        List<Integer> crowdSourcingCourierIds = scambleAlgorithmService.findNearestCouiriers(merchantEntity.getLng(),
                merchantEntity.getLat(), radis, Integer.MAX_VALUE);
        if (CollectionUtils.isNotEmpty(crowdSourcingCourierIds)) {
            courierIds.addAll(crowdSourcingCourierIds);
        }

        // 只要有一个快递员上班就返回true
        for (Integer courierId : courierIds) {
            boolean onduty = attendanceService.isOnDuty(courierId);
            if (onduty == true) {
                exsists = true;
                break;
            }
        }
        return exsists;
    }

    @Override
    public List<Map<String, Object>> getMerchantTypeAndOrderQuantityBycourierId(Integer courierId, String date) {
        StringBuilder query = new StringBuilder();
        query.append(" select creator, count(o.id) orders from `order` o");
        query.append(" left join merchant m on m.id = o.merchant_id ");
        query.append(" left join `user` u on u.id = m.user_id");
        query.append(" where o.courier_id = ? and o.from_type <> 'crowdsourcing' and o.from_type <> 'supplychain' ");
        query.append(" and date(from_unixtime(o.create_time))=? and date(from_unixtime(o.complete_time))=? and o.rstate<>'berefund'");
        query.append(" group  by creator");
        return findForJdbc(query.toString(), courierId, date, date);
    }

    @Override
    public int createLogisticsByMerchant(Integer orderId, String logisticsName, String logisticsNumber, String logisticsSnapshot) {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (order != null) {
            LogisticsEntity logistics = new LogisticsEntity();
            logistics.setOrderId(orderId);
            logistics.setLogisticsName(logisticsName);
            logistics.setLogisticsNumber(logisticsNumber);
            logistics.setLogisticsSnapshot(logisticsSnapshot);
            logistics.setCreateById(order.getMerchant().getId());
            logistics.setType('0');
            logistics.setCreateTime(DateUtils.getTimestamp());
            this.saveOrUpdate(logistics);
            order.setState("delivery");
            order.setAccessTime(DateUtils.getSeconds());
            this.updateEntitie(order);
            orderStateService.accessOrderState(orderId);//添加订单状态记录
            //推送消息给用户：订单发货通知
            messageService.flashSendMessage(order, "delivery");
            return 1;
        } else {
            logger.error("订单orderId[" + orderId + "]不存在！！！");
            return 0;
        }
    }

    @Override
    public Map<String, Object> getRefundDetail(OrderEntity order, String state) {//详情状态： 1 退款       2 退货
        Map<String, Object> map = new HashMap<String, Object>();
        if ("1".equals(state)) {//1 退款
            FlashOrderReturnEntity flash = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", order.getId());
            if ('1' == flash.getType()) {//0:退款,1:退货退款
                map.put("state", "退款类型：退货退款");
            } else if ('0' == flash.getType()) {
                map.put("state", "退款类型：仅退款");
            }
            map.put("refund_reason", StringUtil.isEmpty(flash.getRefundDesc()) ? "" : flash.getRefundDesc());
            map.put("refund_money", String.format("%.2f", flash.getRefundAmount()));
            String refundImg = flash.getRefundImg();
            List<String> l = new ArrayList<String>();
            if (refundImg != null && refundImg.length() > 0) {
                String[] img = refundImg.split(";");
                if (img.length > 1) {
                    for (String s : img) {
                        l.add(s);
                    }
                } else {
                    l.add(img[0]);
                }
            }
            map.put("refund_img", l);
        } else if ("2".equals(state)) {//  2 退货
            //物流信息类型 0:商家发货,1:买家退货
            LogisticsEntity logistics = this.getLogisticsByOrderId(order.getId(), "1");
            List<String> l = new ArrayList<String>();
            if (logistics != null) {
                map.put("logistics_name", logistics.getLogisticsName());
                map.put("logistics_number", logistics.getLogisticsNumber());
                String logisticsSnapshot = logistics.getLogisticsSnapshot();
                if (logisticsSnapshot != null && logisticsSnapshot.length() > 0) {
                    String[] snapshot = logisticsSnapshot.split(";");
                    if (snapshot.length > 1) {
                        for (String s : snapshot) {
                            l.add(s);
                        }
                    } else {
                        l.add(snapshot[0]);
                    }
                }
            } else {
                map.put("logistics_name", "用户未发货");
                map.put("logistics_number", "");
            }
            map.put("logistics_snapshot", l);
        }
        return map;
    }

    @Override
    public String flashOrderAcceptRefundApply(Integer orderId) {
        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (order == null) {
            this.deleteOrderTimerByOrderId(orderId);//删除自动退款的定时记录
            logger.info("找不到订单orderId:[" + orderId + "]相关信息");
            return "";
        }
        order.setRstate("acceptRefundApply");//修改订单退款状态为同意申请
        this.updateEntitie(order);
        orderStateService.acceptRefundOrderState(orderId);//添加订单同意申请记录
        this.deleteOrderTimerByOrderId(orderId);//删除自动退款的定时记录

        Double money = 0d;

        //推送消息给用户
        messageService.flashSendMessage(order, "returnproductapply");
        FlashOrderReturnEntity flash = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", orderId);
        if (flash != null) {
            money = flash.getRefundAmount();
        }
        return String.format("%.2f", money);
    }


    public LogisticsEntity getLogisticsByOrderId(Integer orderId, String type) {
        String sql = "select id from logistics where order_id=? and type=? ";
        List<Map<String, Object>> list = this.findForJdbc(sql, orderId, type);
        if (list != null && list.size() > 0) {
            Map<String, Object> map = list.get(0);
            Long id = Long.valueOf(map.get("id").toString());
            LogisticsEntity logistics = this.get(LogisticsEntity.class, id);
            return logistics;
        } else {
            return null;
        }
    }

    @Override
    public void flashOrderUnAcceptRefundApply(Integer orderId) {

        this.unacceptRefund(orderId);//商家拒绝申请退款/退货退款

        OrderEntity order = this.get(OrderEntity.class, orderId);
        if (order == null) {
            logger.error("闪购订单拒绝退款/退款退货申请失败，找不到orderId[" + orderId + "]相关的订单信息");
            return;
        }

        FlashOrderReturnEntity flash = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", orderId);
        if (flash != null) {
            //退款类型      0:退款,1:退货退款
            String stateCode = "refundapply";
            if ('0' == flash.getType()) {//退款类型
                LogisticsEntity logistics = this.getLogisticsByOrderId(orderId, "0");//物流信息类型 0:商家发货,1:买家退货
                if (logistics != null) {
                    order.setState("done");
                } else {
                    order.setState("pay");
                }
            } else if ('1' == flash.getType()) {
                order.setState("done");
                stateCode = "returnproductapply";
            }
            updateEntitie(order);
            //推送消息给用户
            messageService.flashSendMessage(order, stateCode);
        }
        this.deleteOrderTimerByOrderId(orderId);
    }

    @Override
    public boolean flashOrderAcceptRefund(Integer orderId, Integer merchantId) {
        OrderEntity order = this.get(OrderEntity.class, orderId);

        if ("confirm".equals(order.getState()) || "berefund".equals(order.getRstate())) {
            return false;
        }

        order.setRstate("acceptRefundApply");//修改订单退款状态为同意申请
        this.updateEntitie(order);
        this.deleteOrderTimerByOrderId(orderId);//删除自动退款的定时记录

        Double orderMoney = order.getOnlineMoney() + order.getCredit();//订单实付金额
        FlashOrderReturnEntity flash = this.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", orderId);
        Boolean result = false;
        if (flash != null && "acceptRefundApply".equals(order.getRstate())) {//闪购订单同意退款申请
            Double refundMoney = flash.getRefundAmount();//用户申请的退款金额
            OrderIncomeEntity orderIncome = orderIncomeService.getOrderIncomeByOrderIdAndType(orderId, 0);//type 预收入类型，0 普通预收入，1 供应链预收入

            if (orderMoney > refundMoney) {
                try {
                    if (orderIncome == null) {
                        orderIncomeService.createOrderIncome(order);
                    }
                    result = acceptRefund(orderId, merchantId);
                    if (result) {
                        // 闪购订单修改退款信息状态
                        // 用户退款金额与用户实付金额之间的差值退给商家余额
                        // 修改order_income表的记录
                        orderIncome.setMoney(orderMoney - refundMoney);
                        orderIncome.setPayTime(DateUtils.getSeconds());
                        this.updateEntitie(orderIncome);
                        orderIncomeService.unOrderIncome(orderIncome.getId());
                    }
                } catch (Exception e) {
                    logger.error("闪购订单同意退款失败orderId:[" + orderId + "]", e);
                }
            } else {
                if (orderIncome != null) {
                    orderIncome.setState("cancel");
                    this.updateEntitie(orderIncome);
                }
                if (orderMoney.equals(refundMoney)) {
                    try {
                        result = acceptRefund(orderId, merchantId);
                    } catch (Exception e) {
                        logger.error("闪购订单同意退款失败orderId:[" + orderId + "]", e);
                    }
                } else {
                    logger.info("闪购订单用户申请退款金额大于用户订单实付金额，无法退款orderId:[" + orderId + "]=== orderMoney:[" + orderMoney + "]=== refundMoney:[" + refundMoney + "]");
                    result = false;
                }
            }
            //发送微信推送消息：退款成功
            if (result == true) {
                messageService.flashSendMessage(order, "refundSuccess");
            }

            return result;

        } else {
            logger.error("未找到orderId:[" + orderId + "]的闪购退款信息记录！");
            return result;
        }
    }

    @Override
    public List<Map<String, Object>> getExpressList() {
        String sql = "SELECT name FROM express WHERE state=1";
        List<Map<String, Object>> list = this.findForJdbc(sql);
        return list;
    }

    @Override
    public void resetOrderNum() {
        AliOcs.initOrderNum();
    }

    @Override
    public List<Map<String, Object>> findNeedCompleteList() {
        String yesToday = DateTime.now().minusDays(1).toString("yyyy-MM-dd");
        String sql = " select o.id, o.courier_id "
                + "from `order` o "
                + "where o.pay_state = 'pay' "
                + "and order_type in('normal','mobile','supermarket') "
                + "and state in ('delivery','pay','accept') "
                + "and rstate in ('normal') "
                + "and from_type not in('supplychain') "
                + "and flash_order_id = -1 " //排除闪购订单
                + "and date(from_unixtime(o.create_time)) = ? "; //昨天的订单
        return this.findForJdbc(sql, yesToday);
    }

    @Override
    public Map<String, Object> getCouponUser(String sn, Integer userId) {
        Integer num = userId % 10;
        StringBuffer sql = new StringBuffer("SELECT a.* FROM coupon_user_" + num + " a where  a.sn = '" + sn + "'");
        List<Map<String, Object>> list = findForJdbc(sql.toString());
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean refundNoticeToCourier(Integer orderId) {
        if (isUsingNewAlgorithm) {
            return scambleAlgorithmService.deleteRefundOrder(orderId);
        } else {
            pushedOrderDao.deleteByOrderId(orderId);
            return true;
        }
    }

    @Override
    public int setOrderState(String orderNum, String orderState) {
        StringBuilder sql = new StringBuilder("UPDATE `order` SET state = '" + orderState + "' WHERE state = 'accept' AND pay_id = '");
        sql.append(orderNum);
        sql.append("'");
        return this.updateBySqlString(sql.toString());
    }

}