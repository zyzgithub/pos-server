package com.courier_mana.management.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.management.service.AssignBuildingToCourierService;

/**(OvO)
 * 管理-配送管理-大厦分配模块controller
 * @author hyj
 */
@Controller
@RequestMapping("/ci/courier/management/building")
public class AssignBuildingToCourierController extends BasicController {
	private final static Logger logger = LoggerFactory.getLogger(AssignBuildingToCourierController.class);
	
	@Autowired
	private AssignBuildingToCourierService assignBuildingToCourierService;
	
	/**(OvO)
	 * 管理-配送管理-大厦分配(主页面)
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
			return SUCCESS(this.assignBuildingToCourierService.getAssignmentData(courierId));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取快递员大厦分配信息失败: " + e.getMessage());
		}
	}
}
