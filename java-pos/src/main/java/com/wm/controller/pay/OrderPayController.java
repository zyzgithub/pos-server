package com.wm.controller.pay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.MD5;
import org.jeecgframework.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alipay.util.AlipayCore;
import com.alipay.util.AlipayNotify;
import com.base.enums.OrderStateEnum;
import com.base.enums.PayEnum;
import com.wm.entity.order.OrderEntity;
import com.wm.entity.orderchannel.OrderChannel;
import com.wm.service.order.OrderServiceI;
import com.wm.service.orderchannel.OrderChannelServiceI;
import com.wm.service.pay.PayServiceI;
import com.wm.tencent.client.ClientResponseHandler;
import com.wm.tencent.client.TenpayHttpClient;
import com.wm.tencent.pay.RequestHandler;
import com.wm.tencent.pay.ResponseHandler;
import com.wm.util.AliOcs;
import com.wm.util.DateUtil;
import com.wm.util.HttpUtils;
import com.wm.util.IDistributedLock;
import com.wm.util.MemcachedDistributedLock;
import com.wm.util.innerAPI.WMApiCall;
import com.wp.JsonUtil;
import com.wp.XMLUtil;

/**
 * 订单支付回调处理类
 * @author Simon
 */
@Controller
@RequestMapping("/takeOutController")
public class OrderPayController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(OrderPayController.class);


	@Autowired
	private PayServiceI payService;
	@Autowired
	private OrderServiceI orderService;
	@Autowired
	private OrderChannelServiceI orderChannelService;
	@Autowired
	private WMApiCall wmApiCall;

	// 财付通
	@Value("${PartnerKey}")
	private String partnerKey;

	private final static int EXPIRE = 60;
	private final static int TRY_TIME = 30;

	/**
	 * 支付宝回调
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/alipay.do")
	public void alipay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		// 获取支付宝POST过来反馈信息
		logger.info("-------------进入支付宝回调--------------");
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		String logs = AlipayCore.createLinkString(params);

		logger.info("支付宝 back logs:" + logs);
		boolean verify_result = AlipayNotify.verify(params);
		if (!verify_result) {
			logger.warn(" MD5 解密签名不匹配, 尝试使用RSA解密签名,尝试去匹配手机支付");
			verify_result = AlipayNotify.verifyRSA(params);
		}

		boolean caseByAlipayRefund = caseByAlipayRefund(params);
		if (caseByAlipayRefund) {
			// 目前暂不处理，由实际退款的异步通知接口进行处理
			logger.warn("因退款导致交易状态发生变化而触发移动支付异步通知");
		}

		boolean caseByAlipayTradeSuccess = caseByAlipayTradeSuccess(params);

		if (verify_result && !caseByAlipayRefund && caseByAlipayTradeSuccess) {
			IDistributedLock lock = new MemcachedDistributedLock();
			String uuid = null;
			String lockName = "alipay" + params.get("out_trade_no");

			try {
				uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
				if (uuid == null) {
					logger.error("其它线程也在对支付宝订单【{}】进行操作， 获取不到锁", params.get("out_trade_no"));
					response.getOutputStream().println("fail");
					return;
				}
				String payId = params.get("out_trade_no");// 商家订单号
				String outTraceId = params.get("trade_no");// 支付宝交易流水号
				String buyerEmail = params.get("buyer_email"); // 买家支付宝账号
				double totalFee = Double.parseDouble(params.get("total_fee"));
				int totalFeeInt = (int) Math.rint(totalFee * 100);
				String totalFeeStr = String.valueOf(totalFeeInt);
				logger.info("alipay payId:{}, outTraceId:{}, buyer_email:{}, totalFeeStr:{}", payId, outTraceId, buyerEmail, totalFeeStr);
				OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
				if (order != null) {
					String orderPayState = order.getPayState();
					String orderOutTraceId = order.getOutTraceId();
					logger.info("order payId:{}, payState:{}, orderOutTraceId:{}", payId, orderPayState, orderOutTraceId);
					if(OrderStateEnum.UNPAY.getOrderStateEn().equals(order.getPayState())){
						order.setPayType(PayEnum.alipay.getEn());
						order.setOnlineMoney(totalFee);
						order.setPayTime(DateUtils.getSeconds());
						order.setOutTraceId(outTraceId);
						payService.updateEntitie(order);
						
						payService.orderPayCallback(order);
						
						long costTime = System.currentTimeMillis() - startTime;
						logger.info("alipay costtime:{} ms", costTime);
						
						response.getOutputStream().println("success");
					} else {
						if (outTraceId.equals(orderOutTraceId)) {
							logger.warn("--------------重复通知--------------该订单{}已支付！状态为{}", payId, orderPayState);
						} else {
							logger.error("该订单{}已支付！状态为{}，但是交易流水号:{}与订单的交易流水号:{}不一致！", payId, orderPayState, outTraceId, orderOutTraceId);
						}
						response.getOutputStream().println("success");
						return;
					}
				} else {
					logger.error("支付宝支付回调失败，未找到该订单{}", payId);
					response.getOutputStream().println("fail");
				}
			} catch (Exception e) {
				logger.error("支付宝回调失败 ", e);
				response.getOutputStream().println("fail");
			} finally {
				if (uuid != null) {
					lock.releaseLock(lockName, uuid);
				}
			}
		} else {
			logger.error("签名校验失败~ ");
			response.getOutputStream().println("fail");
		}
	}
	
	/**
	 *支付宝支付是否成功
	 *trade_status = TRADE_FINISHED 或者TRADE_SUCCESS为成功
	 *WAIT_BUYER_PAY = 交易创建
	 *TRADE_CLOSED =交易关闭
	 * @param params
	 * @return
	 * @author chenweihong
	 * @date 2016年4月29日 下午2:45:03
	 */
    private boolean caseByAlipayTradeSuccess(Map<String, String> params) {
        return   "TRADE_SUCCESS".equals(params.get("trade_status")) || "TRADE_FINISHED".equals(params.get("trade_status"));
    }

	/**
	 * <p>
	 * 检查参数：refund_status, gmt_refund
	 * <p>
	 * 以上两个参数只出现于因为退款导致的交易状态发生变化的情况下。
	 * 即如果有这两个参数，则当前通知实际不是支付成功的通知，而是因为退款而导致的交易状态变化的通知。
	 * <p>
	 * 具体参考
	 * https://doc.open.alipay.com/doc2/detail.htm?treeId=59&articleId=103666&
	 * docType=1，或咨询支付宝客服。
	 * 
	 * @param params
	 *            参数map
	 * @return 是否因退款导致交易状态变化而触发的通知
	 */
	private boolean caseByAlipayRefund(Map<String, String> params) {
		return StringUtils.isNotEmpty(params.get("refund_status")) || StringUtils.isNotEmpty(params.get("gmt_refund"));
	}

	

	/**
	 * 财付通支付
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/wxpay.do")
	public AjaxJson wxpay(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("-------------进入财付通支付回调--------------");
		AjaxJson j = new AjaxJson();
		ResponseHandler resHandler = new ResponseHandler(request, response);
		resHandler.setKey(partnerKey);
		RequestHandler queryReq = new RequestHandler(null, null);// 创建请求对象
		queryReq.init();
		if (resHandler.isTenpaySign() == true) {
			String payId = resHandler.getParameter("out_trade_no");// 商户订单号
			String transactionId = resHandler.getParameter("transaction_id");// 财付通订单号
			String totalFee = resHandler.getParameter("total_fee");// 金额,以分为单位
			// 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
			// String discount = resHandler.getParameter("discount");
			// 支付结果
			String tradeState = resHandler.getParameter("trade_state");

			// 判断签名及结果
			if ("0".equals(tradeState)) {
				IDistributedLock lock = new MemcachedDistributedLock();
				String uuid = null;
				String lockName = "alipay" + payId;

				try {
					uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
					if (uuid == null) {
						logger.error("其它线程也在对财付通订单【{}】进行操作， 获取不到锁", payId);
						resHandler.sendToCFT("fail");
						j.setSuccess(false);
						return j;
					}
					OrderEntity order = payService.findUniqueByProperty(OrderEntity.class, "payId", payId);
					if (order != null && OrderStateEnum.UNPAY.getOrderStateEn().equals(order.getPayState())) {
						logger.info("orderType:" + order.getOrderType() + ", fromType:" + order.getFromType());
						order.setPayType(PayEnum.tenpay.getEn());
						order.setOnlineMoney(Integer.valueOf(totalFee) / 100.0);
						order.setPayTime(DateUtils.getSeconds());
						order.setOutTraceId(transactionId);
						order.setTimeRemark(DateUtil.getOrderTimeRemark(order.getTimeRemark(), order.getSaleType()));
						payService.saveOrUpdate(order);
						payService.orderPayCallback(order);
						resHandler.sendToCFT("success");
					} else {
						j.setSuccess(false);
						return j;
					}
					logger.info("即时到账支付成功");
				} catch (Exception e) {
					logger.error("财付通回调失败 ", e);
					resHandler.sendToCFT("fail");
				} finally {
					if (uuid != null) {
						lock.releaseLock(lockName, uuid);
					}
				}
			}
		}
		resHandler.sendToCFT("fail");
		return j;
	}

	/**
	 * 微信支付回调处理
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws JDOMException
	 * @throws Exception
	 */
	@RequestMapping("/wxnotify.do")
	public void wechatPayCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("-------------进入微信支付回调--------------");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		// 获取微信调用我们notify_url的返回信息
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("微信支付回调内容:\n{}", result);
		if (StringUtil.isEmpty(result)) {
			logger.error("微信支付回调内容为空!!!!");
			response.getWriter().write(XMLUtil.setXML("FAIL", ""));
			return;
		}
		Map<String, String> map = XMLUtil.doXMLParse(result);
		outSteam.close();
		inStream.close();
		String code = map.get("result_code");
		logger.info("支付状态:{}", code);
		if ("SUCCESS".equals(code)) {
			String payId = map.get("out_trade_no");
			String outTraceId = map.get("transaction_id");
			String onlineMoney = map.get("total_fee");//交易金额，单位：分
			logger.info("wechatPayCallback payId:{}, outTraceId:{}, onlineMoney:{}", payId, outTraceId, onlineMoney);
			IDistributedLock lock = new MemcachedDistributedLock();
			String uuid = null;
			String lockName = "alipay" + payId;
			try {
				uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
				if (uuid == null) {
					logger.error("其它线程也在对微信订单【{}】进行操作， 获取不到锁", payId);
					response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
					return;
				}
				OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
				if (null != order) {
					String orderPayState = order.getPayState();
					String orderOutTraceId = order.getOutTraceId();
					logger.info("order payId:{}, payState:{}", payId, orderPayState);
					if (OrderStateEnum.UNPAY.getOrderStateEn().equals(orderPayState)) {
						order.setPayType(PayEnum.weixinpay.getEn());
						order.setOnlineMoney(Integer.valueOf(onlineMoney) / 100.0);
						order.setPayTime(DateUtils.getSeconds());
						order.setOutTraceId(outTraceId);
						order.setTimeRemark(DateUtil.getOrderTimeRemark(order.getTimeRemark(), order.getSaleType()));
						orderService.updateEntitie(order);
						
						payService.orderPayCallback(order);
						long costTime = System.currentTimeMillis() - startTime;
						logger.info("wechatPayCallback costtime:{} ms", costTime);
						respToWeixin(response, "SUCCESS", "OK");
						return;
					} else {
						if (outTraceId.equals(orderOutTraceId)) {
							// 告诉微信服务器，我收到信息了，不要再重复回调了
							logger.warn("--------------重复通知--------------该订单{}已支付！状态为{}", payId, orderPayState);
						} else {
							logger.error("该订单{}已支付！状态为{}，但是交易流水号:{}与订单的交易流水号:{}不一致！", payId, orderPayState, outTraceId, orderOutTraceId);
						}
						respToWeixin(response, "SUCCESS", "OK");
						return;
					}
				} else {
					logger.error("微信支付回调失败，未找到该订单{}", payId);
					respToWeixin(response, "FAIL", "");
					return;
				}
			} catch (Exception e) {
				logger.error("微信回调失败 ", e);
				respToWeixin(response, "FAIL", "");
			} finally {
				if (uuid != null) {
					lock.releaseLock(lockName, uuid);
				}
			}
		} else {
			logger.error("支付状态{}返回异常！！！", code);
			respToWeixin(response, "FAIL", "");
			return;
		}
	}

	/**
	 * 威富通支付回调处理
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/wftnotify.do")
	public void wftPayCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("-------------进入威富通支付回调--------------");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		// 获取微信调用我们notify_url的返回信息
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("威富通支付回调内容:\n{}", result);
		if (StringUtil.isEmpty(result)) {
			logger.error("威富通支付回调内容为空!!!!");
			respToWeixin(response, "FAIL", "");
			return;
		}
//		Map<String, String> map = XMLUtil.doXMLParse(result);
		Map<String, String> map = JsonUtil.toMap(result);
		outSteam.close();
		inStream.close();
		String code = map.get("result_code");
		logger.info("威富通支付状态:{}", code);
		if ("SUCCESS".equals(code)) {
			String payId = map.get("out_trade_no");// 获取支付ID 等价订单号
			String outTraceId = map.get("transaction_id");// 获取微信支付交易流水号
			String onlineMoney = map.get("total_fee");//交易金额，单位：分
			logger.info("wftPayCallback payId:{}, outTraceId:{}, onlineMoney:{}", payId, outTraceId, onlineMoney);
			IDistributedLock lock = new MemcachedDistributedLock();
			String uuid = null;
			String lockName = "alipay" + payId;
			try {
				uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
				if (uuid == null) {
					logger.error("其它线程也在对威富通订单【{}】进行操作， 获取不到锁", payId);
					response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
					return;
				}
				OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
				if (null != order) {
					String orderPayState = order.getPayState();
					String orderOutTraceId = order.getOutTraceId();
					logger.info("order payId:{}, payState:{}, orderOutTraceId:{}", payId, orderPayState, orderOutTraceId);
					if (OrderStateEnum.UNPAY.getOrderStateEn().equals(orderPayState)) {
						logger.info("威富通支付回调，修改订单{}的状态", order.getId());
						order.setPayType(PayEnum.wft_pay.getEn());
						order.setOutTraceId(outTraceId);
						order.setOnlineMoney(Integer.valueOf(onlineMoney) / 100.0);
						order.setPayTime(DateUtils.getSeconds());
						order.setTimeRemark(DateUtil.getOrderTimeRemark(order.getTimeRemark(), order.getSaleType()));
						orderService.updateEntitie(order);
						
						payService.orderPayCallback(order);
						
						// 通知乐玩咖平台
//						try {
//							notifyLwk(order);
//						} catch (Exception e) {
//							logger.info("通知乐玩咖失败");
//						}
						long costTime = System.currentTimeMillis() - startTime;
						logger.info("orderId:{} wftPayCallback costtime:{} ms", order.getId(), costTime);
						respToWeixin(response, "SUCCESS", "OK");
						return;
					} else {
						if (outTraceId.equals(orderOutTraceId)) {
							// 告诉微信服务器，我收到信息了，不要再重复回调了
							logger.warn("--------------重复通知--------------该订单{}已支付！状态为{}", payId, orderPayState);
						} else {
							logger.error("该订单{}已支付！状态为{}，但是交易流水号:{}与订单的交易流水号:{}不一致！", payId, orderPayState, outTraceId, orderOutTraceId);
						}
						respToWeixin(response, "SUCCESS", "OK");
						return;
					}
				} else {
					logger.error("威富通支付回调失败，未找到该订单{}", payId);
					respToWeixin(response, "FAIL", "");
					return;
				}
			} catch (Exception e) {
				logger.error("威富通支付回调处理失败，处理订单:{},异常:{}\n", payId, e);
				respToWeixin(response, "FAIL", "");
			} finally {
				if (uuid != null) {
					lock.releaseLock(lockName, uuid);
				}
			}
		} else {
			logger.error("威富通支付状态{}返回异常！！！", code);
			respToWeixin(response, "FAIL", "");
			return;
		}
	}
	
	/**
	 * 双乾支付回调-微信
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/ucfnotify.do")
	public void ucfPayCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("-------------进入双乾支付回调--------------");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		// 获取微信调用我们notify_url的返回信息
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("双乾支付回调内容:\n{}", result);
		if (StringUtil.isEmpty(result)) {
			logger.error("双乾支付回调内容为空!!!!");
			respToWeixin(response, "FAIL", "");
			return;
		}
		Map<String, String> map = JsonUtil.toMap(result);
		outSteam.close();
		inStream.close();
		String code = map.get("result_code");
		logger.info("双乾支付状态:{}", code);
		if ("SUCCESS".equals(code)) {
			String payId = map.get("out_trade_no");// 获取支付ID 等价订单号
//			String outTraceId = map.get("transaction_id");// 获取微信支付交易流水号
			String onlineMoney = map.get("total_fee");//交易金额，单位：分
			logger.info("ucfPayCallback payId:{}, onlineMoney:{}", payId, onlineMoney);
			IDistributedLock lock = new MemcachedDistributedLock();
			String uuid = null;
			String lockName = "ucfpay" + payId;
			try {
				uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
				if (uuid == null) {
					logger.error("其它线程也在对双乾支付订单【{}】进行操作， 获取不到锁", payId);
					response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
					return;
				}
				OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
				if (null != order) {
					String orderPayState = order.getPayState();
//					String orderOutTraceId = order.getOutTraceId();
					String orderPayId = order.getPayId();
					logger.info("order payId:{}, payState:{}", payId, orderPayState);
					if (OrderStateEnum.UNPAY.getOrderStateEn().equals(orderPayState)) {
						logger.info("双乾支付回调，修改订单{}的状态", order.getId());
						order.setPayType(PayEnum.ucf_pay.getEn());
//						order.setOutTraceId(outTraceId);
						order.setOnlineMoney(Integer.valueOf(onlineMoney) / 100.0);
						order.setPayTime(DateUtils.getSeconds());
						order.setTimeRemark(DateUtil.getOrderTimeRemark(order.getTimeRemark(), order.getSaleType()));
						orderService.updateEntitie(order);
						
						payService.orderPayCallback(order);
						long costTime = System.currentTimeMillis() - startTime;
						logger.info("orderId:{} ucfPayCallback costtime:{} ms", order.getId(), costTime);
						respToWeixin(response, "SUCCESS", "OK");
						return;
					} else {
						if (payId.equals(orderPayId)) {
							// 告诉微信服务器，我收到信息了，不要再重复回调了
							logger.warn("--------------重复通知--------------该订单{}已支付！状态为{}", payId, orderPayState);
						} else {
							logger.error("该订单{}已支付！状态为{}，", payId, orderPayState);
						}
						respToWeixin(response, "SUCCESS", "OK");
						return;
					}
				} else {
					logger.error("双乾支付回调失败，未找到该订单{}", payId);
					respToWeixin(response, "FAIL", "");
					return;
				}
			} catch (Exception e) {
				logger.error("双乾支付回调处理失败，处理订单:{},异常:{}\n", payId, e);
				respToWeixin(response, "FAIL", "");
			} finally {
				if (uuid != null) {
					lock.releaseLock(lockName, uuid);
				}
			}
		} else {
			logger.error("双乾支付状态{}返回异常！！！", code);
			respToWeixin(response, "FAIL", "");
			return;
		}
	}
	
	/**
	 * 双乾支付回调-微信
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/sqnotify.do")
	public void sqPayCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("-------------进入双乾支付回调--------------");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		// 获取微信调用我们notify_url的返回信息
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("双乾支付回调内容:\n{}", result);
		if (StringUtil.isEmpty(result)) {
			logger.error("双乾支付回调内容为空!!!!");
			respToWeixin(response, "FAIL", "");
			return;
		}
		Map<String, String> map = JsonUtil.toMap(result);
		outSteam.close();
		inStream.close();
		String code = map.get("result_code");
		logger.info("双乾支付状态:{}", code);
		if ("SUCCESS".equals(code)) {
			String payId = map.get("out_trade_no");// 获取支付ID 等价订单号
			String outTraceId = map.get("transaction_id");// 获取微信支付交易流水号
			String onlineMoney = map.get("total_fee");//交易金额，单位：元
			logger.info("sqPayCallback payId:{}, onlineMoney:{}", payId, onlineMoney);
			IDistributedLock lock = new MemcachedDistributedLock();
			String uuid = null;
			String lockName = "sqpay" + payId;
			try {
				uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
				if (uuid == null) {
					logger.error("其它线程也在对双乾支付订单【{}】进行操作， 获取不到锁", payId);
					response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
					return;
				}
				OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
				if (null != order) {
					String orderPayState = order.getPayState();
//					String orderOutTraceId = order.getOutTraceId();
					String orderPayId = order.getPayId();
					logger.info("order payId:{}, payState:{}", payId, orderPayState);
					if (OrderStateEnum.UNPAY.getOrderStateEn().equals(orderPayState)) {
						logger.info("双乾支付回调，修改订单{}的状态", order.getId());
//						order.setPayType(PayEnum.ucf_pay.getEn());
						order.setOutTraceId(outTraceId);
						order.setOnlineMoney(Double.valueOf(onlineMoney));
						order.setPayTime(DateUtils.getSeconds());
						order.setTimeRemark(DateUtil.getOrderTimeRemark(order.getTimeRemark(), order.getSaleType()));
						orderService.updateEntitie(order);
						
						payService.orderPayCallback(order);
						long costTime = System.currentTimeMillis() - startTime;
						logger.info("orderId:{} sqPayCallback costtime:{} ms", order.getId(), costTime);
						respToWeixin(response, "SUCCESS", "OK");
						return;
					} else {
						if (payId.equals(orderPayId)) {
							// 告诉微信服务器，我收到信息了，不要再重复回调了
							logger.warn("--------------重复通知--------------该订单{}已支付！状态为{}", payId, orderPayState);
						} else {
							logger.error("该订单{}已支付！状态为{}，", payId, orderPayState);
						}
						respToWeixin(response, "SUCCESS", "OK");
						return;
					}
				} else {
					logger.error("双乾支付回调失败，未找到该订单{}", payId);
					respToWeixin(response, "FAIL", "");
					return;
				}
			} catch (Exception e) {
				logger.error("双乾支付回调处理失败，处理订单:{},异常:{}\n", payId, e);
				respToWeixin(response, "FAIL", "");
			} finally {
				if (uuid != null) {
					lock.releaseLock(lockName, uuid);
				}
			}
		} else {
			logger.error("双乾支付状态{}返回异常！！！", code);
			respToWeixin(response, "FAIL", "");
			return;
		}
	}

	/**
	 * 支付宝支付回调处理
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/alipaynotify.do")
	public void aliPayPayCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("-------------进入支付宝支付回调--------------");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		// 获取微信调用我们notify_url的返回信息
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("支付宝支付回调内容:\n{}", result);
		if (StringUtil.isEmpty(result)) {
			logger.error("支付宝支付回调内容为空!!!!");
			respToWeixin(response, "FAIL", "");
			return;
		}
		// Map<String, String> map = XMLUtil.doXMLParse(result);
		Map<String, String> map = JsonUtil.toMap(result);
		outSteam.close();
		inStream.close();
		String code = map.get("result_code");
		logger.info("支付宝支付状态:{}", code);
		
		if ("SUCCESS".equals(code)) {
			// 获取支付ID 等价订单号
			String payId = map.get("out_trade_no");
			// 获取微信支付交易流水号
			String outTraceId = map.get("transaction_id");
			String buyerId = map.get("buyer_id");
			String totalFee = map.get("total_fee");
			
			//供应链H5页面，支付宝回调用到body
			String body = "";
			String detail = map.get("detail");
			Map<String, String> stringToMap = JsonUtil.toMap(detail);
			if(stringToMap!=null){
				body = stringToMap.get("body");  //作为备用字段
			}
			logger.info("payId:{},outTraceId:{},buyerId:{},totalFee:{},body:{}", payId, outTraceId, buyerId, totalFee, body);

			IDistributedLock lock = new MemcachedDistributedLock();
			String uuid = null;
			String lockName = "alipay_callback_" + payId;
			try {
				uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
				if (uuid == null) {
					logger.error("其它线程也在对支付宝扫码订单【{}】进行操作， 获取不到锁", payId);
					response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
					return;
				}
				
				//供应链H5页面，支付宝支付回调
				if("supply".equals(body)){
					logger.info("《1.5项目》供应链H5页面，支付宝支付回调,begin...");
					AjaxJson json = wmApiCall.aliConfirmPay(payId, outTraceId, totalFee);
					if(json.isSuccess()){
						logger.error("《1.5项目》支付宝扫码支付回调成功，订单号：{}", payId);
						respToWeixin(response, "SUCCESS", "OK");
					}else{
						logger.error("《1.5项目》支付宝扫码支付回调失败，订单号：{}", payId);
						respToWeixin(response, "FAIL", "");
					}
					return;
				}

				OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
				if (null != order) {
					String orderPayState = order.getPayState();
					String orderOutTraceId = order.getOutTraceId();
					logger.info("payId:{}, payState:{}", payId, orderPayState);
					if (OrderStateEnum.UNPAY.getOrderStateEn().equals(orderPayState)) {
						logger.info("支付宝支付回调，修改订单{}的状态", order.getId());
						order.setPayType(PayEnum.alipay.getEn());
						order.setOnlineMoney(Double.valueOf(totalFee));
						order.setPayTime(DateUtils.getSeconds());
						order.setOutTraceId(outTraceId);
						order.setTimeRemark(DateUtil.getOrderTimeRemark(order.getTimeRemark(), order.getSaleType()));
						orderService.updateEntitie(order);

						payService.orderAlipayScanDone(order, buyerId);

						long costTime = System.currentTimeMillis() - startTime;
						logger.info("aliPayPayCallback costtime:{} ms", costTime);
						
						respToWeixin(response, "SUCCESS", "OK");
						return;
					} else {
						if (outTraceId.equals(orderOutTraceId)) {
							logger.warn("--------------重复通知--------------该订单{}已支付！状态为{}", payId, orderPayState);
						} else {
							logger.error("该订单{}已支付！状态为{}，但是交易流水号:{}与订单的交易流水号:{}不一致！", payId, orderPayState, outTraceId, orderOutTraceId);
						}
						respToWeixin(response, "SUCCESS", "OK");
						return;
					}
				} else {
					logger.error("支付宝扫码支付回调失败，未找到该订单{}", payId);
					respToWeixin(response, "FAIL", "");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("支付宝扫码支付回调失败，处理异常，payId:{}", payId);
				respToWeixin(response, "FAIL", "");
			} finally {
				if (uuid != null) {
					lock.releaseLock(lockName, uuid);
				}
			}
		} else {
			logger.error("支付宝扫码支付状态{}返回异常！！！", code);
			respToWeixin(response, "FAIL", "");
			return;
		}
	}
	
	/**
	 * QQ支付回调处理
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/qqPayNotify.do")
	public void qqPayCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("-------------进入QQ钱包支付回调--------------");
		//商户号
		String partner = "1335096801";
		//密钥
		String key = "015fcf61ccdf0151070439e27b6db9b5";
		//创建支付应答对象
		ResponseHandler resHandler = new ResponseHandler(request, response);
		resHandler.setKey(key);
		//判断签名
		if(resHandler.isTenpaySign()) {
			
			Map<String, Object> paramsMaps = resHandler.getAllParameters();
			for(Map.Entry<String, Object> param : paramsMaps.entrySet()){
				logger.info(param.getKey()+">>>>>>>>"+param.getValue());
			}
			//通知id
			String notify_id = resHandler.getParameter("notify_id");
			
			//创建请求对象
			RequestHandler queryReq = new RequestHandler(null, null);
			//通信对象
			TenpayHttpClient httpClient = new TenpayHttpClient();
			//应答对象
			ClientResponseHandler queryRes = new ClientResponseHandler();
			
			//通过通知ID查询，确保通知来至财付通
			queryReq.init();
			queryReq.setKey(key);
			queryReq.setGateUrl("https://gw.tenpay.com/gateway/verifynotifyid.xml");
			queryReq.setParameter("partner", partner);
			queryReq.setParameter("notify_id", notify_id);
			
			//通信对象
			httpClient.setTimeOut(5);
			//设置请求内容
			httpClient.setReqContent(queryReq.getRequestURL());
			System.out.println("queryReq:" + queryReq.getRequestURL());
			//后台调用
			if(httpClient.call()) {
				//设置结果参数
				queryRes.setContent(httpClient.getResContent());
				System.out.println("queryRes:" + httpClient.getResContent());
				queryRes.setKey(key);
					
					
				//获取返回参数
				String retcode = queryRes.getParameter("retcode");
				String trade_state = queryRes.getParameter("trade_state");
			
				String trade_mode = queryRes.getParameter("trade_mode");
					
				//判断签名及结果
				if(queryRes.isTenpaySign()&& "0".equals(retcode) && "0".equals(trade_state) && "1".equals(trade_mode)) {
					System.out.println("订单查询成功");
					//取结果参数做业务处理				
					System.out.println("out_trade_no:" + queryRes.getParameter("out_trade_no")+
							" transaction_id:" + queryRes.getParameter("transaction_id"));
					System.out.println("trade_state:" + queryRes.getParameter("trade_state")+
							" total_fee:" + queryRes.getParameter("total_fee"));
				        //如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
					System.out.println("discount:" + queryRes.getParameter("discount")+
							" time_end:" + queryRes.getParameter("time_end"));
					
					
					// 获取支付ID 等价订单号
					String payId = queryRes.getParameter("out_trade_no");
					// 获取微信支付交易流水号
					String outTraceId =queryRes.getParameter("transaction_id");
					//String buyerId = map.get("buyer_id");
					String totalFee = queryRes.getParameter("total_fee");
					logger.info("payId:{}", payId);
					logger.info("outTraceId:{}", outTraceId);
					//logger.info("buyerId:{}", buyerId);
					logger.info("totalFee:{}", totalFee);
					
					IDistributedLock lock = new MemcachedDistributedLock();
					String uuid = null;
					String lockName = "qqpay" + payId;
					try {
						uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
						if (uuid == null) {
							logger.error("其它线程也在对qq扫码订单【{}】进行操作， 获取不到锁", payId);
							throw new RuntimeException("其它线程也在对qq扫码订单【" + payId + "】进行操作， 获取不到锁");
						}
						
						OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
						if (null != order) {
							String orderPayState = order.getPayState();
							String orderOutTraceId = order.getOutTraceId();
							logger.info("payId:{}, payState:{}", payId, orderPayState);
							if (OrderStateEnum.UNPAY.getOrderStateEn().equals(orderPayState)) {
								logger.info("qq支付回调，修改订单{}的状态", order.getId());
								String orderNum = AliOcs.genOrderNum(order.getMerchant().getId().toString());
								order.setOrderNum(orderNum);
								order.setState(OrderStateEnum.PAY.getOrderStateEn());
								order.setPayType(PayEnum.tenpay.getEn());
								order.setPayState(OrderStateEnum.PAY.getOrderStateEn());
								order.setOnlineMoney(Double.valueOf(totalFee));
								order.setPayTime(DateUtils.getSeconds());
								order.setOutTraceId(outTraceId);
								order.setTimeRemark(DateUtil.getOrderTimeRemark(order.getTimeRemark(), order.getSaleType()));
								orderService.updateEntitie(order);
								
								payService.orderQQpayScanDone(order.getId());
								
								respToWeixin(response, "SUCCESS", "OK");
								return;
							} else {
								if (outTraceId.equals(orderOutTraceId)) {
									logger.warn("--------------重复通知--------------该订单{}已支付！状态为{}", payId, orderPayState);
								} else {
									logger.error("该订单{}已支付！状态为{}，但是交易流水号:{}与订单的交易流水号:{}不一致！", payId, orderPayState, outTraceId, orderOutTraceId);
								}
								respToWeixin(response, "SUCCESS", "OK");
								return;
							}
						} else {
							logger.error("支付宝扫码支付回调失败，未找到该订单{}", payId);
							respToWeixin(response, "FAIL", "");
							return;
						}
						
					} catch (Exception e) {
						logger.error("支付宝扫码支付回调失败，未找到该订单{}", payId);
						respToWeixin(response, "FAIL", "");
					} finally {
						if (uuid != null) {
							lock.releaseLock(lockName, uuid);
						}
					}
					
					resHandler.sendToCFT("Success");
				}
				else{
						//错误时，返回结果未签名，记录retcode、retmsg看失败详情。
						System.out.println("查询验证签名失败或业务错误");
						System.out.println("retcode:" + queryRes.getParameter("retcode")+
								" retmsg:" + queryRes.getParameter("retmsg"));
				}
			
			} else {

				System.out.println("后台调用通信失败");
					
				System.out.println(httpClient.getResponseCode());
				System.out.println(httpClient.getErrInfo());
				//有可能因为网络原因，请求已经处理，但未收到应答。
			}
		}
		else{
			System.out.println("通知签名验证失败");
		}
		
		
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		// 获取微信调用我们notify_url的返回信息
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("QQ钱包支付回调内容:\n{}", result);
		if (StringUtil.isEmpty(result)) {
			logger.error("QQ钱包支付回调内容为空!!!!");
			respToWeixin(response, "FAIL", "");
			return;
		}
		// Map<String, String> map = XMLUtil.doXMLParse(result);
		Map<String, String> map = JsonUtil.toMap(result);
		outSteam.close();
		inStream.close();
		String code = map.get("result_code");
		logger.info("支付宝支付状态:{}", code);
		if ("SUCCESS".equals(code)) {
			// 获取支付ID 等价订单号
			String payId = map.get("out_trade_no");
			// 获取微信支付交易流水号
			String outTraceId = map.get("transaction_id");
			String buyerId = map.get("buyer_id");
			String totalFee = map.get("total_fee");
			logger.info("payId:{}", payId);
			logger.info("outTraceId:{}", outTraceId);
			logger.info("buyerId:{}", buyerId);
			logger.info("totalFee:{}", totalFee);
			
			IDistributedLock lock = new MemcachedDistributedLock();
			String uuid = null;
			String lockName = "alipay" + payId;
			try {
				uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
				if (uuid == null) {
					logger.error("其它线程也在对支付宝扫码订单【{}】进行操作， 获取不到锁", payId);
					throw new RuntimeException("其它线程也在对支付宝扫码订单【" + payId + "】进行操作， 获取不到锁");
				}
				
				OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
				if (null != order) {
					String orderPayState = order.getPayState();
					String orderOutTraceId = order.getOutTraceId();
					logger.info("payId:{}, payState:{}", payId, orderPayState);
					if (OrderStateEnum.UNPAY.getOrderStateEn().equals(orderPayState)) {
						logger.info("支付宝支付回调，修改订单{}的状态", order.getId());
						order.setPayType(PayEnum.tenpay.getEn());
						order.setOnlineMoney(Double.valueOf(totalFee));
						order.setPayTime(DateUtils.getSeconds());
						order.setOutTraceId(outTraceId);
						order.setTimeRemark(DateUtil.getOrderTimeRemark(order.getTimeRemark(), order.getSaleType()));
						orderService.updateEntitie(order);
						
						payService.orderAlipayScanDone(order, buyerId);
						
						respToWeixin(response, "SUCCESS", "OK");
						return;
					} else {
						if (outTraceId.equals(orderOutTraceId)) {
							// 告诉微信服务器，我收到信息了，不要再重复回调了
							logger.warn("--------------重复通知--------------该订单{}已支付！状态为{}", payId, orderPayState);
						} else {
							logger.error("该订单{}已支付！状态为{}，但是交易流水号:{}与订单的交易流水号:{}不一致！", payId, orderPayState, outTraceId, orderOutTraceId);
						}
						respToWeixin(response, "SUCCESS", "OK");
						return;
					}
				} else {
					logger.error("支付宝扫码支付回调失败，未找到该订单{}", payId);
					respToWeixin(response, "FAIL", "");
					return;
				}
				
			} catch (Exception e) {
				logger.error("支付宝扫码支付回调失败，未找到该订单{}", payId);
				respToWeixin(response, "FAIL", "");
			} finally {
				if (uuid != null) {
					lock.releaseLock(lockName, uuid);
				}
			}
		} else {
			logger.error("支付宝扫码支付状态{}返回异常！！！");
			respToWeixin(response, "FAIL", "");
			return;
		}
	}
	
	/**
	 * 返回给微信的消息
	 * @param code SUCCESS/FAIL
	 * @param msg
	 * @return
	 * @throws IOException 
	 */
	public static void respToWeixin(HttpServletResponse response, String code, String msg) throws IOException {
		response.getWriter().write("<xml><return_code><![CDATA[" + code
				+ "]]></return_code><return_msg><![CDATA[" + msg
				+ "]]></return_msg></xml>");
	}

	/**
	 * 乐玩咖 - 已停止合作
	 * @param order
	 */
	@SuppressWarnings("unused")
	private void notifyLwk(OrderEntity order) {
		StringBuilder sb = new StringBuilder();
		OrderChannel orderChannel = orderChannelService.getEntityByOrderId(order.getId());
		if (StringUtil.isNotEmpty(orderChannel) && "N".equals(orderChannel.getChannelFlag())) {
			String lwkId = orderChannel.getChannelId();
			String openId = order.getWuser().getOpenId();
			Double money = order.getOnlineMoney();
			Integer payTime = order.getPayTime();
			sb.append("ID=").append(lwkId).append("&money=").append(money).append("&openId=").append(openId)
					.append("&pay_time=").append(payTime).append("&hantolewanka");
			String sign = MD5.GetMD5Code(sb.toString());
			logger.info("sb====" + sb.toString());
			logger.info("sign====" + sign);
			JSONObject params = new JSONObject();
			params.put("ID", lwkId);// 乐玩咖平台用户的标识id
			params.put("openId", openId);
			params.put("money", money);
			params.put("pay_time", order.getPayDate());
			params.put("sign", sign);
			String LWK_URL = "http://www.hogoho.com";
			JSONObject ret = HttpUtils.post(LWK_URL, params, true, false);
			String state = ((JSONObject) ret.get("result")).get("state").toString();
			if (StringUtil.isNotEmpty(ret) && "1".equals(state)) {
				logger.error("乐玩咖返回成功");
				orderChannel.setChannelFlag("Y");
				orderChannelService.updateEntitie(orderChannel);
			} else {
				logger.error("乐玩咖返回失败");
			}
		}
	}
	
	
	/**
	 * 铂金支付回调处理 - 动态码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/ptnotify.do")
	public void ptPayCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		logger.info("-------------进入铂金支付回调--------------");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		String result = new String(outSteam.toByteArray(), "utf-8");
		logger.info("铂金支付回调内容:\n{}", result);
		if (StringUtil.isEmpty(result)) {
			logger.error("铂金支付回调内容为空!!!!");
			respToWeixin(response, "FAIL", "");
			return;
		}
		Map<String, String> map = JsonUtil.toMap(result);
		outSteam.close();
		inStream.close();
		String code = map.get("result_code");
		logger.info("铂金支付状态:{}", code);
		if ("SUCCESS".equals(code)) {
			String payId = map.get("out_trade_no");// 获取支付ID 等价订单号
//			String outTraceId = map.get("transaction_id");// 获取微信支付交易流水号
			String onlineMoney = map.get("total_fee");//交易金额，单位：元
			logger.info("ptPayCallback payId:{}, onlineMoney:{} 元", payId, onlineMoney);
			IDistributedLock lock = new MemcachedDistributedLock();
			String uuid = null;
			String lockName = "ptPayCallback_" + payId;
			try {
				uuid = lock.tryAcquireLock(lockName, EXPIRE, TRY_TIME);
				if (uuid == null) {
					logger.error("其它线程也在对威富通订单【{}】进行操作， 获取不到锁", payId);
					response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
					return;
				}
				OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
				if (null != order) {
					String orderPayState = order.getPayState();
					String orderOutTraceId = order.getOutTraceId();
					logger.info("order payId:{}, payState:{}, orderOutTraceId:{}", payId, orderPayState, orderOutTraceId);
					if (OrderStateEnum.UNPAY.getOrderStateEn().equals(orderPayState)) {
						logger.info("铂金支付回调，修改订单{}的状态", order.getId());
//						order.setPayType(PayEnum.wft_pay.getEn());
//						order.setOutTraceId(outTraceId);
						order.setOnlineMoney(Double.valueOf(onlineMoney));
						order.setPayTime(DateUtils.getSeconds());
						order.setTimeRemark(DateUtil.getOrderTimeRemark(order.getTimeRemark(), order.getSaleType()));
						orderService.updateEntitie(order);
						
						payService.orderPayCallback(order);
						
						long costTime = System.currentTimeMillis() - startTime;
						logger.info("orderId:{} ptPayCallback costtime:{} ms", order.getId(), costTime);
						respToWeixin(response, "SUCCESS", "OK");
						return;
					} else {
						respToWeixin(response, "SUCCESS", "OK");
						return;
					}
				} else {
					logger.error("铂金支付回调失败，未找到该订单{}", payId);
					respToWeixin(response, "FAIL", "");
					return;
				}
			} catch (Exception e) {
				logger.error("铂金支付回调处理失败，处理订单:{},异常:{}\n", payId, e);
				respToWeixin(response, "FAIL", "");
			} finally {
				if (uuid != null) {
					lock.releaseLock(lockName, uuid);
				}
			}
		} else {
			logger.error("铂金支付状态{}返回异常！！！", code);
			respToWeixin(response, "FAIL", "");
			return;
		}
	}
}
