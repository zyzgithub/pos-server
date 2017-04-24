package com.courier_mana.management.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.management.service.BuildingAndFloorMonitorService;

/**(OvO)
 * 管理-配送管理-监控模块controller
 * @author huj
 *
 */
@Controller
@RequestMapping("/ci/courier/management/monitor")
public class BuildingAndFloorMonitorController extends BasicController{
	private final static Logger logger = LoggerFactory.getLogger(BuildingAndFloorMonitorController.class);
	
	@Autowired
	private BuildingAndFloorMonitorService buildingAndFloorMonitorService;
	
	/**
	 * 管理-监控-店铺监控-主页面
	 * @param courierId 快递员ID
	 * @return
	 */
	@RequestMapping("/merchantAssignmentInfo")
	@ResponseBody
	public AjaxJson merchantAssignmentInfo(Integer courierId){
		logger.info("Invoke method: merchantAssignmentInfo， params: courierId - {}",	courierId);
		
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		
		try{
			return SUCCESS(this.buildingAndFloorMonitorService.getMerchantAssignmentInfo(courierId));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取店铺-快递员分配信息失败: " + e.getMessage());
		}
	}
	
	/**
	 * 管理-监控-店铺监控-快递员浮层
	 * @param merchantId	店铺ID
	 * @return	返回快递员浮层需要的数据
	 */
	@RequestMapping("/getCourierList4Merchant")
	@ResponseBody
	public AjaxJson getCourierList4Merchant(Integer merchantId){
		logger.info("Invoke method: getCourierList4Merchant, params: merchantId - {}", merchantId);
		/**
		 * 检查必要的参数
		 */
		if(merchantId == null){
			return FAIL("01","参数: 店铺ID,不能为空");
		}
		
		try{
			return SUCCESS(this.buildingAndFloorMonitorService.getCourierList4Merchant(merchantId));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取店铺监控-快递员浮层信息失败: " + e.getMessage());
		}
	}
	
	/**
	 * 管理-监控-店铺监控-快递员浮层
	 * @param merchantId	店铺ID
	 * @return	返回快递员浮层需要的数据
	 */
	@RequestMapping("/updateMerchantAssignment")
	@ResponseBody
	public AjaxJson updateMerchantAssignment(Integer merchantId, Integer[] courierId){
		logger.info("Invoke method: updateMerchantAssignment, params: merchantId - {}, courierIds - {}", merchantId, courierId);
		/**
		 * 先检查参数必要的参数
		 */
		if(merchantId == null){
			return FAIL("01","参数: 店铺ID,不能为空");
		}
		if(courierId == null){
			return FAIL("01","参数: 快递员ID列表,不能为空");
		}
		
		try{
			return SUCCESS(this.buildingAndFloorMonitorService.updateMerchantAssignment(merchantId, courierId));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取店铺监控-快递员浮层信息失败: " + e.getMessage());
		}
	}
	
	/**
	 * 管理-监控-大厦监控-主页面
	 * @param courierId 快递员ID
	 * @return
	 */
	@RequestMapping("/buildingAssignmentInfo")
	@ResponseBody
	public AjaxJson buildingAssignmentInfo(Integer courierId){
		logger.info("Invoke method: buildingAssignmentInfo， params: courierId - {}",	courierId);
		
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		
		try{
			return SUCCESS(this.buildingAndFloorMonitorService.getBuildingAssignmentInfo(courierId));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取大厦分配信息失败: " + e.getMessage());
		}
	}
}
