package com.courier_mana.monitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.examples.service.CourierOrgServicI;
import com.courier_mana.monitor.service.CourierAttendanceServiceI;
import com.courier_mana.monitor.service.CourierMerchantServicI;
import com.courier_mana.monitor.service.CourierOrderMonitorServicI;


@Controller
@RequestMapping("ci/courier/order/monitor")
public class CourierOrderMonitorController extends BasicController{

	private final static Logger logger = LoggerFactory.getLogger(CourierOrderMonitorController.class);
	
	@Autowired
	private CourierOrderMonitorServicI courierOrderServicImpl;
	@Autowired
	private CourierAttendanceServiceI courierAttendanceServiceI;
	@Autowired
	private CourierMerchantServicI courierMerchantServicImpl;
	@Autowired
	private CourierOrgServicI courierOrgService;
	
	@RequestMapping("/get_manage_orders_count")
	@ResponseBody
	public AjaxJson getManageOrdersCount(@RequestParam Integer courierId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getManageOrdersCount, params:{}", courierId);
		try {
			//参数检查
			if(courierId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				List<Integer> orgIds = this.courierOrgService.getManageOrgIds(courierId);
				String orgIdsStr = null;
				if(!CollectionUtils.isEmpty(orgIds)){
					orgIdsStr = StringUtils.join(orgIds, ",");
				}
				Map<String, Object>  map = courierOrderServicImpl.getCourierOrdersCounts(courierId);
				Integer signCount = courierAttendanceServiceI.getCourierOndutySum(courierId);
				map.put("signCount", signCount);//签到数
				
				long totalMerchantCounts = courierMerchantServicImpl.getMerchantCount(orgIdsStr);//总商家
				long bindMerchantCounts = courierMerchantServicImpl.getBindMerchants(orgIdsStr);//已绑定商家
				map.put("totalMerchantCounts", totalMerchantCounts);
				map.put("totalUnBindMerchantCounts",totalMerchantCounts - bindMerchantCounts);//未绑定商家
				ajaxJson = SUCCESS(map);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "获取订单统计数据失败");
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	@RequestMapping("/get_couriers_location")
	@ResponseBody
	public AjaxJson getCouriersLocation(@RequestParam Integer courierId,Integer orgId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getCouriersLocation, courierId:{}, orgId:{}", courierId, orgId);
		try {
			//参数检查
			if(courierId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				List<Map<String, Object>> list;
				if(null != orgId){
					list = courierOrderServicImpl.getCouriersAndOrdersCountById(courierId,orgId);
				}else{
					list = courierOrderServicImpl.getCouriersAndOrdersCount(courierId);
				}
				if(CollectionUtils.isNotEmpty(list))
				for(int i=0;i<list.size();i++){
					if(Integer.valueOf(list.get(i).get("acceptQuantity").toString()) == 0){
						list.get(i).put("type", "0");
					}
					if(Integer.valueOf(list.get(i).get("acceptQuantity").toString()) <= 10 && Integer.valueOf(list.get(i).get("acceptQuantity").toString()) > 0){
						list.get(i).put("type", "1");
					}
					if(Integer.valueOf(list.get(i).get("acceptQuantity").toString()) > 10){
						list.get(i).put("type", "2");
					}
				}
				
				ajaxJson = SUCCESS(list);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "获取快递员列表数据失败: " + e.getMessage());
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	@RequestMapping("/get_courier_orders_count")
	@ResponseBody
	public AjaxJson getCourierOrdersCount(@RequestParam Integer courierId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getCourierOrdersCount, params:{}", courierId);
		try {
			//参数检查
			if(courierId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				ajaxJson = SUCCESS(courierOrderServicImpl.getCourierOrdersCountById(courierId));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "获取订单统计数据失败");
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	@RequestMapping("/get_order_detail")
	@ResponseBody
	public AjaxJson getOrderDetail(@RequestParam Integer orderId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getOrderDetail, params:{}", orderId);
		try {
			//参数检查
			if(orderId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				int all = 0;//总数
				double total = 0;//总价格
				List<Map<String, Object>> orders = courierOrderServicImpl.getOrderDetailByOrderId (orderId);
				for(int i=0;i<orders.size();i++){
					all = all+ Integer.valueOf((orders.get(i)).get("quantity").toString());
					total = total+ Double.valueOf((orders.get(i)).get("total_price").toString());
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("list",orders);
				map.put("addup", "");
				map.put("box", "");
				if(orders!=null && orders.size()>0){
					if(orders.get(0)!=null){
						map.put("addup", orders.get(0).get("addup"));
						map.put("box", orders.get(0).get("box"));
					}
				}
				map.put("all",all);
				map.put("total",total);
				ajaxJson = SUCCESS(map);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "获取订单明细数据失败");
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	
	@RequestMapping("/get_order_by_org")
	@ResponseBody
	public AjaxJson getOrderByOrg(@RequestParam Integer page, Integer rows, Integer courierId,Integer orgId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getOrderByKeywords, params:{}", orgId);
		try {
			//参数检查
			if(orgId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				ajaxJson = SUCCESS(courierOrderServicImpl.getOrdersByOrgId(page,rows,courierId, orgId));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "根据机构查询订单数据失败");
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	@RequestMapping("/get_order_by_keywords")
	@ResponseBody
	public AjaxJson getOrderByKeywords(@RequestParam Integer page, Integer rows, Integer courierId,String keyword){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getOrderByKeywords, params:{}", keyword);
		try {
			//参数检查
			if(keyword == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				ajaxJson = SUCCESS(courierOrderServicImpl.getOrdersByKeywords(page,rows,courierId, keyword));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "根据关键词查询订单数据失败");
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	@RequestMapping("/get_other_couriers")
	@ResponseBody
	public AjaxJson getOtherCouriers(@RequestParam Integer orderId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getOtherCouriers, params:{}", orderId);
		try {
			//参数检查
			if(orderId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				ajaxJson = SUCCESS(courierOrderServicImpl.getCouriersByOrderId(orderId));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "根据关键词查询订单数据失败");
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	@RequestMapping("/assign_order")
	@ResponseBody
	public AjaxJson assignOrder(@RequestParam Integer courierId,Integer orderId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getOtherCouriers, params:{}", courierId,orderId);
		try {
			//参数检查
			if(orderId == null || courierId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				ajaxJson = SUCCESS(courierOrderServicImpl.updateOrderCourierById(courierId, orderId));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "指派订单失败");
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	
}
