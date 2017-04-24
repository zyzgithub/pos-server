package com.wm.controller.takeout;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.common.UserInfo;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.wm.controller.pay.OrderPayController;
import com.wm.entity.merchant.MerchantEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.pay.PayServiceI;
import com.wm.service.user.TumUserStatisticsServiceI;
import com.wm.service.user.WUserServiceI;
import com.wm.util.IPUtil;
import com.wp.AdvancedUtil;
import com.wp.CommonUtil;
import com.wp.ConfigUtil;
import com.wp.PayCommonUtil;
import com.wp.PayService;
import com.wp.XMLUtil;

@Controller
@RequestMapping("/weixin/store")
public class QRCodeController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(QRCodeController.class);
	
	@Autowired 
	private TumUserStatisticsServiceI tumUserStatisticsService;
	
	@Value("${isMixVersion}")
	private String isMixVersion;
	
	
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private PayServiceI payService;
	@Autowired
	private MerchantServiceI merchantService;
	@Autowired
	private WUserServiceI wUserService;
	
	@RequestMapping(value="/qrCode", method=RequestMethod.GET)
	private String createQRCodeOrder(@RequestParam(value = "merchantId") String merchantId, HttpServletRequest request, Model model) {
		logger.info("QRCodeController createQRCodeOrder merchantId={}", merchantId);
		if("false".equals(isMixVersion)){
			return "redirect:" + ConfigUtil.QRCODE_URL.replace("MERCHANT_ID", merchantId);
		}
		 
		if(!isWeixin(request)){
			return "main/warn";
		}
		// 登录验证
		UserInfo u = getUserInfo(request);
		Integer userId = u.getUserId();
		if(null != userId){
			logger.info("扫码支付userId==" + userId + ",扫码商家merchantId==="+merchantId);
			MerchantEntity mer = merchantService.get(MerchantEntity.class, Integer.parseInt(merchantId));
			model.addAttribute("merchantTitle", mer.getTitle());
			model.addAttribute("merchantId", merchantId);		
			return "takeout/qrcode";
		} else {
			return "redirect:" + AdvancedUtil.getWeiXinRedirectUrl(ConfigUtil.QRCode_URL + merchantId);
		}
	}
	
	/**
	 * 扫码支付创建订单
	 * @param payType
	 * @param saleType
	 * @param merchantId
	 * @param orderDetailCostList
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/createOrder",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	private  String createOrder (
			@RequestParam(value = "merchantId")String merchantId,
			@RequestParam(value = "money")Double money,
			@RequestParam(value = "saleType")String saleType,
			@RequestParam(value = "payType")String payType,
			Model model,HttpServletRequest request,HttpServletResponse response){
		UserInfo u = getUserInfo(request);
		Integer userId = u.getUserId();
		logger.info("扫码支付创建userId======="+userId);
		Map<String, String> resultMap = new HashMap<String, String>();
		//判断会员是否存在
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		if (user == null) {
			resultMap.put("state", "fail");
			resultMap.put("reason", "用户为空");
			return JSON.toJSONString(resultMap);
		}

		//生成订单
		Double checkCost = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if (checkCost > 0 && saleType.equals("2")) {
			//生成支付序列号，对于订单列表是唯一的
			String payId = String.valueOf(System.currentTimeMillis());
			Integer orderId = orderService.createQcCodeOrder(payId, user, merchantId, checkCost, saleType, payType);
			if(orderId != null){
				resultMap = qcCodeOrderWXPay(orderId, userId, true, request);
			}
			return JSON.toJSONString(resultMap);
		}
		return null;
	}
	
	@ResponseBody
	private Map<String, String> qcCodeOrderWXPay(
			Integer orderId,Integer userId,
			@RequestParam(required=false, defaultValue="false")Boolean valid,
			HttpServletRequest request) {
		
		Map<String, String> map = new HashMap<String, String>();
		OrderEntity order = orderService.get(OrderEntity.class, orderId);
		if(!valid) {
			boolean flag = orderService.validOrderByOrderId(orderId);
			if(!flag) {
				map.put("state", "fail");
				map.put("reason", "订单已无效，必须在20分钟内完成支付");
				orderService.returnCreditAndScore(orderId);
				return map;
			}
			//判断订单是否使用积分
			if (order.getScore() == 100 && order.getScoreMoney() == 5.0) {
				if(order.getOrigin() < 10) {
					order.setScore(0);
					order.setScoreMoney(0.0);
					orderService.updateEntitie(order);
				}
			}
		}
		
		//查询订单
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		MerchantEntity mer = merchantService.get(MerchantEntity.class, order.getMerchant().getId());
		
		//生成微信预付订单
		int money = ((int)Math.rint(order.getOrigin()*100) + (int)Math.rint(order.getDeliveryFee()*100)
					 - (int)Math.rint(order.getScoreMoney()*100) - (int)Math.rint(order.getCard()*100));
		String xml = PayService.createQRCodeJSPackage(mer.getTitle(), money+"", order.getPayId(), user.getOpenId(), IPUtil.getRemoteIp(request), ConfigUtil.QRCode_NOTIFY_URL);
		logger.info("预付订单提交数据"+xml);
		String result = CommonUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL, "POST", xml);
		logger.info("请求预付订单返回数据"+result);
		try {
			Map<String, String> resultMap = XMLUtil.doXMLParse(result);
			//判断prepay_id是否生成
			if(resultMap.get("return_code").equals("FAIL")) {
				map.put("state", "fail");
				map.put("reason", resultMap.get("return_msg"));
				logger.error("微信支付失败，错误码："+"------错误信息："+resultMap.get("return_msg"));
				return map;
			} else {
				if(resultMap.get("result_code").equals("FAIL")) {
					String errCode = resultMap.get("err_code");
					String errMsg = resultMap.get("err_code_des");
					logger.error("微信支付失败，错误码："+errCode+"------错误信息："+errMsg);
					map.put("state", "fail");
					map.put("reason", errMsg);
					return map;
				}
			}
			
			logger.info("微信预付订单prepay_id"+resultMap.get("prepay_id"));
			//封装用户支付订单
			SortedMap<String, String> params = new TreeMap<String, String>();
			params.put("appId", ConfigUtil.APPID_KFZ);
			params.put("timeStamp", System.currentTimeMillis()+"");
			params.put("nonceStr", PayCommonUtil.CreateNoncestr());
			params.put("package", "prepay_id=" + resultMap.get("prepay_id"));
			params.put("signType", ConfigUtil.SIGN_TYPE);
			
			// paySign的生成规则和Sign的生成规则一致
			String paySign = PayCommonUtil.createSign("UTF-8", params, ConfigUtil.API_KEY);
			params.put("paySign", paySign); 
			// 这里用packageValue是预防package是关键字在js获取值出错
			params.put("packageValue", "prepay_id=" + resultMap.get("prepay_id")); 
			
			logger.info("生成微信预付订单" + order.getPayId() + "|" + user.getNickname());
			params.put("orderId", orderId.toString());
			params.put("state", "success");
			params.put("payType", "wx");
			params.put("saleType", order.getSaleType().toString());
			params.put("preScore", String.valueOf(order.getOrigin().intValue()));
			if(order.getOrderType().equals("scan_order")){
				params.put("qcCodeMoney", order.getOrigin().toString());
				params.put("merchantId", order.getMerchant().getId().toString());
			}
			return params;
		} catch (Exception e) {
			e.printStackTrace();
			map.put("state", "fail");
			map.put("reason", "订单支付失败");
		}
		return map;
	}
	
	/**
	 * 扫码支付-微信支付回调
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/wxnotify.do")
	public void wechatPayCallback(HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("-------------扫码支付-微信支付回调--------------");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        //获取微信调用我们notify_url的返回信息
        String result  = new String(outSteam.toByteArray(),"utf-8");
        logger.info("微信支付回调内容:\n{}", result);
        if(StringUtil.isEmpty(result)){
        	logger.error("微信支付回调内容为空!!!!");
        	OrderPayController.respToWeixin(response, "FAIL", "");
        	return;
        }
        Map<String, String> map = XMLUtil.doXMLParse(result);
        outSteam.close();
        inStream.close();
        
        String code = map.get("result_code");
		logger.info("支付状态:{}", code);
		if ("SUCCESS".equals(code)) {
        	//获取支付ID 等价订单号
        	String payId = map.get("out_trade_no");
        	logger.info("payId:{}", payId);
        	//获取微信支付交易流水号
        	String outTraceId = map.get("transaction_id");
			logger.info("outTraceId:{}", outTraceId);
        	
        	//修改订单的状态
        	OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
        	if(null != order){
        		String orderPayState = order.getPayState();
				String orderOutTraceId = order.getOutTraceId();
				logger.info("payId:{}, payState:{}", payId, orderPayState);
				if (OrderStateEnum.UNPAY.getOrderStateEn().equals(orderPayState)) {
	        		order.setPayType(PayEnum.weixinpay.getEn());
	       			order.setOnlineMoney(Integer.valueOf(map.get("total_fee"))/100.0);
	       			order.setPayTime(DateUtils.getSeconds());
	       			order.setOutTraceId(outTraceId);
	                payService.updateEntitie(order);
	                
	                payService.orderPayCallback(order);

	                OrderPayController.respToWeixin(response, "SUCCESS", "OK");
				} else {
					if(outTraceId.equals(orderOutTraceId)){
						// 告诉微信服务器，我收到信息了，不要再重复回调了
						logger.warn("--------------重复通知--------------该订单{}已支付！状态为{}", payId, orderPayState);
					} else {
						logger.error("该订单{}已支付！状态为{}，但是交易流水号:{}与订单的交易流水号:{}不一致！", payId, orderPayState, outTraceId, orderOutTraceId);
					}
					OrderPayController.respToWeixin(response, "SUCCESS", "OK");
					return ;
				}
        	} else {
				logger.error("微信支付回调失败，未找到该订单{}", payId);
				OrderPayController.respToWeixin(response, "SUCCESS", "OK");
				return ;
			}
        } else {
        	logger.error("支付状态返回异常！！！");
			OrderPayController.respToWeixin(response, "FAIL", "");
			return ;
		}
    }
	
	
	/**
	 * 删除没支付的扫码订单
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/deleteQcCodeOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String deleteQcCodeOrder(@RequestParam(value = "orderId") String orderId){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			logger.warn("deleting order by id {} !!!!!!!!!!", orderId);
			orderService.deleteOrder(Integer.parseInt(orderId));
			map.put("success", "success");
		}catch(Exception e){
			logger.error("delete order by id " + orderId + " failed.", e);
			map.put("success", "fail");
		}
		return JSON.toJSONString(map);

	}
	
	/**
	 * 扫码支付成功页面跳转
	 * @param merchantId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/qrCodeSuccess",method = RequestMethod.GET)
	public String qrCodeSuccess(@RequestParam(value = "qcCodeMoney")String qcCodeMoney,Model model,HttpServletRequest request,HttpServletResponse response){
		UserInfo u = getUserInfo(request);
		Integer userId = u.getUserId();
		logger.info("扫码支付userId=="+userId+"扫码商家成功qcCodeMoney==="+qcCodeMoney);
		model.addAttribute("qcCodeMoney", qcCodeMoney);
		return "takeout/qrcodeSuccess";
	}
}
