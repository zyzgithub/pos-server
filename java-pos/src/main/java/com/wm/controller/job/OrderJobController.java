package com.wm.controller.job;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.controller.open_api.OpenResult.State;
import com.wm.entity.order.FlashOrderReturnEntity;
import com.wm.entity.order.OrderEntity;
import com.wm.service.merchant.MerchantServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.order.PushedOrderServiceI;
import com.wm.service.order.scamble.ScambleAlgorithmServiceI;
import com.wm.service.orderincome.OrderIncomeServiceI;
import com.wm.util.SafeUtil;

@Controller
@RequestMapping("/jobController/order")
public class OrderJobController {
	
	private final static Logger logger = LoggerFactory.getLogger(OrderJobController.class);

	@Autowired
	OrderServiceI orderService;
	@Autowired
	MerchantServiceI merchantService;
	@Autowired
	OrderIncomeServiceI orderIncomeService;
	@Autowired
	PushedOrderServiceI pushedOrderService;
	@Autowired
	ScambleAlgorithmServiceI scambleAlgorithmService;
	
//	private static final int EXPIRED_HOURS = 24;
	
	@Value("${redis_password}")
	private String password;


	@Value("${is_using_new_algorithm}")
	private boolean isUsingNewAlgorithm;
	
	/**
	 * 监控可抢订单
	 */
	@RequestMapping("/monitor")
	@ResponseBody
	public AjaxJson monitor(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		try {
			if(isUsingNewAlgorithm){
				scambleAlgorithmService.executeRepushNew();
			}
			else {
				pushedOrderService.executeRepush();
			}
			j.setSuccess(true);
			j.setMsg("监控可抢订单完成");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
			logger.error("监控可抢订单失败！", e);
		} 
		return j;
	}
	
	/**
	 * 监控未接单订单
	 */
	@RequestMapping("/monitorRefund")
	@ResponseBody
	public AjaxJson monitorRefund(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		
		logger.info("15分钟内商家未接单自动退单");
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		/**
		 * 查询所有私厨订单信息
		 * fromType 0:众包订单  1：用户订单 2：闪购订单
		 */
		List<Map<String, Object>> list = orderService.getOrderTimer();
		if(list != null && list.size() > 0){
			int size = list.size();
			logger.info("monitorRefund total size:" + size);
			for(int i=0;i<size;i++){
				logger.info("monitorRefund current index:" + i);
				Map<String, Object> orderMap = list.get(i);
				int id = Integer.parseInt(orderMap.get("id").toString());
				int orderId =Integer.parseInt(orderMap.get("orderId").toString());
				int merchantid = Integer.parseInt(orderMap.get("merchantId").toString());
				int fromType = Integer.parseInt(orderMap.get("fromType").toString());
				String creatTime = orderMap.get("createTime").toString();
				
				try {		
					Long time = (format.parse(format.format(date)).getTime() - format.parse(creatTime).getTime()) / (60*1000);
					Integer minute = time.intValue();
					boolean b = false;
					
					if(fromType == 1){
						if(minute >= 15){
							b = orderService.kitMerchantUnAcceptOrder(orderId,merchantid,"商家15分钟内未接单");
						}
					}else if(fromType == 0 ){
						if(minute >=60){  //1小时内快递员未接单，取消订单
							b = orderService.autoCancelCrowdsourcingOrder(orderId,merchantid);
						}
					}else if(fromType == 2){//闪购订单，3天不处理自动退款
						if(minute >= 4320){//60*24*3=4320 
							orderService.flashOrderAcceptRefundApply(orderId);//自动同意退款申请
							FlashOrderReturnEntity flash = orderService.findUniqueByProperty(FlashOrderReturnEntity.class, "orderId", orderId);
							//0:退款,1:退货退款
							if('0'==flash.getType()){
								b = orderService.flashOrderAcceptRefund(orderId,merchantid);
							}else if('1'==flash.getType()){
								b = true;
							}
						}
					}
					if(b){
						/**
						 * 查一个删一个
						 */
						orderService.deleteOrderTimer(id);
					}		
				} catch(Exception e){
					logger.error("自动退单失败, orderId=" + orderId, e);
				}
			}
		}
		j.setSuccess(true);
		j.setMsg("监控未接单订单完成");
		return j;
	}
	
	/**
	 * 重置订单排号
	 */
	@RequestMapping("/resetOrderNum")
	@ResponseBody
	public AjaxJson resetOrderNum(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		
		/*List<Map<String, Object>> merchantList = merchantService.findByDelState(0);
		int size = merchantList.size();
		logger.info("resetOrderNum total size:" + size);
		for(Map<String, Object> merchant : merchantList){
			logger.info("resetOrderNum current index:" + size--);
			String merchatId = merchant.get("id").toString();
			try {
				AliOcs.resetOrderNum(merchatId);
			} catch (Exception e) {
				logger.error("重置商家{}订单排号失败!", merchatId);
				e.printStackTrace();
			}
		}*/
		j.setSuccess(true);
		j.setMsg("重置订单排号已经取消！！！");
		return j;
	}
	
	/**
	 * 批量结算订单
	 */
	@RequestMapping("/settlement")
	@ResponseBody
	public AjaxJson settlement(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		Double money = orderIncomeService.getTotalMoney4JZ();
		logger.info("before settlement jz total money:{}", String.format("%.4f", money));
		List<Map<String, Object>> settleList = orderIncomeService.findSettleList();
		int size = settleList.size();
		logger.info("settlement total size:{}", size);
		for(Map<String, Object> settleMap : settleList){
			Integer orderIncomeId = Integer.valueOf(settleMap.get("id").toString());
			logger.info("settlement current index:{} orderIncomeId:{}", size--, orderIncomeId);
			try {
				orderIncomeService.settleMent(orderIncomeId);
			} catch (Exception e) {
				logger.error("预收入{}结算失败!", orderIncomeId);
				e.printStackTrace();
			}
		}
		j.setSuccess(true);
		j.setMsg("订单结算完成!!");
		return j;
	}
	
	/**
	 * 订单自动完成
	 */
	@RequestMapping("/completeOrder")
	@ResponseBody
	public AjaxJson completeOrder(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		List<Map<String, Object>> orderList = orderService.findNeedCompleteList();
		int size = orderList.size();
		logger.info("completeOrder total size:" + size);
		for(Map<String, Object> orderMap : orderList){
			logger.info("completeOrder current index:" + size--);
			OrderEntity order = new OrderEntity();
			order.setId(Integer.valueOf(orderMap.get("id").toString()));
			Object courierId = orderMap.get("courier_id");
			if(courierId != null){
				order.setCourierId(Integer.parseInt(courierId.toString()));
			}
			try {
				orderService.autoCompleteOrder(order);
			} catch (Exception e) {
				logger.error("更新订单失败，订单id:" + order.getId(), e);
			}
		}
		j.setSuccess(true);
		j.setMsg("订单结算完成!!");
		return j;
	}

}
