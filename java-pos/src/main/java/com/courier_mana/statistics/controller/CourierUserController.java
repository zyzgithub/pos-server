package com.courier_mana.statistics.controller;


import java.util.List;
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
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.common.vo.SearchVo4UserRank;
import com.courier_mana.personal.service.CourierMyInfoService;
import com.courier_mana.personal.service.CourierRegionService;
import com.courier_mana.statistics.service.CourierUserService;

/**
 * 用户统计模块Controller
 * @author hyj
 *
 */
@Controller
@RequestMapping("/ci/courier/user")
public class CourierUserController extends BasicController{

	private final static Logger logger = LoggerFactory.getLogger(CourierUserController.class);
	
	@Autowired
	private CourierRegionService CourierRegionService;
	
	@Autowired
	private CourierUserService courierUserService;
	
	@Autowired
	private CourierMyInfoService courierMyInfoService;
	
	/**
	 * 获取划分用户类型规则
	 * @return	id		类型ID
	 * 			typeName类型名称
	 * 			typeDesc类型描述
	 * 			amount	类型的最低消费
	 */
	@RequestMapping("/getUserTypeRule")
	@ResponseBody
	public AjaxJson getUserTypeRule(){
		logger.info("invoke method getUserTypeRule, noParam");
		
		AjaxJson ajaxJson = new AjaxJson();
		try {
			ajaxJson = SUCCESS(this.courierUserService.getUserTypeRule());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxJson = FAIL("02","获取用户分类规则失败");
		}
		
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
	
	/**
	 * 获取各类型用户数量
	 * 数据来自tum_user_statistics
	 * @param 	orgId		机构ID(可选)(按地区筛选用户，可以突破快递员的管辖范围)
	 * 			courierId	快递员ID(按地区筛选用户，只显示快递员管辖的范围)
	 * @return	id			类型ID
	 * 			typeName	类型名称
	 * 			typeDesc	类型描述
	 * 			amount		类型的最低消费
	 * 			userCount	用户数量
	 */
	@RequestMapping("/getUserCount")
	@ResponseBody
	public AjaxJson getUserCount(SearchVo vo, Integer courierId){
		logger.info("invoke method getUserCount1, without param.");
		AjaxJson ajaxJson = new AjaxJson();
		if(courierId == null){
			return FAIL("01","参数错误");
		}
		Integer orgId = vo.getOrgId();
		
		/**
		 * 合作商flag(true: 合作商)
		 */
		boolean isAgent = this.courierMyInfoService.isAgentUser(courierId);
		
		/**
		 * 判断courierId是否合作商用户ID, 控制流程
		 */
		if(!isAgent){
			/**
			 * 如果传入的courierId不是合作商
			 * 检查快递员所属的区域是否为空
			 */
			Map<String, Object> courierInfo = this.CourierRegionService.getCourierOrgInfo(courierId);
			if(courierInfo == null){
				throw new IllegalArgumentException("无法获取用户的机构信息");
			}
			if(orgId == null || orgId == 0){
				orgId = (Integer)courierInfo.get("id");
				orgId = orgId==null?0:orgId;		//判断orgId是否为空,为空则orgId为0
			}
		}
		
		try {
			/**
			 * 获取用户划分规则
			 * 划分规则以高级用户优先的方式排序
			 */
			List<Map<String, Object>> typeRuleList = this.courierUserService.getUserTypeRule();
			
			int amountOfType = typeRuleList.size();
			
			/**
			 * 用于保存各类用户数量的数组
			 */
			int[] userCount = new int[amountOfType];
			
			/**
			 * 获取用户消费情况List
			 */
			List<Map<String, Object>> list = null;
			if(isAgent){
				list = this.courierUserService.getUserTotalSpent4Agent(vo, courierId);
			}else{
				list = this.courierUserService.getUserTotalSpent(vo, orgId);
			}
			
			/**
			 * 遍历用户消费情况List
			 * 并且统计各类型用户数量
			 */
			for(Map<String, Object>m: list){
				Integer totalSpent = Integer.valueOf(m.get("totalSpent").toString());
				
				/**
				 * 遍历用户类型列表中的amount(类型的最低消费要求)数值
				 * 此数值由大到小进行排序
				 * 利用amount对各类用户数量进行统计
				 */
				for(int i = 0; i < amountOfType; i++){
					Integer typeAmount = (Integer)typeRuleList.get(i).get("amount");
					/**
					 * totalSpent是来自表tum_user_statistics的数据,单位为分
					 * typeAmount是来自0085_custtype_rule表的数据,在生产库中单位为分
					 * 所以不需要进行单位转换
					 */
					if(totalSpent >= typeAmount){
						userCount[i] += 1;
						break;
					}
				}
			}
			for(int k = 0; k < amountOfType; k++){
				typeRuleList.get(k).put("userCount", userCount[k]);
			}
			ajaxJson = SUCCESS(typeRuleList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ajaxJson = FAIL("02","获取用户数量失败");
		}
		return ajaxJson;
	}
	
	/**
	 * 获取用户排名
	 * @param vo			搜索条件Vo
	 * @param page			页码
	 * @param rowsPerPage	每页显示的记录数
	 * @return	courierId	快递员ID(用于筛选用户，只显示快递员管辖的范围)
	 * 			userId		用户ID
	 * 			userName	用户名
	 * 			spent		搜索时间段内的花费
	 * 			orgName		用户所属区域的名字
	 * 			rank		用户在搜索时间段的排名
	 * 			preSpent	用户在前段时间的花费(用于判断涨幅)
	 * 			userType	用户类型
	 * 			userTypeId	用户类型ID
	 * 			userTypeDesc用户类型详细说明
	 * 			totalSpent	用户从注册至今的总花费(用于判断用户类别)
	 * 			totalCount	用户从注册至今的总订单数(用于debug)
	 */
	@RequestMapping("/getUserRank")
	@ResponseBody
	public AjaxJson getUserRank(SearchVo4UserRank vo, Integer page, Integer rowsPerPage, Integer courierId){
		logger.info("invoke method getUserRank, params:{},{},{}", vo, page, rowsPerPage);
		AjaxJson ajaxJson = new AjaxJson();
		if(courierId == null){
			return FAIL("01","参数错误");
		}
		/**
		 * 检查入参courierId是否合作商用户ID
		 * 控制流程
		 */
		boolean isAgent = this.courierMyInfoService.isAgentUser(courierId);
		
		if(!isAgent){
			Map<String, Object> courierInfo = this.CourierRegionService.getCourierOrgInfo(courierId);
			Integer orgId = vo.getOrgId();
			if(orgId == null || orgId == 0){
				if(courierInfo == null){
					return FAIL("01", "该用户没有对应的区域信息");
				}
				orgId = (Integer)courierInfo.get("id");
				orgId = orgId==null?0:orgId;		//判断orgId是否为空,为空则orgId为0
				vo.setOrgId(orgId);
			}
		}
		
		if(page == null || rowsPerPage == null){
			page = 1;
			rowsPerPage = 10;
		}
		
		/**
		 * 默认查询老用户
		 */
		if(vo.getIsNewUser() == null){
			vo.setIsNewUser(true);
		}
		
		try {
			ajaxJson = SUCCESS(this.courierUserService.getUserRank(courierId, isAgent, vo, page, rowsPerPage));
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02","获取用户排行失败");
		}
		logger.info("return result:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}
}