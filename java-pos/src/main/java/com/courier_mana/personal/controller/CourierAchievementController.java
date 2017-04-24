package com.courier_mana.personal.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.personal.service.CourierAchievementService;

import com.courier_mana.statistics.service.CourierUserService;

import com.courier_mana.personal.service.CourierMyInfoService;


/**
 * "我负责的区域"的controller
 * @author hyj
 *
 */
@Controller
@RequestMapping("/ci/courier/admin")
public class CourierAchievementController extends BasicController {
	private final static Logger logger = LoggerFactory.getLogger(CourierAchievementController.class);
	
	@Autowired
	private CourierAchievementService courierAchievementService;
	

	@Autowired
	private CourierUserService courierUserService;
	

	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	/**
	 * 获取快递员(管理员)个人信息
	 * 如果查找不到快递员的信息方法返回null
	 * @param courierId 快递员ID
	 * @return	id			快递员ID
	 * 			username	快递员名称
	 * 			photoUrl	头像Url
	 * 			positionName职位名称		
	 * 			orgName		所在机构名称
	 */
	@RequestMapping("/getCourierInfo")
	@ResponseBody
	public AjaxJson getCourierInfo(Integer courierId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getCourierInfo, params:{}", courierId);
		try{
			if(courierId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else{
				Map<String, Object> courierInfo = this.courierAchievementService.getCourierInfo(courierId);
				if(courierInfo == null){
					courierInfo = new HashMap<String, Object>();
				}
				ajaxJson = SUCCESS(courierInfo);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			ajaxJson = FAIL("02", "获取快递员信息失败");
		}
		return ajaxJson;
	}
	
	
	/**
	 * 获取人员 某个节点下的快递员排名
	 * @param courierId   快递员ID
	 * @param nodeType	     节点类型 : city城市,slice按片区,shop网店,urbanDistrict按市区 
	 * @param period	     时间区间:(month月,week周,day日)
	 * @return
	 */
	@RequestMapping("/getRegionRanking")
	@ResponseBody
	public AjaxJson getNodeRanking(Integer courierId,String nodeType,String period){
		
		AjaxJson ajaxJson = null;
		
		logger.info("invoke method getManageOrgs, params:{}{}", courierId, nodeType,period);
		try {
			if(courierId == null || nodeType == null || nodeType.equals("")){
				ajaxJson = FAIL("01", "参数错误");
			}else{
				boolean falg = false;
				Map<String, Object> map = this.courierUserService.getUserByPosition(new Long(courierId));
				if(map == null){
					ajaxJson = FAIL("02", "用户权限不足...");
					return ajaxJson;
				}
				String position = map.get("position").toString();
				switch (nodeType) {
				case "city":
					
					break;
				case "slice":
					break;
				case "shop":
					break;
				case "urbanDistrict":
					break;
				default:
					break;
				}
				if(nodeType.equals("city")){
					
				}
				
				List<Map<String, Object>> list = this.courierAchievementService.queryNodeRanking(new Long(courierId), nodeType);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "获取快递员排名失败");
		}
		return ajaxJson;
	}
	
	
	/**
	 * 获取快递员排名
	 * @param courierId		快递员ID(用于确定搜索区域)
	 * @param period		排名时间(month:按月排名，否则为按天排名)
	 * @param isRankByArea	按区域排名(如果为false，参数：快递员ID的值不会影响获得的排名)
	 * @param page			页数(可选)
	 * @param rowsPerPage	每页记录数(可选)
	 * @return	rankList	排名列表
	 * 				courier_id	快递员ID
	 * 				username	快递员姓名
	 * 				photoUrl	快递员头像Url
	 * 				total		订单数
	 * 				orgName		快递员所在机构
	 * 			myRank		我的排行
	 * 				total		订单数
	 * 				rank		排名
	 */
	@RequestMapping("/getCouriersRank")
	@ResponseBody
	public AjaxJson getMyRank(Integer courierId,
			String period, Boolean isRankByArea, Integer page, Integer rowsPerPage){
		AjaxJson ajaxJson = null;
		String startDate = null;
		String endDate = null;
		logger.info("invoke method getManageOrgs, params:{}{}{}{}{}{}", courierId, period, isRankByArea, page, rowsPerPage);
		try {
			//参数检查
			if(isRankByArea == null){
				isRankByArea = true;
			}
			if("month".equals(period)){
				/**
				 * 如果按月统计则查询当月1号到当日的记录
				 */
				Calendar c = Calendar.getInstance();
				startDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1) + "-1";
				c.add(Calendar.DAY_OF_MONTH, 1);
				endDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.DAY_OF_MONTH);
			}else if("lastMonth".equals(period)){
				/**
				 * 按上月统计则查询上月1号到上月本日的记录
				 */
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MONTH, -1);
				startDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1) + "-1";
				c.add(Calendar.DAY_OF_MONTH, 1);
				endDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.DAY_OF_MONTH);
			}
			else{
				/**
				 * 按天统计
				 */
				Calendar c = Calendar.getInstance();
				startDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.DAY_OF_MONTH);
				c.add(Calendar.DAY_OF_MONTH, 1);
				endDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.DAY_OF_MONTH);
			}
			if(page == null){
				page = 1;
			}
			if(rowsPerPage == null){
				rowsPerPage = 10;
			}
			if(courierId == null){
				ajaxJson = FAIL("01", "参数错误");
			}
			else {
				Map<String, Object> result = new HashMap<String, Object>();
				if(this.courierMyInfoService.isAgentUser(courierId)){
					result.put("rankList", this.courierAchievementService.getCouriersRank4Agent(courierId, startDate, endDate, page, rowsPerPage));
					result.put("myRank", null);
				}else{
					result.put("rankList", this.courierAchievementService.getCouriersRank(courierId, startDate, endDate, isRankByArea, page, rowsPerPage));
					result.put("myRank", this.courierAchievementService.getMyRank(courierId, startDate, endDate, isRankByArea));
				}
				ajaxJson = SUCCESS(result);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02", "获取排行榜失败");
		}
		return ajaxJson;
	}
}
