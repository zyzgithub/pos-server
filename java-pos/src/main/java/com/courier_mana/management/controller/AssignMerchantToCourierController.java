package com.courier_mana.management.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.management.service.AssignMerchantToCourierService;

/**(OvO)
 * 管理-配送管理-店铺分配模块controller
 * @author hyj
 */
@Controller
@RequestMapping("/ci/courier/management/merchant")
public class AssignMerchantToCourierController extends BasicController{
	private final static Logger logger = LoggerFactory.getLogger(AssignMerchantToCourierController.class);
	
	@Autowired
	private AssignMerchantToCourierService assignMerchantToCourierService;
	
	/**(OvO)
	 * 管理-配送管理-店铺分配(主页面)
	 * @param courierId	courierId：快递员ID(必选)
	 * @return
	 */
	@RequestMapping("/getAssignmentData")
	@ResponseBody
	public AjaxJson getMerchantAssignmentData(Integer courierId){
		logger.info("Invoke method: getMerchantAssignmentData， params: courierId - {}",	courierId);
		
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		
		try{
			return SUCCESS(this.assignMerchantToCourierService.getAssignmentData(courierId));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取快递员店铺分配信息失败: " + e.getMessage());
		}
	}
	
	/**(OvO)
	 * 管理-配送管理-店铺分配(删除绑定关系)
	 * @param courierId	courierId：快递员ID(必选)
	 * @return
	 */
	@RequestMapping("/deleteAssignmentRecord")
	@ResponseBody
	public AjaxJson deleteAssignmentRecord(Integer courierId, Integer merchantId){
		logger.info("Invoke method: deleteAssignmentRecord， params: courierId - {}, merchantId - {}", courierId, merchantId);
		
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		if(merchantId == null){
			return FAIL("01","参数: 店铺ID,不能为空");
		}
		
		try{
			return SUCCESS(this.assignMerchantToCourierService.deleteOneMerchantAssignmentRecord(merchantId, courierId));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "解除快递员店铺绑定失败: " + e.getMessage());
		}
	}
	
	/**(OvO)
	 * 管理-配送管理-店铺分配-分配门店(页面)
	 * @param courierId	courierId：快递员ID(必选)
	 * @return
	 */
	@RequestMapping("/getMerchantList")
	@ResponseBody
	public AjaxJson getMerchantList(Integer courierId){
		logger.info("Invoke method: getMerchantList， params: courierId - {}", courierId);
		
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		
		try{
			return SUCCESS(this.assignMerchantToCourierService.getMerchantList(courierId));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取店铺列表失败: " + e.getMessage());
		}
	}
	
	/**(OvO)
	 * 管理-配送管理-店铺分配-写入
	 * @param courierId	courierId：快递员ID(必选)
	 * @return
	 */
	@RequestMapping("/updateAssignmentRecord")
	@ResponseBody
	public AjaxJson updateAssignmentRecord(Integer courierId, Integer[] merchantId, Integer loginedCourierId){
		logger.info("Invoke method: updateAssignmentRecord， params: courierId - {}, merchantIds- {}, loginedCourierId - {}", courierId, merchantId, loginedCourierId);
		
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		if(merchantId == null){
			return FAIL("01","参数: 店铺ID,不能为空");
		}
		
		try{
			return SUCCESS(this.assignMerchantToCourierService.updateAssignmentRecord(courierId, merchantId, loginedCourierId));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取店铺列表失败: " + e.getMessage());
		}
	}
}
