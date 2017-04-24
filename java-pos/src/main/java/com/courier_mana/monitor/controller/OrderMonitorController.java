package com.courier_mana.monitor.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.base.enums.AppTypeConstants;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.monitor.service.OrderMonitorService;
import com.courier_mana.personal.service.CourierMyInfoService;
import com.courier_mana.statistics.service.CourierCommentService;
import com.jpush.JPush;
import com.jpush.SoundFile;

@Controller
@RequestMapping("/ci/courier/admin/orderMonitor")
public class OrderMonitorController extends BasicController {

	private static Logger logger = LoggerFactory.getLogger(OrderMonitorController.class);
	
	@Autowired
	private OrderMonitorService orderMonitorService;
	@Autowired
	private CourierCommentService courierCommentService;
	@Autowired
	private CourierOrgServicI courierOrgServicI;
	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	/**
	 * 未下单 custType 为客户等级
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getNosendOrder")
	@ResponseBody
	public AjaxJson getNosendOrder(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam Integer courierId) {
		AjaxJson ajaxJson = null;
		logger.info("invoke method getNosendOrder ,params:{}{}{}", page, rows,
				courierId);
		List<Map<String, Object>> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (page == null && rows == null) {
				page = 1;
				rows = 2;
			}
			if (courierId == null) {
				ajaxJson = FAIL("01", "参数错误！");
			}
			
			Boolean falg = this.courierMyInfoService.isAgentUser(courierId);
			
			List<Integer> merchantIs = null;
			if(falg){
				merchantIs = orderMonitorService.getPartnerMerchantIds(courierId);
			}else{
				merchantIs = orderMonitorService.getMerchantIds(courierId);
			}
			
			//List<Integer> merchantIdsTwo = orderMonitorService.getMerchantIdsTwo(courierId); // 
			if (merchantIs.size() == 0) {
				ajaxJson = FAIL("02", "没有管理商家！");
			} else {

				list = orderMonitorService.getNosendOrder(page, rows, merchantIs);
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						map = list.get(i);
						
						/**
						 * 转换订单Map中的送货时间(timeRemark --> timeStamp)
						 */
						this.timeRemark2second(map);
						
						Integer userId = ((BigInteger) map.get("userId"))
								.intValue();
						// 用户等级
						String custType = courierCommentService
								.getCustType(userId);
						map.put("custType", custType);

						// 判断是否 为私厨，众包，电话订单 在 orderType 只允许一种存在，优先级为 私厨>众包>电话订单
						Integer merchantId = ((BigInteger) map
								.get("merchantId")).intValue();
						String crowdSourse = (String) map.get("orderFrom");
						String orderType = (String)map.get("orderType");
						//保证orderType 只返回4中状态
						if(!"mobile".equals(orderType)){
							map.put("orderType", "normal");
						}
						Map<String, Object> privateOrder = orderMonitorService
								.getPrivateOrder(merchantId);
						Integer type = null;
						if (privateOrder != null) {
							type = (Integer) privateOrder.get("private");
						}
						if ("crowdsourcing".equals(crowdSourse)) {
							Map<String, Object> merchantDetail = orderMonitorService
									.getMerchantDetail(merchantId);
							map.put("orderType", "crowd");
							map.put("merchantUsername", (String) merchantDetail
									.get("merchantUsername"));
							map.put("merchantMobile", (String) merchantDetail
									.get("merchantMobile"));
							map.put("merchantAddress", (String) merchantDetail
									.get("merchantAddress"));
						}
						if (type != null && type == 2) {
							map.put("orderType", "private");
						}

					}
				}
				ajaxJson = SUCCESS(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", e.getMessage());
		}
		return ajaxJson;
	}

	/**
	 * 
	 * 获取超过十五分钟订单列表
	 * 
	 * @param page
	 * @param rows
	 * @param courierId
	 * @return
	 */
	@RequestMapping("/getCastFifteen")
	@ResponseBody
	public AjaxJson getCastFifteen(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam Integer courierId) {
		AjaxJson ajaxJson = null;
		logger.info("invoke method getCastFifteen ,params:{}{}{}", page, rows,
				courierId);
		List<Map<String, Object>> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (page == null && rows == null) {
				page = 1;
				rows = 2;
			}
			if (courierId == null) {
				ajaxJson = FAIL("01", "参数错误！");
			}

			list = orderMonitorService.getCastFifteen(courierId, page, rows);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					map = list.get(i);
					
					/**
					 * 转换订单Map中的送货时间(timeRemark --> timeStamp)
					 */
					this.timeRemark2second(map);
					
					Integer userId = ((BigInteger) map.get("userId"))
							.intValue();
					// 用户等级
					String custType = courierCommentService.getCustType(userId);
					map.put("custType", custType);

				}
			}
			ajaxJson = SUCCESS(list);
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", e.getMessage());
		}
		return ajaxJson;
	}

	/**
	 * 
	 * 获取超过三十分钟订单列表
	 * 
	 * @param page
	 * @param rows
	 * @param courierId
	 * @return
	 */
	@RequestMapping("/getCastThirdty")
	@ResponseBody
	public AjaxJson getCastThirdty(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam Integer courierId) {
		AjaxJson ajaxJson = null;
		logger.info("invoke method getCastThirdty ,params:{}{}{}", page, rows,
				courierId);
		List<Map<String, Object>> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (page == null && rows == null) {
				page = 1;
				rows = 2;
			}
			if (courierId == null) {
				ajaxJson = FAIL("01", "参数错误！");
			}

			list = orderMonitorService.getCastThirdty(courierId, page, rows);
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					map = list.get(i);
					
					/**
					 * 转换订单Map中的送货时间(timeRemark --> timeStamp)
					 */
					this.timeRemark2second(map);
					
					Integer userId = ((BigInteger) map.get("userId")).intValue();
					// 用户等级
					String custType = courierCommentService.getCustType(userId);
					map.put("custType", custType);

				}
			}
			ajaxJson = SUCCESS(list);

		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", e.getMessage());
		}
		return ajaxJson;
	}

	/**
	 * 
	 * 获取催单订单列表
	 * 
	 * @param page
	 * @param rows
	 * @param courierId
	 * @return
	 */
	@RequestMapping("/getUrgeOrder")
	@ResponseBody
	public AjaxJson getUrgeOrder(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam Integer courierId) {
		AjaxJson ajaxJson = null;
		logger.info("invoke method getCastThirdty ,params:{}{}{}", page, rows,
				courierId);
		List<Map<String, Object>> list = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (page == null && rows == null) {
				page = 1;
				rows = 2;
			}
			if (courierId == null) {
				ajaxJson = FAIL("01", "参数错误！");
			}
			
			List<Integer> merchantIs = null;
			Boolean falg = courierMyInfoService.isAgentUser(courierId);
			if(falg){
				merchantIs = orderMonitorService.getPartnerMerchantIds(courierId);
			}else{
				merchantIs = orderMonitorService.getMerchantIds(courierId);
			}
			
			if (merchantIs.size() == 0) {
				ajaxJson = FAIL("02", "没有管理商家！");
			} else {

				list = orderMonitorService.getUrgeOrder(page, rows, merchantIs);
				for (int i = 0; i < list.size(); i++) {
					map = list.get(i);
					
					/**
					 * 转换订单Map中的送货时间(timeRemark --> timeStamp)
					 */
					this.timeRemark2second(map);
					
					Integer userId = ((BigInteger) map.get("userId"))
							.intValue();
					// 用户等级
					String custType = courierCommentService.getCustType(userId);
					map.put("custType", custType);

				}
				ajaxJson = SUCCESS(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", e.getMessage());
		}
		logger.info("return result：{}", JSON.toJSON(ajaxJson));
		return ajaxJson;
	}

	/**
	 * 
	 * 获取管辖下的快递员
	 * 
	 * @param page
	 * @param rows
	 * @param courierId
	 * @return
	 */
	@RequestMapping("/getCourier")
	@ResponseBody
	public AjaxJson getCourier(@RequestParam Integer courierId) {
		AjaxJson ajaxJson = null;
		logger.info("invoke method getCourier ,params:{}", courierId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			if (courierId == null) {
				ajaxJson = FAIL("01", "参数错误！");
			}
			List<Map<String, Object>> couriers = null;
			boolean flag = this.courierMyInfoService.isAgentUser(courierId);
			if(flag){
				couriers = courierOrgServicI.getPartnerCouriers(courierId);
			}else{
				couriers = courierOrgServicI.getManageCouriers(courierId);
			}
			
			
			if (couriers.size() == 0) {
				ajaxJson = FAIL("02", "没有能指派的快递员！");
			} else {

				for (int i = 0; i < couriers.size(); i++) {
					Map<String, Object> map1 = couriers.get(i);
					Integer courierIdl = ((BigInteger) map1.get("id"))
							.intValue();
					String courierName = (String) map1.get("username");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("courierId", courierIdl);
					map.put("courierName", courierName);
					list.add(map);
				}
				ajaxJson = SUCCESS(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", e.getMessage());
		}
		logger.info("return result：{}", JSON.toJSON(ajaxJson));
		return ajaxJson;
	}

	/**
	 * 
	 * 获取催单数量
	 * 
	 * @param page
	 * @param rows
	 * @param courierId
	 * @return
	 */
	@RequestMapping("/getUrgeCount")
	@ResponseBody
	public AjaxJson getUrgeCount(@RequestParam Integer courierId) {
		AjaxJson ajaxJson = null;
		logger.info("invoke method getUrgeCount ,params:{}", courierId);

		try {

			if (courierId == null) {
				ajaxJson = FAIL("01", "参数错误！");
			}
			Map<String, Object> map = orderMonitorService
					.getUrgeCount(courierId);

			ajaxJson = SUCCESS(map);

		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", e.getMessage());
		}
		logger.info("return result：{}", JSON.toJSON(ajaxJson));
		return ajaxJson;
	}

	/**
	 * 
	 * 为未抢单订单指派快递员
	 * 
	 * @param courierId
	 * @return
	 */
	@RequestMapping("/assigCourier")
	@ResponseBody
	public AjaxJson assigCourier(@RequestParam Integer courierId,
			@RequestParam Integer orderId) {
		AjaxJson ajaxJson = null;
		logger.info("invoke method getUrgeCount ,params:{}{}", courierId,
				orderId);

		try {

			if (courierId == null && orderId == null) {
				ajaxJson = FAIL("01", "参数错误！");
			}
			// 判断是否更新成功
			if (orderMonitorService.assigCourier(courierId, orderId) > 0) {
				String[] targets = new String[] { courierId.toString() };

				Map<String, String> extras = new HashMap<String, String>();
				extras.put("appType", AppTypeConstants.APP_TYPE_COURIER);
				extras.put("orderId", orderId.toString());
				JPush.push(targets, "指派订单", "快递员管理端",
						SoundFile.SOUND_NEW_ORDER, extras);
			}

			ajaxJson = SUCCESS();

		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", e.getMessage());
		}
		logger.info("return result：{}", JSON.toJSON(ajaxJson));
		return ajaxJson;
	}

	/**(OvO)
	 * 将订单Map中的sendTime字段由String(11:30) 转成时间戳
	 * @param ordefMap	订单Map
	 */
	private void timeRemark2second(Map<String, Object> orderMap){
		Object timeRemark = orderMap.get("sendTime");
		String hour = null;
		String min = null;
		/**
		 * 如果timeRemark字段为空就将送货时间设为0:00
		 * 但是感觉timeRemark不会为空
		 */
		if(timeRemark == null){
			logger.warn("订单的timeRemark为空");
			hour = "0";
			min = "0";
		}else{
			String timeRemarkStr = timeRemark.toString().trim();
			int length = timeRemarkStr.length();
			/**
			 * 生性多疑(MDZZ)的我
			 * 如果timeRemark字段不是我想象中那样就将送货地址设为0:00
			 */
			if(length < 5){
				logger.warn("订单的timeRemark有毒");
				hour = "0";
				min = "0";
			}else{
				min = timeRemarkStr.substring(length-2);
				hour = timeRemarkStr.substring(length - 5, length - 3);
			}
		}
		/**
		 * 覆盖订单Map中原有的送货时间sendTime
		 */
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
		c.set(Calendar.MINUTE, Integer.parseInt(min));
		c.set(Calendar.SECOND, 0);
		orderMap.put("sendTime", c.getTimeInMillis()/1000);
	}
}
