package com.wm.controller.weixin;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.wm.controller.pay.OrderPayController;
import com.wm.entity.order.OrderEntity;
import com.wm.service.order.OrderServiceI;
import com.wm.service.pay.PayServiceI;
import com.wm.service.recharge.RechargeServiceI;
import com.wp.ConfigUtil;
import com.wp.XMLUtil;
import com.wxpay.service.WxPayService;

/**
 * 
 * @ClassName: WeixinPayController
 * @Description: 微信支付控制器
 * @author 黄聪
 * @date 2015年9月1日 上午9:31:07
 *
 */
@Controller
@RequestMapping("/takeOutController/weixinPay")
public class WeixinPayController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(OrderPayController.class);
	@Autowired
	private WxPayService wxPayService;
	@Autowired
	private RechargeServiceI rechargeService;
	@Autowired
	private PayServiceI payService;
	@Autowired
	private OrderServiceI orderService;

	/**
	 * 快递员APP充值
	 * 
	 * @param userId
	 * @param money
	 *            订单总金额，只能为整数，单位分
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(params = "recharge")
	@ResponseBody
	public AjaxJson recharge(int userId, String money,
			HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		String outTradeNo = StringUtil.generateSerialNumber("CZ");
		String body = "充值";
		String tradeType = "APP";
		Map<String, String> wxpay = wxPayService.getAppWxPay(ConfigUtil.KDY_APP_ID,ConfigUtil.KDY_MCH_ID,ConfigUtil.KDY_API_KEY,body, money,
				outTradeNo, oConvertUtils.getIp(),
				ConfigUtil.RECHARGE_NOTIFY_URL,tradeType);
		if (wxpay.get("return_code").equals("SUCCESS")
				&& wxpay.get("result_code").equals("SUCCESS")) {
			rechargeService.saveRechargeAndOrder(outTradeNo, body, money, userId, PayEnum.weixinpay.getEn());
			j.setObj(wxpay);
			j.setSuccess(true);
			j.setStateCode("00");
			j.setMsg("微信APP支付参数生成成功");
		} else {
			j.setSuccess(false);
			j.setMsg("wxpay err_code = " + wxpay.get("err_code")
					+ "err_code_des = " + wxpay.get("err_code_des"));
			j.setStateCode("01");
			j.setMsg("微信APP支付参数生成失败");
		}
		return j;
	}

	/**
	 * 充值成功回调方法
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/rechargeNotify.do")
	public void rechargeNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("-------------in rechargeNotify--------------");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		// 获取微信调用我们notify_url的返回信息
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("result = " + result);
		Map<String, String> map = XMLUtil.doXMLParse(result);
		outSteam.close();
		inStream.close();

		if (map.get("return_code").toString().equalsIgnoreCase("SUCCESS")) {
			if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
				String outTradeNo = map.get("out_trade_no");
				String transactionId = map.get("transaction_id");
				String totalFee = map.get("total_fee");
				int totalFeeInt = Integer.parseInt(totalFee);
				double totalFeeDouble = totalFeeInt/100.0;
				OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", outTradeNo);
				logger.info("payId:" + outTradeNo + ", payState:" + order.getPayState());
				if(order != null && OrderStateEnum.UNPAY.getOrderStateEn().equals(order.getPayState())){
	        		order.setPayType(PayEnum.weixinpay.getEn());
	       			order.setPayTime(DateUtils.getSeconds());
	       			order.setOnlineMoney(totalFeeDouble);
	       			order.setOutTraceId(transactionId);
					orderService.saveOrUpdate(order);
					payService.orderPayCallback(order);
					// 告诉微信服务器，我收到信息了，不要在调用回调action了
					OrderPayController.respToWeixin(response, "SUCCESS", "OK");
				}
			}
		} else {
			logger.error("return_msg=" + map.get("return_msg"));
		}
	}

	
	
}