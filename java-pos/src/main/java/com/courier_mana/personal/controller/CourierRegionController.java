package com.courier_mana.personal.controller;

import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.personal.service.CourierRegionService;

/**
 * "我负责的区域"的controller
 * @author hyj
 *
 */
@Controller
@RequestMapping("/ci/courier/admin")
public class CourierRegionController extends BasicController {
	private final static Logger logger = LoggerFactory.getLogger(CourierRegionController.class);
	
	@Autowired
	private CourierRegionService courierRegionService;
	
//	@RequestMapping("/getRegion")
//	@ResponseBody
//	public AjaxJson getRegion(@RequestParam Integer courierId){
//		logger.info("invoke method getRegion, params:{}", courierId);
//		
//		AjaxJson ajaxJson = null;
//		if(courierId == null){
//			ajaxJson = FAIL("01", "参数错误");
//			return ajaxJson;
//		}
//		
//		//用于输出的map
//		Map<String, Object> result = new HashMap<String, Object>();
//		
//		//先查询快递员所在的组织
//		Map<String, Object> map = this.courierRegionService.getCourierOrgInfo(courierId);
//		
//		if(map == null){
//			ajaxJson = FAIL("01", "无此快递员记录");
//			return ajaxJson;
//		}
//		
//		try {
//			Integer orgLevel = (Integer)map.get("level");
//			String province = null;//省
//			String city = null;//市
//			
//			//根据组织的级别构造输出的对象
//			switch(orgLevel){
//			case 2: province = (String)map.get("orgName");break;
//			case 3: city = (String)map.get("orgName");break;
//			case 4: result.put("district", map.get("orgName"));break;
//			case 5: result.put("area", map.get("orgName"));break;
//			case 6: result.put("network", map.get("orgName"));break;
//			}
//			
//			String str = (String)map.get("orgPath");
//
//			if(str != null){			
//				String[] strs = str.split("_");
//				for(String s:strs){
//					Map<String, Object> org = this.courierRegionService.getOrgInfo(Integer.valueOf(s));
//					Integer level = (Integer)org.get("level");
//					switch(level){
//					case 2: province = (String)org.get("orgName");break;
//					case 3: city = (String)org.get("orgName");break;
//					case 4: result.put("district", org.get("orgName"));break;
//					case 5: result.put("area", org.get("orgName"));break;
//					case 6: result.put("network", org.get("orgName"));break;
//					}
//				}
//			}
//			result.put("city", province+city);
//		} catch (NumberFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			ajaxJson = FAIL("02", "获取快递员所负责区域失败");
//		}
//		ajaxJson = SUCCESS(result);
//		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
//		return ajaxJson;
//	}
	
	/**
	 * 根据快递员(管理员)ID
	 * 获取管辖范围
	 * @param courierId 快递员(管理员)ID
	 * @return 	city:		城市
	 * 			district:	区
	 * 			area:		片区
	 * 			network:	网点
	 */
	@RequestMapping("/getAdminRegion")
	@ResponseBody
	public AjaxJson getAdminRegionRegion(Integer courierId){
		logger.info("invoke method getAdminRegionRegion, params:{}", courierId);
		
		AjaxJson ajaxJson = null;
		if(courierId == null){
			ajaxJson = FAIL("01", "参数错误");
		}
		else{
			try {
				Map<String, Object> map = this.courierRegionService.getAdminRegion(courierId);
				if(map == null){
					map = new HashMap<String, Object>();
				}
				ajaxJson = SUCCESS(map);				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ajaxJson = FAIL("02", "获取快递员所负责区域失败");
			}
		}
		
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
}
