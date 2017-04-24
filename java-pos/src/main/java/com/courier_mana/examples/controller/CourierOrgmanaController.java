package com.courier_mana.examples.controller;

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
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.examples.service.CourierOrgServicI;


@Controller
@RequestMapping("/ci/courier/Orgmana")
public class CourierOrgmanaController extends BasicController{

	private final static Logger logger = LoggerFactory.getLogger(CourierOrgmanaController.class);
	
	@Autowired
	private CourierOrgServicI courierOrgServicI;
	
	/**
	 * 根据快递员ID获取其管辖的机构列表
	 * @param courierId
	 * @return
	 */
	@RequestMapping("/get_manage_orgs")
	@ResponseBody
	public AjaxJson getManageOrgs(@RequestParam Integer courierId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getManageOrgs, params:{}", courierId);
		try {
			//参数检查
			if(courierId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				ajaxJson = SUCCESS(courierOrgServicI.getManageOrgs(courierId));
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("01", "获取快递员所管辖的机构失败");
		}
		logger.info("return result{}:",JSON.toJSONString(ajaxJson)) ;
		return ajaxJson;
	}
	
	/**
	 * 获取显示搜索区域数据
	 * @param courierId
	 * @return
	 */
	@RequestMapping("/getSearchOrgs")
	@ResponseBody
	public AjaxJson getSearchOrgs(Integer courierId, Integer orgId){
		logger.info("invoke method getSearchOrgs, params:{}", courierId, orgId);
		try {
			Map<String, Object> courierOrg = new HashMap<String, Object>();
			if(orgId==null){
				courierOrg = courierOrgServicI.getParentOrg(courierId);
				orgId = (Integer) courierOrg.get("id");
			}else{
				courierOrg = courierOrgServicI.getManageCurrentOrgs(orgId);
			}
			List<Map<String, Object>> nextOrgs = courierOrgServicI.getManageNextOrgs(orgId);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("courierOrg", courierOrg);  //当前选中区域
			result.put("nextOrgs", nextOrgs);		//下一级区域列表
			return SUCCESS(result);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return FAIL("01", "获取显示搜索区域数据失败");
		}
	}
	
	/**(OvO)
	 * 获取各网点基本信息
	 * @param courierId
	 * @return	返回各网点的基本信息, 包括: 
	 * 		id 				机构ID 
	 * 		orgName 		机构名称 
	 * 		latitude 		网点纬度 
	 * 		longitude 		网点经度 
	 * 		merchantCount 	网点下商铺数量 
	 * 		courierCount 	网点下快递员数量
	 */
	@RequestMapping("/getOrgsInfo")
	@ResponseBody
	public AjaxJson getOrgsInfo(){
		logger.info("Invoke method: getOrgsInfo, without param.");
		try{
			return SUCCESS(this.courierOrgServicI.getOrgsInfo());
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("01","获取网点信息失败" + e.getMessage());
		}
	}
}
