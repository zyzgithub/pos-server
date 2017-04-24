package com.wm.service.impl.pay;

import com.alipay.service.AlipayService;
import com.base.constant.ErrorCode;
import com.base.enums.OrderStateEnum;
import com.base.exception.BusinessException;
import com.courier_mana.common.Constants;
import com.unionpay.upmp.sdk.conf.UpmpConfig;
import com.unionpay.upmp.sdk.service.UpmpService;
import com.wm.controller.open_api.retail.RetailPortCall;
import com.wm.controller.user.vo.CashierVo;
import com.wm.entity.card.CardEntity;
import com.wm.entity.credit.CreditEntity;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.pay.PayEntity;
import com.wm.entity.supermarket.CashierEntity;
import com.wm.entity.supermarket.CashierLoginLogEntity;
import com.wm.entity.supermarket.CashierSettlementLogEntity;
import com.wm.entity.systemconfig.SystemconfigEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.flow.FlowServiceI;
import com.wm.service.menu.MenuServiceI;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.merchant.TpmStatisticsRealtimeServiceI;
import com.wm.service.message.WmessageServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.ScanDiscountLogServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.service.orderstate.OrderStateServiceI;
import com.wm.service.pay.OrderHandler;
import com.wm.service.pay.PayServiceI;
import com.wm.service.ruralbase.MealServiceI;
import com.wm.service.supermarket.SuperMarketServiceI;
import com.wm.service.user.CashierServiceI;
import com.wm.service.user.TumUserStatisticsServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.AliOcs;
import com.wxpay.service.WxPayService;
import com.wxpay.util.SDKRuntimeException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.MD5;
import org.jeecgframework.core.util.PrintFEUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("payService")
@Transactional
@SuppressWarnings({"unused", "deprecation"})
public class PayServiceImpl extends CommonServiceImpl implements PayServiceI {

    private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private FlowServiceI flowService;
    @Autowired
    private OrderStateServiceI orderStateService;
    @Autowired
    private OrderIncomeServiceI orderIncomeService;
    @Autowired
    private OrderServiceI orderService;
    @Autowired
    private MenuServiceI menuService;
    @Autowired
    private WUserServiceI wUserService;
    @Autowired
    private TpmStatisticsRealtimeServiceI tpmStatisticsRealtimeService;
    @Autowired
    private MerchantServiceI merchantService;
    @Autowired
    private ScanDiscountLogServiceI scanDiscountLogService;
    @Autowired
    private TumUserStatisticsServiceI tumUserStatisticsService;
    @Autowired
    private MealServiceI mealService;
    @Autowired
    private WmessageServiceI messageService;
    @Resource
    private OrderHandleFactoryImpl orderHandleService;
    @Autowired
    private SuperMarketServiceI supermarketService;
    @Autowired
    private CashierServiceI cashierService;


    @Value("${wm_CFT_pay_receice_url}")
    public String payReceiveUrl;

    @Value("${wm_CFT_pay_key}")
    public String payKey;

    @Value("${wm_CFT_pay_merchantid}")
    public String payMerchantId;

    @Value("${wm_CFT_pay_callback_url}")
    public String payCallbackUrl;

    // 财付通支付常量
    private static final String CFT_VERSION = "2.0";
    private static final String CFT_BANK_TYPE = "0";
    private static final String CFT_FEE_TYPE = "1";
    private static final String CFT_CHARSET = "1";

    private static final String cancelOrderSql = "update `order` o set state='cancel' "
            + " where o.id >= ? and o.id < ? and o.order_type = 'supermarket_settlement'"
            + " and exists (select order_id from 0085_cashier_order where cashier_id=? and order_id=o.id)";


    /**
     * 支付回调订单处理
     */
    public void orderPayCallback(OrderEntity order) throws Exception {
        long startTime = System.currentTimeMillis();
        if (order == null) {
            logger.error("orderPayCallback order not exits !!!");
            throw new RuntimeException("order not exits !!!");
        }
        Integer orderId = order.getId();
        logger.info("==============================支付回调处理,orderId:{}", orderId);
        if (StringUtils.isEmpty(order.getOrderNum())) {
            String orderNum = AliOcs.genOrderNum(order.getMerchant().getId().toString());
            order.setOrderNum(orderNum);
        }
        order.setState(OrderStateEnum.PAY.getOrderStateEn());
        order.setPayState(OrderStateEnum.PAY.getOrderStateEn());
        orderService.updateEntitie(order);

        WUserEntity user = order.getWuser();

        // 使用积分Log
        if (order.getScore() > 0) {
            CreditEntity credit = new CreditEntity();
            credit.setWuser(user);
            credit.setDetail("[订单支付]-订单支付");
            credit.setScore(order.getScore());
            credit.setAction("buy");
            credit.setDetailId(orderId);
            this.save(credit);
        }

        // 处理首单用户
        if (order.getUserId() > 0 && user != null && wUserService.isFirstOrder(user.getId())) {
            user.setFirstOrderTime(DateUtils.getSeconds());
            wUserService.updateEntitie(user);
        }

        MerchantEntity merchant = order.getMerchant();
        String orderType = order.getOrderType();
        OrderHandler orderPayBackHandler = orderHandleService.getHandler(orderType);
        orderPayBackHandler.handle(order);

        String source = merchantService.getMerchantSource(merchant.getId());
        logger.info(">>>>>>>>>>>>>进入支付回调，orderId={},source={},orderType:{}", orderId, source, orderType);
        if (Constants.MERCHANT_SOURCE_RARUL.equals(source)) {
            // ===============================乡村基需求=======================================//
            logger.info(">>>>>>>>>>>>>进入乡村基堂食系统,orderId={}", orderId);
            mealService.rarulbase(order);
        } else if (Constants.MERCHANT_SOURCE_RETAIL.equals(source)) {
            // ===============================第三方Retail超市需求=======================================//
            logger.info(">>>>>>>>>>>>>进入一号生活的Retail中间件,进行提交订单操作【生成订单的支付回调:orderAlipayDone】,orderId={}", orderId);
            RetailPortCall.submitOrder(orderId);
        }

        // 用户消费统计,消费金额=总金额+配送费-积分抵扣金额-优惠券金额
        Double consumeMoney = Double.parseDouble(orderService.getOrderRealMoney(order)) * 100;
        logger.info("consumeMoney:{}", consumeMoney);
        // 用户、商家统计
        if(order.getUserId() > 0 && user != null){
	        if (OrderEntity.OrderType.RECHARGE.equals(orderType)
	                || OrderEntity.OrderType.MERCHANT_RECHARGE.equals(orderType)
	                || OrderEntity.OrderType.AGENT_RECHARGE.equals(orderType)) {
	            tumUserStatisticsService.updateStat(user.getId(), 0, consumeMoney.intValue());
	        } else {
	            tumUserStatisticsService.updateStat(user.getId(), consumeMoney.intValue(), 0);
	            if (OrderEntity.SaleType.DINE.equals(order.getSaleType())) {
	                // 堂食订单，立即统计；外卖订单，要等订单完成动作触发
	                tpmStatisticsRealtimeService.updateStat(merchant.getId(), consumeMoney.intValue(), 20);
	            }
	        }
        }
        long costTime = System.currentTimeMillis() - startTime;
        logger.info("orderId:{} orderPayCallback costtime:{} ms", orderId, costTime);
    }


    public void orderAlipayScanDone(OrderEntity order, String aliAcNo) throws Exception {
        if (order == null) {
            logger.error("orderAlipayScanDone order not exits !!!");
            throw new RuntimeException("order not exits !!!");
        }
        Integer orderId = order.getId();
        logger.info("==============================支付宝扫码支付回调处理,orderId:{}", orderId);
        if (StringUtils.isEmpty(order.getOrderNum())) {
            String orderNum = AliOcs.genOrderNum(order.getMerchant().getId().toString());
            order.setOrderNum(orderNum);
        }
        order.setState(OrderStateEnum.PAY.getOrderStateEn());
        order.setPayState(OrderStateEnum.PAY.getOrderStateEn());

        //根据支付宝账号查询用户
        WUserEntity user = wUserService.getUserByAliAcNo(aliAcNo, "user");
        if (user == null) {
            user = new WUserEntity();
            user.setAliAcNo(aliAcNo);
            user.setUsername(aliAcNo);
            user.setNickname(aliAcNo);
            user.setPassword(StringUtils.EMPTY);
            user.setPayPassword(StringUtils.EMPTY);
            user.setMobile(StringUtils.EMPTY);
            user.setSns(StringUtils.EMPTY);
            user.setMoney(new Double(0));
            user.setScore(0);
            user.setIsDelete(0);
            user.setUserType("user");
            user.setIp("");
            user.setCreateTime(DateUtils.getSeconds());
            user.setLoginTime(DateUtils.getSeconds());
            user.setFirstOrderTime(DateUtils.getSeconds());//新用户第一次下单时间
            wUserService.save(user);
        }
        order.setWuser(user);
        orderService.updateEntitie(order);


        String source = merchantService.getMerchantSource(order.getMerchant().getId());
        logger.info(">>>>>>>>>>>>>进入支付回掉，当前商家来源为:,source={}", source);
        // ===============================乡村基需求=======================================//
        if (Constants.MERCHANT_SOURCE_RARUL.equals(source)) {
            logger.info(">>>>>>>>>>>>>进入乡村基堂食系统,orderId={}", orderId);
            mealService.rarulbase(order);
        }
        // ===============================乡村基需求=======================================//
        String orderType = order.getOrderType();
        OrderHandler orderPayBackHandler = orderHandleService.getHandler(orderType);
        orderPayBackHandler.handle(order);
        /*if(OrderEntity.OrderType.SCAN_ORDER.equals(orderType)){
            logger.info("支付宝堂食==============start=====================");
			orderStateService.payOrderState(order);
			
			orderService.autoPrint(order);// 打印订单
			orderService.merchantAcceptOrder(order);
			menuService.buyCount(orderId);// 销量统计
			//扫码折扣记录表
			//加判断 这张单是否打折的  例如 originMoney != onlineMoney 
			scanDiscountLogService.processSanDiscount(order);//扫码折扣逻辑处理
			logger.info("支付宝堂食==============end=====================");
		} else {
			logger.warn("未知订单类型：orderId=" + orderId);
		}*/


        // 用户消费统计,消费金额=总金额+配送费-积分抵扣金额-优惠券金额
        Double consumeMoney = Double.parseDouble(orderService.getOrderRealMoney(order)) * 100;
        logger.info("consumeMoney:{}", consumeMoney);
        tumUserStatisticsService.updateStat(user.getId(), consumeMoney.intValue(), 0);
        if (OrderEntity.SaleType.DINE.equals(order.getSaleType())) {
            // 堂食订单，立即统计；外卖订单，要等订单完成动作触发
            tpmStatisticsRealtimeService.updateStat(order.getMerchant().getId(), consumeMoney.intValue(), 20);
        }

    }

    public void orderQQpayScanDone(Integer orderId) throws Exception {

        OrderEntity order = this.get(OrderEntity.class, orderId);
        String source = merchantService.getMerchantSource(order.getMerchant().getId());
        logger.info(">>>>>>>>>>>>>进入支付回掉，当前商家来源为:,source={}", source);
        // ===============================乡村基需求=======================================//
        if (Constants.MERCHANT_SOURCE_RARUL.equals(source)) {
            logger.info(">>>>>>>>>>>>>进入乡村基堂食系统,orderId={}", orderId);
            mealService.rarulbase(order);
        }
        // ===============================乡村基需求=======================================//
        String orderType = order.getOrderType();
        if (OrderEntity.OrderType.SCAN_ORDER.equals(orderType)) {
            logger.info("支付宝堂食==============start=====================");
            orderStateService.payOrderState(order);

            orderService.autoPrint(order);// 打印订单
            orderService.merchantAcceptOrder(order);
            menuService.buyCount(orderId);// 销量统计
            //扫码折扣记录表
            //加判断 这张单是否打折的  例如 originMoney != onlineMoney
            scanDiscountLogService.processSanDiscount(order);//扫码折扣逻辑处理
            logger.info("支付宝堂食==============end=====================");
        } else {
            logger.warn("未知订单类型：orderId=" + orderId);
        }

        // 用户消费统计,消费金额=总金额+配送费-积分抵扣金额-优惠券金额
        Double consumeMoney = Double.parseDouble(orderService.getOrderRealMoney(order)) * 100;
        logger.info("consumeMoney:{}", consumeMoney);
        tumUserStatisticsService.updateStat(order.getWuser().getId(), consumeMoney.intValue(), 0);
        if (OrderEntity.SaleType.DINE.equals(order.getSaleType())) {
            // 堂食订单，立即统计；外卖订单，要等订单完成动作触发
            tpmStatisticsRealtimeService.updateStat(order.getMerchant().getId(), consumeMoney.intValue(), 20);
        }

    }


    @Override
    public AjaxJson orderPay(int orderid, int userid, String mobile,
                             String cardid, int cardMoney, int score, double balance,
                             double alipayBalance, String payType, String timeRemark, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AjaxJson j = new AjaxJson();

        SystemconfigEntity systemConfigScore = this.findUniqueByProperty(SystemconfigEntity.class, "code", "score");

        SystemconfigEntity systemConfigScoreMoney = this.findUniqueByProperty(SystemconfigEntity.class, "code", "score_money");

        int scoreMoney = (int) (Integer.parseInt(systemConfigScoreMoney.getValue()) * score / Integer.parseInt(systemConfigScore.getValue()));
        OrderEntity order = this.get(OrderEntity.class, orderid);
        CardEntity card = null;
        WUserEntity wuser = order.getWuser();
        String title = order.getMerchant().getTitle();
        String orderNo = order.getPayId();
        String orderPayState = order.getPayState();
        int orderUserId = wuser.getId();
        double origin = order.getOrigin();

        if (cardid != null) {
            card = this.get(CardEntity.class, cardid);
        }

        // 判断订单是否合法
        if (userid != orderUserId) {
            j.setSuccess(false);
            j.setObj("error");
            j.setMsg("非法支付！用户错误~");
            j.setStateCode("01");
            System.out.println("非法支付！用户错误~");
            return j;
        }

        BigDecimal alipayBalanceBD = new BigDecimal(alipayBalance);
        alipayBalance = alipayBalanceBD.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        System.out.println("cardMoney:" + cardMoney);
        System.out.println("alipayBalance:" + alipayBalance);
        System.out.println("balance:" + balance);
        System.out.println("scoreMoney:" + scoreMoney);
        System.out.println("origin:" + origin);
        System.out.println("total:" + (cardMoney + alipayBalance + balance + scoreMoney));
        System.out.println("condition:" + ((cardMoney + alipayBalance + balance + scoreMoney) != origin));

        BigDecimal total_moneyBD = new BigDecimal(cardMoney + alipayBalance + balance + scoreMoney);
        double total_money = total_moneyBD.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 判断订单是否合法
        if (total_money != origin) {
            j.setSuccess(false);
            j.setObj("error");
            j.setMsg("非法支付！金额错误~");
            j.setStateCode("01");
            System.out.println("非法支付！金额错误~");
            return j;
        }

        int userScore = wuser.getScore();
        userScore -= score;
        // 检查积分支付是否合法
        if (userScore < 0) {
            j.setSuccess(false);
            j.setObj("error");
            j.setMsg("非法支付！积分错误~");
            j.setStateCode("01");
            System.out.println("非法支付！积分错误~");
            return j;
        }
        // 检查代金券支付是否合法
        if (cardMoney != 0) {
            if (card == null) {
                j.setSuccess(false);
                j.setObj("error");
                j.setMsg("非法支付！代金券错误~");
                j.setStateCode("01");
                System.out.println("非法支付！代金券错误~");
                return j;
            }

        }

        long time = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date nowDate = new Date(time);
        String orderDatetime = formatter.format(nowDate);

        String service = "";
        String state = "";
        String detail = "余额支付";
        String payState = "";
        // 开始支付
        if ("unpay".equals(orderPayState)) {

            // 使用积分
            if (score != 0 && alipayBalance == 0) {
                wuser.setScore(userScore);
                this.save(wuser);
                CreditEntity credit = new CreditEntity();
                credit.setWuser(wuser);
                credit.setDetail("[订单支付]-订单支付");
                credit.setScore(score);
                credit.setAction("buy");
                credit.setDetailId(orderid);
                this.save(credit);

            }

            // 使用余额
            if (balance != 0 && alipayBalance == 0) {
                double userBalance = wuser.getMoney();

                if (userBalance < balance) {
                    j.setSuccess(false);
                    j.setObj("error");
                    j.setMsg("用户余额不足！");
                    j.setStateCode("01");
                    return j;
                }
                service = "balance";
                state = "pay";
                payState = "pay";
                flowService.balancePayFlowCreate(orderid, wuser, balance);
                menuService.buyCount(orderid);    //销量统计
                orderService.setOrderNum(order);    //设置排号
                menuService.buyCountMenuPromotion(order.getId());//加促销数量
            }

            // 使用代金券
            if (cardMoney != 0 && alipayBalance == 0) {
                detail = "代金券";
                payState = "pay";
                state = "unpay";
                card.setOrderId(orderid);//关联订单表
                card.setConsume("Y");
                this.saveOrUpdate(card);
            }


            String bank = "";
            String orderInfo = "";
            TreeMap<String, String> wxpayParams = null;
            String subject = title;
            String body = title + ",订单号:" + orderNo;
            String alipayString = String.valueOf(alipayBalance);

            if (alipayBalance != 0) {
                state = "unpay";
                payState = "unpay";
                if ("unionpay".equals(payType)) {// 银联支付
                    service = "unionpay";
                    bank = "银联";
                    detail = "银联支付";
                    Map<String, String> resp = this.getYinLianFlow(orderNo,
                            alipayBalance, title, userid, orderDatetime);
                    if (resp != null) {
                        j.setObj(resp.get("tn"));
                    } else {
                        j.setSuccess(false);
                        j.setStateCode("01");
                        j.setMsg("银联支付出错");
                    }
                } else if ("alipay".equals(payType)) {// 支付宝支付
                    service = "alipay";
                    bank = "支付宝";
                    detail = "支付宝支付";
                    try {
                        orderInfo = AlipayService.getOrderInfo(subject, body,
                                alipayString, orderNo);
                    } catch (UnsupportedEncodingException e) {
                        j.setSuccess(false);
                        j.setStateCode("01");
                        j.setMsg("支付宝支付操作异常");
                        e.printStackTrace();
                    }
                    j.setObj(orderInfo);
                    j.setSuccess(true);
                } else if ("tenpay".equals(payType)) {// 财付通支付
                    service = "tenpay";
                    bank = "财付通";
                    detail = "财付通支付";
                    j.setObj(this.payParamMD5CodeByCFTZF(orderNo,
                            alipayBalance, title, userid, orderDatetime));
                } else if ("weixinpay".equals(payType)) {// 微信支付
                    service = "weixinpay";
                    bank = "微信支付";
                    detail = "微信支付";
                    try {
                        wxpayParams = WxPayService.getWxpayOrderInfo(body, alipayString, orderNo, request, response);
                        j.setMsg("操作成功");
                    } catch (SDKRuntimeException e) {
                        j.setSuccess(false);
                        j.setMsg("微信支付操作异常");
                        j.setStateCode("01");
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    j.setSuccess(true);
                    j.setObj(wxpayParams);
                }

            }
            order.setCardId(cardid);
            order.setMobile(mobile);
            order.setCredit(balance);
            order.setScoreMoney(Double.parseDouble(scoreMoney + ""));
            order.setScore(score);
            order.setCard(Double.parseDouble(cardMoney + ""));
            order.setOnlineMoney(alipayBalance);
            order.setPayState(payState);
            String orderType = order.getOrderType();
            if ("normal".equals(orderType)) {
                order.setState(state);
            }

            if (order.getTimeRemark() == null && "".equals(order.getTimeRemark()) && timeRemark != null && !"".equals(timeRemark.trim())) {
                order.setTimeRemark(timeRemark);
            }
            order.setPayType(payType);
            order.setPayTime((int) (time / 1000));
            this.saveOrUpdate(order);

            if (balance != 0 && alipayBalance == 0) {
                orderStateService.payOrderState(orderid);//订单状态记录
                messageService.sendMessage(order, "paySuccess");//发送下单成功微信公众号模版信息
                if (order.getSaleType().equals(1)) {
                    orderService.pushOrder(order.getId());
                }
                if (!orderType.equals("mobile")) {
                    orderService.merchantAcceptOrder(order);
                    orderService.autoPrint(order);//自动打票接单
                }

            }
        } else {
            j.setSuccess(false);
            j.setStateCode("01");
            j.setMsg("重复支付");
            System.out.println("重复支付");
        }
        Object obj = j.getObj();
        if (obj != null) {
            System.out.println(obj.toString());
        }

        return j;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map payParamMD5CodeByCFTZF(String orderNo, double orderAmount, String productName, int userid, String orderDatetime) {
        orderAmount = Math.rint(orderAmount * 100);

        String ver = CFT_VERSION; // 版本
        String charset = CFT_CHARSET;
        String bank_type = CFT_BANK_TYPE;
        String desc = productName;
        String purchaser_id = "";
        String bargainor_id = payMerchantId;
        String sp_billno = orderNo;
        String total_fee = "" + (int) orderAmount;
        String fee_type = CFT_FEE_TYPE;
        String notify_url = payReceiveUrl;
        String callback_url = payCallbackUrl;
        String key = payKey;

        String sign = toSignMsg(ver, charset, bank_type, desc, purchaser_id,
                bargainor_id, sp_billno, total_fee, fee_type, notify_url,
                callback_url, key).toUpperCase();

        Map payParamMap = new HashMap();

        String post = "https://wap.tenpay.com/cgi-bin/wappayv2.0/wappay_init.cgi";

        @SuppressWarnings({"resource"})
        HttpClient httpClient = new DefaultHttpClient();
        LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("ver", ver));
        params.add(new BasicNameValuePair("charset", charset));
        params.add(new BasicNameValuePair("bank_type", bank_type));
        params.add(new BasicNameValuePair("desc", desc));
        params.add(new BasicNameValuePair("purchaser_id", purchaser_id));
        params.add(new BasicNameValuePair("bargainor_id", bargainor_id));
        params.add(new BasicNameValuePair("sp_billno", sp_billno));
        params.add(new BasicNameValuePair("total_fee", "" + (int) orderAmount));
        params.add(new BasicNameValuePair("fee_type", fee_type));
        params.add(new BasicNameValuePair("notify_url", notify_url));
        params.add(new BasicNameValuePair("callback_url", callback_url));
        params.add(new BasicNameValuePair("sign", sign));

        try {
            HttpPost postMethod = new HttpPost(post);
            postMethod.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 将参数填入POST
            // Entity中

            HttpResponse response = httpClient.execute(postMethod); // 执行POST方法
            payParamMap
                    .put("resCode", response.getStatusLine().getStatusCode());

            Document doc = null;
            doc = DocumentHelper.parseText(EntityUtils.toString(
                    response.getEntity(), "utf-8"));
            Element rootElt = doc.getRootElement();
            String token_id = rootElt.elementTextTrim("token_id");

            payParamMap.put("token_id", token_id);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return payParamMap;
    }

    public String toSignMsg(String ver, String charset, String bank_type,
                            String desc, String purchaser_id, String bargainor_id,
                            String sp_billno, String total_fee, String fee_type,
                            String notify_url, String callback_url, String key) {
        String[] paaParamsArray = {bank_type, "bank_type", //
                bargainor_id, "bargainor_id", //
                callback_url, "callback_url",//
                charset, "charset",//
                desc, "desc",//
                fee_type, "fee_type",//
                notify_url, "notify_url",//
                sp_billno, "sp_billno",//
                total_fee, "total_fee",//
                ver, "ver",//
                key, "key", //
        };

        String paaStr = "";
        for (int i = 0; i < paaParamsArray.length; i++) {
            paaStr += paaParamsArray[i + 1] + "=" + paaParamsArray[i] + "&";
            i++;
        }
        String md5Str = MD5.GetMD5Code(paaStr.substring(0, paaStr.length() - 1));
        return md5Str;
    }

    private Map<String, String> getYinLianFlow(String orderNo,
                                               double orderAmount, String productName, int userid,
                                               String orderDatetime) {
        orderAmount = Math.rint(orderAmount * 100);
        Map<String, String> req = new HashMap<String, String>();
        req.put("version", UpmpConfig.VERSION);// 版本号
        req.put("charset", UpmpConfig.CHARSET);// 字符编码
        req.put("transType", "01");// 交易类型
        req.put("merId", UpmpConfig.MER_ID);// 商户代码
        req.put("backEndUrl", UpmpConfig.MER_BACK_END_URL);// 通知URL
        req.put("frontEndUrl", UpmpConfig.MER_FRONT_END_URL);// 前台通知URL(可选)
        req.put("orderDescription", productName);// 订单描述(可选)
        req.put("orderTime", orderDatetime);// 交易开始日期时间yyyyMMddHHmmss
        req.put("orderTimeout", "");// 订单超时时间yyyyMMddHHmmss(可选)
        req.put("orderNumber", orderNo);// 订单号(商户根据自己需要生成订单号)
        req.put("orderAmount", (int) orderAmount + "");// 订单金额
        req.put("orderCurrency", "156");// 交易币种(可选)
        req.put("reqReserved", "透传信息");// 请求方保留域(可选，用于透传商户信息)

        // 保留域填充方法
        Map<String, String> merReservedMap = new HashMap<String, String>();
        merReservedMap.put("test", "test");
        req.put("merReserved", UpmpService.buildReserved(merReservedMap));// 商户保留域(可选)

        Map<String, String> resp = new HashMap<String, String>();
        boolean validResp = UpmpService.trade(req, resp);

        // 商户的业务逻辑
        if (validResp) {
            System.out.println(resp);
            return resp;
        } else {
            return null;
        }
    }


    @Override
    public AjaxJson directPayOrderPay(int orderid, int userid, String mobile,
                                      String cardid, int cardMoney, int score, double balance,
                                      double alipayBalance, String payType, HttpServletRequest request, HttpServletResponse response) throws Exception {
        AjaxJson j = new AjaxJson();

        int scoreMoney = (int) (5 * score / 100);
        OrderEntity order = this.get(OrderEntity.class, orderid);

        CardEntity card = null;
        WUserEntity wuser = order.getWuser();
        String title = order.getTitle();
        String orderNo = order.getPayId();
        String orderPayState = order.getPayState();
        int orderUserId = wuser.getId();
        double origin = order.getOrigin();

        if (cardid != null) {
            card = this.get(CardEntity.class, cardid);
        }

        // 判断订单是否合法
        if (userid != orderUserId) {
            j.setSuccess(false);
            j.setObj("error");
            j.setMsg("非法支付！用户错误~");
            j.setStateCode("01");
            System.out.println("非法支付！用户错误~");
            return j;
        }

        System.out.println("cardMoney:" + cardMoney);
        System.out.println("alipayBalance:" + alipayBalance);
        System.out.println("balance:" + balance);
        System.out.println("scoreMoney:" + scoreMoney);
        System.out.println("origin:" + origin);
        System.out.println("total:"
                + (cardMoney + alipayBalance + balance + scoreMoney));
        System.out
                .println("condition:"
                        + ((cardMoney + alipayBalance + balance + scoreMoney) != origin));

        BigDecimal total_moneyBD = new BigDecimal(cardMoney + alipayBalance
                + balance + scoreMoney);
        double total_money = total_moneyBD
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 判断订单是否合法
        if (total_money != origin) {
            j.setSuccess(false);
            j.setObj("error");
            j.setMsg("非法支付！金额错误~");
            j.setStateCode("01");
            System.out.println("非法支付！金额错误~");
            return j;
        }

        int userScore = wuser.getScore();
        userScore -= score;
        // 检查积分支付是否合法
        if (userScore < 0) {
            j.setSuccess(false);
            j.setObj("error");
            j.setMsg("非法支付！积分错误~");
            j.setStateCode("01");
            System.out.println("非法支付！积分错误~");
            return j;
        }
        // 检查代金券支付是否合法
        if (cardMoney != 0) {
            if (card == null) {
                j.setSuccess(false);
                j.setObj("error");
                j.setMsg("非法支付！代金券错误~");
                j.setStateCode("01");
                System.out.println("非法支付！代金券错误~");
                return j;
            }
        }

        long time = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date nowDate = new Date(time);
        String orderDatetime = formatter.format(nowDate);

        String service = "";
        String state = "";
        String detail = "余额支付";
        String payState = "";
        // 开始支付
        if ("unpay".equals(orderPayState)) {

            // 使用积分
            if (score != 0 && alipayBalance == 0) {
                wuser.setScore(userScore);
                this.save(wuser);
                CreditEntity credit = new CreditEntity();
                credit.setWuser(wuser);
                credit.setDetail("[自主买单支付]-订单支付");
                credit.setScore(score);
                credit.setAction("buy");
                credit.setDetailId(orderid);
                this.save(credit);
            }

            // 使用余额
            if (balance != 0 && alipayBalance == 0) {
                double userBalance = wuser.getMoney();

                if (userBalance < balance) {
                    j.setSuccess(false);
                    j.setObj("error");
                    j.setMsg("用户余额不足！");
                    j.setStateCode("01");
                    return j;
                }
                service = "balance";
                state = "pay";
                payState = "pay";
                order.setCompleteTime(DateUtils.getSeconds());
                flowService.balancePayFlowCreate(orderid, wuser, balance);
            }

            // 使用代金券
            if (cardMoney != 0 && alipayBalance == 0) {
                detail = "代金券";
                state = "pay";
                payState = "pay";
                card.setOrderId(orderid);//关联订单表
                card.setConsume("Y");
                this.saveOrUpdate(card);
            }


            String bank = "";
            String orderInfo = "";
            String subject = title;
            String body = title + ",订单号:" + orderNo;
            String alipayString = String.valueOf(alipayBalance);

            if (alipayBalance != 0) {
                state = "unpay";
                payState = "unpay";
                if ("unionpay".equals(payType)) {// 银联支付
                    service = "unionpay";
                    bank = "银联";
                    detail = "银联支付";
                    Map<String, String> resp = this.getYinLianFlow(orderNo,
                            alipayBalance, title, userid, orderDatetime);
                    if (resp != null) {
                        j.setObj(resp.get("tn"));
                    } else {
                        j.setSuccess(false);
                        j.setStateCode("01");
                        j.setMsg("银联支付出错");
                    }
                } else if ("alipay".equals(payType)) {// 支付宝支付
                    service = "alipay";
                    bank = "支付宝";
                    detail = "支付宝支付";
                    try {
                        orderInfo = AlipayService.getOrderInfo(subject, body, alipayString, orderNo);
                    } catch (UnsupportedEncodingException e) {
                        j.setSuccess(false);
                        j.setStateCode("01");
                        j.setMsg("支付宝支付操作异常");
                        e.printStackTrace();
                    }
                    j.setObj(orderInfo);
                    j.setSuccess(true);
                } else if ("tenpay".equals(payType)) {// 财付通支付
                    service = "tenpay";
                    bank = "财付通";
                    detail = "财付通支付";
                    j.setObj(this.payParamMD5CodeByCFTZF(orderNo,
                            alipayBalance, title, userid, orderDatetime));
                } else if ("weixinpay".equals(payType)) {// 微信支付

                    service = "weixinpay";
                    bank = "微信支付";
                    detail = "微信支付";
                    try {
                        //后艺改
                        Map<String, String> wxpay = WxPayService.getWxpayOrderInfo(body, alipayString, orderNo, request, response);
                        j.setObj(wxpay);
                        orderInfo = "微信支付成功";

                    } catch (SDKRuntimeException e) {
                        j.setSuccess(false);
                        j.setMsg("微信支付操作异常");
                        j.setStateCode("01");
                        e.printStackTrace();
                    } catch (Exception e) {
                        j.setSuccess(false);
                        j.setMsg("微信支付操作异常");
                        j.setStateCode("01");
                        e.printStackTrace();
                    }
                    j.setSuccess(true);
                    j.setMsg(orderInfo);
                }
            }
            order.setCardId(cardid);
            order.setMobile(mobile);
            order.setCredit(balance);
            order.setScoreMoney(Double.parseDouble(scoreMoney + ""));
            order.setScore(score);
            order.setCard(Double.parseDouble(cardMoney + ""));
            order.setOnlineMoney(alipayBalance);
            order.setPayState(payState);
            order.setState("done");

            order.setPayType(payType);
            order.setPayTime((int) (time / 1000));
            this.saveOrUpdate(order);

            if (balance != 0 && alipayBalance == 0) {
                orderIncomeService.createOrderIncome(order);//商家预收入
            }


            PayEntity pay = this.get(PayEntity.class, orderNo);

            if (pay == null) {
                pay = new PayEntity();
                pay.setId(orderNo);
                pay.setOrderId(orderid);
                pay.setBank(bank);
                pay.setMoney(alipayBalance);
                pay.setService(service);
                pay.setState("pay");
                this.save(pay);
            } else {
                pay.setBank(bank);
                pay.setMoney(alipayBalance);
                pay.setService(service);
                pay.setState("pay");
                this.saveOrUpdate(pay);
            }


        } else {
            j.setSuccess(false);
            j.setStateCode("01");
            j.setMsg("重复支付");
            System.out.println("重复支付");
        }
        Object obj = j.getObj();
        if (obj != null) {
            System.out.println(obj.toString());
        }
        return j;
    }

    @Override
    public AjaxJson rechargeOrderPay(int orderid, int userid, double alipayBalance, String payType, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();

        Map<String, Object> map = this.findOneForJdbc("SELECT a.user_id,a.title,a.state,a.pay_id AS orderNo,a.origin FROM `order` a where a.id=?", orderid);
        Map<String, Object> userMap = this.findOneForJdbc("select mobile,score,money from user where id=?", userid);

        String title = (String) map.get("title");
        String orderNo = (String) map.get("orderNo");
        String orderState = (String) map.get("state");
        String userMobile = (String) userMap.get("mobile");
        int orderUserId = Integer.parseInt(map.get("user_id").toString());
        double origin = Double.parseDouble(map.get("origin").toString());
        // 判断订单是否合法
        if (userid != orderUserId) {
            j.setSuccess(false);
            j.setObj("error");
            j.setMsg("非法支付！用户错误~");
            j.setStateCode("01");
            System.out.println("非法支付！用户错误~");
            return j;
        }

        // 判断订单是否合法
        if (alipayBalance != origin) {
            j.setSuccess(false);
            j.setObj("error");
            j.setMsg("非法支付！金额错误~");
            j.setStateCode("01");
            System.out.println("非法支付！金额错误~");
            return j;
        }

        long time = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date nowDate = new Date(time);
        String orderDatetime = formatter.format(nowDate);

        String service = "";
        String state = "";
        String detail = "";

        // 开始支付
        if ("unpay".equals(orderState)) {

            String bank = "";
            String orderInfo = "";
            String subject = title;
            String body = title + ",订单号:" + orderNo;
            String alipayString = String.valueOf(alipayBalance);

            if (alipayBalance != 0) {
                if ("unionpay".equals(payType)) {// 银联支付
                    service = "unionpay";
                    bank = "银联";
                    detail = "银联支付";
                    Map<String, String> resp = this.getYinLianFlow(orderNo,
                            alipayBalance, title, userid, orderDatetime);
                    if (resp != null) {
                        j.setObj(resp.get("tn"));
                    } else {
                        j.setSuccess(false);
                        j.setStateCode("01");
                        j.setMsg("银联支付出错");
                    }
                } else if ("alipay".equals(payType)) {// 支付宝支付
                    service = "alipay";
                    bank = "支付宝";
                    detail = "支付宝支付";
                    try {
                        orderInfo = AlipayService.getOrderInfo(subject, body,
                                alipayString, orderNo);
                    } catch (UnsupportedEncodingException e) {
                        j.setSuccess(false);
                        j.setStateCode("01");
                        j.setMsg("支付宝支付操作异常");
                        e.printStackTrace();
                    }
                    j.setObj(orderInfo);
                    j.setSuccess(true);
                } else if ("tenpay".equals(payType)) {// 财付通支付
                    service = "tenpay";
                    bank = "财付通";
                    detail = "财付通支付";
                    j.setObj(this.payParamMD5CodeByCFTZF(orderNo,
                            alipayBalance, title, userid, orderDatetime));
                } else if ("weixinpay".equals(payType)) {// 微信支付
                    service = "weixinpay";
                    bank = "微信支付";
                    detail = "微信支付";
                    try {
                        orderInfo = WxPayService.getOrderInfo(body,
                                alipayString, orderNo);
                        //后艺改
                        Map<String, String> wxpay = WxPayService.getWxpayOrderInfo(body, alipayString, orderNo, request, response);
                        j.setObj(wxpay);
                        orderInfo = "微信支付成功";

                    } catch (SDKRuntimeException e) {
                        j.setSuccess(false);
                        j.setMsg("微信支付操作异常");
                        j.setStateCode("01");
                        e.printStackTrace();
                    } catch (Exception e) {
                        j.setSuccess(false);
                        j.setMsg("微信支付操作异常");
                        j.setStateCode("01");
                        e.printStackTrace();
                    }
                    j.setSuccess(true);
                    j.setMsg(orderInfo);
                }

            }

            this.executeSql("update `order` set online_money=?,pay_type=?,pay_time=UNIX_TIMESTAMP(?) where id=?",
                    alipayBalance, payType, orderDatetime, orderid);
            this.executeSql(
                    "insert into pay(id,order_id,bank,money,currency,service,create_time) values(?,?,?,?,'CNY',?,UNIX_TIMESTAMP())",
                    orderNo, orderid, bank, alipayBalance, service);


        } else {
            j.setSuccess(false);
            j.setStateCode("01");
            j.setMsg("重复支付");
            System.out.println("重复支付");
        }
        Object obj = j.getObj();
        if (obj != null) {
            System.out.println(obj.toString());
        }
        return j;
    }

    public List<Map<String, Object>> findPayRecord(Integer userId) {
        String sql = "select p.money, FROM_UNIXTIME(p.create_time,'%Y-%m-%d %H:%i') from pay p left join `order` o on o.id=p.order_id ";
        sql += " where p.vid is not null and o.user_id=?";
        return this.findForJdbc(sql, new Object[]{userId});
    }

    public void updatePayState(Integer orderId) {
        String sql = "UPDATE `pay` SET state ='pay' WHERE order_id=?";
        this.executeSql(sql, orderId);
    }


    /**
     * @param order
     * @param cashierId
     * @throws Exception
     */
    @Override
    public void saveSettleMentLog(OrderEntity order, Integer cashierId) throws Exception {
        CashierVo cashierVo = cashierService.get(cashierId);
        String findCashierSettlementLogHql = " from CashierSettlementLogEntity where isPaid='0' and cashierId=? order by create_time desc";
        List<CashierSettlementLogEntity> cashierSettlements = supermarketService.findHql(findCashierSettlementLogHql, cashierId);

        //更新结算日志(老版本保存日志)
        CashierSettlementLogEntity cashierSettlement = null;
        Integer orderId = order.getId();
        logger.info("cashierSettlements size : {}", cashierSettlements.size());
        if (CollectionUtils.isEmpty(cashierSettlements)) {
            cashierSettlement = new CashierSettlementLogEntity();
            cashierSettlement.setCashierId(cashierVo.getId());
            cashierSettlement.setMerchantId(cashierVo.getMerchantId());
            cashierSettlement.setSettlementOrderId(orderId);
            cashierSettlement.setMoney(order.getOrigin());
            cashierSettlement.setCreateTime(DateUtils.getSeconds());
            cashierSettlement.setCash(order.getOrigin());
            cashierSettlement.setPaidTime(DateUtils.getSeconds());
            cashierSettlement.setIsPaid("1");
            cashierService.save(cashierSettlement);
        } else {
            cashierSettlement = cashierSettlements.get(0);
            cashierSettlement.setMoney(order.getOrigin());
            cashierSettlement.setPaidTime(DateUtils.getSeconds());
            cashierSettlement.setIsPaid("1");
            cashierSettlement.setSettlementOrderId(orderId);
            cashierService.saveOrUpdate(cashierSettlement);
            //如果存在多条结算订单，取消之前的结算订单，以当前的结算订单为准
            Integer settleMentId = cashierSettlement.getSettlementOrderId();
            if (!orderId.equals(settleMentId)) {
                this.executeSql(cancelOrderSql, settleMentId, orderId, cashierVo.getId());
            }
        }

        //保存一条推出日志
        logger.info("cashier:{} quit ...", cashierVo.getId());
        CashierEntity cashier = cashierService.get(CashierEntity.class, cashierVo.getId());
        cashierService.saveCashierLoginLog(cashier, CashierLoginLogEntity.EXIT, null);
    }

    public String process(String template, Map<String, ?> data) throws Exception {
        Configuration cfg = new Configuration();
        String path = this.getClass().getResource("/template").getPath();
        cfg.setDirectoryForTemplateLoading(new File(path));
        cfg.setObjectWrapper(new DefaultObjectWrapper());

        //设置字符集
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);

        StringWriter out = new StringWriter();
        Template temp = cfg.getTemplate(template);
        temp.process(data, out);
        out.flush();

        return out.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void printSettlement(OrderEntity order, Integer cashierId) throws Exception {
        try {
            CashierVo cashierVo = cashierService.get(cashierId);
            String findCashierSettlementLogHql = " from CashierSettlementLogEntity where isPaid='0' and cashierId=? order by create_time desc";
            List<CashierSettlementLogEntity> cashierSettlements = supermarketService.findHql(findCashierSettlementLogHql, cashierId);
            //统计
            String key = "settle_" + order.getId() + "_" + cashierVo.getId();
            Object o = AliOcs.getObject(key);

            Map<String, Object> data = null;
            if (o != null) {
                data = (Map<String, Object>) o;
            } else {
                String beginTime = supermarketService.getBeginTimeForStatictis(cashierVo.getMerchantId(), cashierVo.getId());
                if (beginTime == null) {
                    logger.warn("查不到收银员cashierId:{}对应的登录信息", cashierVo.getId());
                    throw new BusinessException(ErrorCode.WRONG_DATA, "数据异常");
                }
                if (CollectionUtils.isNotEmpty(cashierSettlements)) {
                    String endTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("beginTime", beginTime);
                    params.put("endTime", endTime);
                    params.put("merchantId", cashierVo.getMerchantId());
                    params.put("cash", cashierSettlements.get(0).getCash());
                    data = supermarketService.newStatisticAfterSettlement(cashierVo.getId(), params);
                } else {
                    data = supermarketService.statisticAfterSettlement(cashierVo.getId());
                }
            }

            //打印小票
            data.put("cashier", cashierVo.getName());
            Integer posEdition = cashierService.getPosEdition(cashierId);
            //基础版
            String content = "";
            if (posEdition.intValue() == 2) {
                content = process("basicPosSettlement.ftl", data);
            } else {
                content = process("settlement.ftl", data);
            }
            if (!"sunmi".equals(order.getMerchant().getPrintCode())) {
                PrintFEUtils.print(order.getMerchant().getPrintCode(), "1", content);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("打印设备异常");
            throw new BusinessException(ErrorCode.INVALID_ARGUMENT, "小票机绑定异常，请检查");
        }
    }
}