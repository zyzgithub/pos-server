package com.wm.controller.courier;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jeecg.system.service.SystemService;
import net.spy.memcached.internal.OperationFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.StringUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Maps;
import com.wm.controller.takeout.vo.CourierOrgVo;
import com.wm.controller.takeout.vo.CourierPositionVo;
import com.wm.entity.courier.CourierLocationEntity;
import com.wm.entity.org.OrgEntity;
import com.wm.service.courier.CourierServiceI;
import com.wm.service.org.OrgServiceI;
import com.wm.util.AliOcs;

/**
 * 快递员控制器类
 *
 */
@Controller
@RequestMapping("ci/courierController") 
public class CourierController extends BaseController {
	private static final Logger LOG = Logger.getLogger(CourierController.class);
	
	@Autowired
	private CourierServiceI courierService;

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OrgServiceI orgService;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 根据快递员ID查询此快递员的派送范围
	 * @param courierId 快递员id
	 * @param page 开始页, 第一页为1，不能为0, 默认为1
	 * @param rows 每页行数，默认为10
	 * @return
	 */
	@RequestMapping(params = "queryDeliveryScopeList")
	@ResponseBody
	public AjaxJson queryDeliveryScopeList(
			@RequestParam(value="courierId") int courierId,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows) {
		AjaxJson j = new AjaxJson();
		if (courierId != 0) {
			if (page == 0) {
				page = 1;
			}
			List<Map<String, Object>> scopeList = courierService
					.queryDeliveryScopeList(courierId, page, rows);
			if (scopeList != null && scopeList.size() > 0) {
				j.setObj(scopeList);
				j.setSuccess(true);
			} else {
				j.setMsg("暂无快递员的派送范围");
				j.setSuccess(false);
			}
		} else {
			j.setMsg("查询快递员的派送范围失败，快递员ID不允许为空！");
			j.setSuccess(false);
		}
		return j;
	}

	/**
	 * 根据快递员ID查询此快递员的绑定商户
	 * @param courierId
	 * @param page 开始页, 第一页为1，不能为0, 默认为1
	 * @param rows 每页行数，默认为10
	 * @return
	 */
	@RequestMapping(params = "queryBindMerchantList")
	@ResponseBody
	public AjaxJson queryBindMerchantList(@RequestParam int courierId,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows) {
		AjaxJson j = new AjaxJson();
		if (courierId != 0) {
			if (page == 0) {
				page = 1;
			}
			List<Map<String, Object>> scopeList = courierService
					.queryBindMerchantList(courierId, page, rows);
			if (scopeList != null && scopeList.size() > 0) {
				j.setObj(scopeList);
				j.setSuccess(true);
			} else {
				j.setMsg("暂无快递员的绑定商户");
				j.setSuccess(false);
			}
		} else {
			j.setMsg("查询快递员的派送绑定商户，快递员ID不允许为空！");
			j.setSuccess(false);
		}
		return j;
	}
	
	/**
	 * 保存用户最新的位置
	 * @param userId 用户ID
	 * @param longitude 经度
	 * @param latitude 纬度
	 */
	@RequestMapping(params = "saveUserRenewalLocation" ,method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson saveUserRenewalLocation(@RequestParam int userId,
			@RequestParam Double longitude, @RequestParam Double latitude) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		if(userId == 0){
			j.setMsg("用户ID不允许为空！");
			return j;
		}
		if(longitude == null || latitude == null){
			j.setMsg("经纬度不允许为空！");
			return j;
		}
		courierService.saveUserLocation(userId, longitude, latitude);
		j.setSuccess(true);
		j.setMsg("保存用户最新的位置成功");
		
		SessionUtils.broadcast(JSON.toJSONString(new CourierLocation(longitude, latitude, userId)));
		return j;
	}
	
	/**
	 * 保存用户最新的位置
	 * @param userId 用户ID
	 * @param longitude 经度
	 * @param latitude 纬度
	 */
	@RequestMapping(params = "saveUserLocationIntoRedis")
	@ResponseBody
	public AjaxJson saveUserLocationInRedis(@RequestParam Double mLongitude, @RequestParam Double mLatitude,
			@RequestParam int userId, @RequestParam Double uLongitude, @RequestParam Double uLatitude) {
		AjaxJson j = new AjaxJson();
		try {
			Map<String, Object> result = courierService.saveUserLocationInRedis(mLongitude, mLatitude, userId, uLongitude, uLatitude);
			j.setSuccess(true);
			j.setMsg("保存用户最新的位置成功");
			j.setObj(result);
			
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("保存用户最新的位置失败");
		}
		return j;
	}
	
	/**
	 * 清除用户的位置
	 * @param userId 用户ID
	 */
	@RequestMapping(params = "cleanUserLocationInRedis")
	@ResponseBody
	public AjaxJson cleanUserLocationInRedis(@RequestParam int userId) {
		AjaxJson j = new AjaxJson();
		try {
			 courierService.cleanUserLocation(userId);
			j.setSuccess(true);
			j.setMsg("清除用户位置成功");
			
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("清除用户位置失败");
		}
		return j;
	}
	 
	@RequestMapping(params = "getUserRenewalLocation" ,method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson getUserRenewalLocation(@RequestParam int userId) {
		AjaxJson j = new AjaxJson();
		try {
			j.setSuccess(false);
			if(userId == 0){
				j.setMsg("用户ID不允许为空！");
				return j;
			}
			CourierLocationEntity courierLocationEntity = courierService.getUserLocation(userId);
			if(courierLocationEntity != null){
				j.setStateCode("00");
				j.setSuccess(true);
				j.setMsg("获取用户最新的位置成功");
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("userId", courierLocationEntity.getUserId());
				result.put("longitude", courierLocationEntity.getLongitude());
				result.put("latitude", courierLocationEntity.getLatitude());
				j.setObj(result);
			}
			else{
				j.setStateCode("01");
				j.setSuccess(false);
				j.setMsg("获取用户最新的位置失败");
			}
		} catch (Exception e) {
			j.setStateCode("02");
			j.setSuccess(false);
			j.setMsg("内部错误");
		}
		return j;
	}

	/**
	 * 根据用户ID和组织片区名称查询所有属于该片区快递员信息
	 * @param userId
	 * @param orgName
	 * @param page 开始页, 第一页为1，不能为0, 默认为1
	 * @param rows 每页行数，默认为10
	 * @return
	 */
	@RequestMapping(params = "queryCourierOrgs")
	@ResponseBody
	public AjaxJson queryCourierOrgs(int userId, String orgName, 
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows) {
		AjaxJson j = new AjaxJson();
		j.setMsg("暂无信息");
		j.setSuccess(false);
		j.setStateCode("01");
		if (userId != 0) {
			List<CourierOrgVo> couriers = courierService.queryOrgByUserId(userId);
			if (couriers != null && couriers.size() > 0) {
				CourierOrgVo courier = couriers.get(0);
				if (courier != null) {
					List<OrgEntity> orgList = systemService.findByProperty(
							OrgEntity.class, "pid", courier.getOrgPid());
					if (orgList != null && orgList.size() > 0) {
						if (StringUtil.isNotEmpty(orgName)) {
							try {
								orgName = new String(
										orgName.getBytes("ISO-8859-1"), "UTF-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}

							for (int i = 0; i < orgList.size(); i++) {
								OrgEntity org = orgList.get(i);
								if (org.getOrgName().equals(orgName)) {
									List<CourierOrgVo> courierOrgs = courierService
											.queryOrgByOrgName(orgName, page,
													rows);
									if (courierOrgs != null
											&& courierOrgs.size() > 0) {
										j.setObj(courierOrgs);
										j.setMsg("成功");
										j.setSuccess(true);
										j.setStateCode("00");
										return j;
									}
								}
							}
						} else {
							OrgEntity org = orgList.get(0);
							List<CourierOrgVo> courierOrgs = courierService
									.queryOrgByOrgName(org.getOrgName(), page,
											rows);
							if (courierOrgs != null && courierOrgs.size() > 0) {
								j.setObj(courierOrgs);
								j.setMsg("成功");
								j.setSuccess(true);
								j.setStateCode("00");
								return j;
							}

						}
					}
				}
			}

		}
		return j;
	}

	/**
	 * 根据快递员组长用户ID查询该组的快递员列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param page 开始页, 第一页为1，不能为0, 默认为1
	 * @param rows 每页行数，默认为10
	 * @return
	 */
	@RequestMapping(params = "queryGroupByUserId")
	@ResponseBody
	public AjaxJson queryGroupByUserId(int userId,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows) {
		AjaxJson j = new AjaxJson();
		j.setMsg("暂无信息");
		j.setSuccess(false);
		j.setStateCode("01");
		if (userId != 0) {
			List<CourierPositionVo> courierPositions = courierService
					.queryPositionByUserId(userId, page, rows);
			if (courierPositions != null && courierPositions.size() > 0) {
				CourierPositionVo courierPosition = courierPositions.get(0);
				if (courierPosition != null) {
					List<CourierOrgVo> courierOrgs = courierService
							.queryCouriersByGroupId(
									courierPosition.getGroupId(), page, rows);
					if (courierOrgs != null && courierOrgs.size() > 0) {
						j.setObj(courierOrgs);
						j.setMsg("成功");
						j.setSuccess(true);
						j.setStateCode("00");
						return j;
					}
				}
			}
		}
		return j;
	}


	/**
	 * 查询快递员的催单列表通过orgId
	 * 
	 * @param orgId 区域id
	 * @param userId 登录管理管的用户id
	 * @param page 开始页, 第一页为1，不能为0, 默认为1
	 * @param rows 每页行数，默认为10
	 * @see searchCourierOrder
	 * @return
	 */
	@Deprecated
	@RequestMapping(params = "queryCourierReminderByOrgId")
	@ResponseBody
	public AjaxJson queryCourierReminderByOrgId(Integer orgId, Integer userId, 
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows) {
		AjaxJson j = new AjaxJson();
		j.setMsg("暂无信息");
		j.setSuccess(false);
		j.setStateCode("01");
		if(orgId == null || orgId == 0){
			j.setMsg("区域ID不允许为空！");
			return j;
		}
		if(userId == null || userId == 0){
			j.setMsg("用户ID不允许为空！");
			return j;
		}
		if (orgId != null && orgId != 0 && userId != null && userId != 0 ) {
			List<Map<String, Object>> list = courierService.queryCourierReminderByOrgId(orgId, userId, page, rows);
			if (list != null && list.size() > 0) {
				j.setObj(list);
				j.setMsg("成功");
				j.setSuccess(true);
				j.setStateCode("00");
				return j;
			}
		}
		return j;
	}

	/**
	 * 获取管理员下的快递员列表by管理员id
	 * 通过快递员ID查他所在的小组的成员，如果是片区经理以上的，则查组织结构里所有的下属成员
	 * @param courierId 快递员id(也是管理员id)
	 * @param orderId 订单ID，表示这个订单下有哪些可以指派
	 * @param page 开始页, 第一页为1，不能为0, 默认为1
	 * @param rows 每页行数，默认为10
	 * @return
	 */
	@RequestMapping(params = "querySubCourierByCourierId")
	@ResponseBody
	public AjaxJson querySubCourierByCourierId(Integer courierId,
			Integer orderId,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows) {
		AjaxJson j = new AjaxJson();
		j.setMsg("暂无信息");
		j.setSuccess(false);
		j.setStateCode("01");
		if (courierId == null || courierId == 0 ) {
			j.setMsg("快递员id不能为空");
			return j;
		}
		if (orderId == null || orderId == 0 ) {
			j.setMsg("订单id不能为空");
			return j;
		}
		List<Map<String, Object>> list = courierService.queryCourierSubCourierByCourierId(courierId, orderId, page, rows);
		if (list != null && list.size() > 0) {
			j.setObj(list);
		}
		j.setMsg("成功");
		j.setSuccess(true);
		j.setStateCode("00");
		return j;
	}

	/**
	 * 指派订单给快递员
	 * @param orderId， 订单id
	 * @param designateId, 指派者 id
	 * @param designeeId， 被指派的快递员id
	 * @return 
	 */
	@RequestMapping(params = "designateOrder" ,method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson designateOrder(@RequestParam Integer orderId,
			@RequestParam Integer designateId,
			@RequestParam Integer designeeId) {
		AjaxJson j = new AjaxJson();
		
		j.setSuccess(false);
		if(orderId == null || orderId == 0){
			j.setMsg("订单ID不允许为空！");
			return j;
		}
		if(designateId == null || designateId == 0){
			j.setMsg("指派ID不允许为空！");
			return j;
		}
		if(designeeId == null || designeeId == 0){
			j.setMsg("快递员ID不允许为空！");
			return j;
		}
		Map<String, Object> map = courierService.designateOrder(designateId, designeeId, orderId);
		System.out.println(map);
		boolean flag = Boolean.parseBoolean(map.get("success").toString());
		if(flag){
			j.setMsg("指派订单成功");
		}else{
			j.setMsg("指派订单失败");
		}
		j.setSuccess(flag);
		return j;
	}
	

	/**
	 * 查询某个区域未抢的订单列表
	 * @param orgId 区域id
	 * @param courierId 快递员id(也是管理员id)
	 * @param page 开始页, 第一页为1， 默认为1
	 * @param rows 每页行数，默认为10
	 * @see searchCourierOrder
	 * @return
	 */
	@Deprecated
	@RequestMapping(params = "queryNotScrambleOrderByOrgId")
	@ResponseBody
	public AjaxJson queryNotScrambleOrderByOrgId(@RequestParam Integer orgId,
			@RequestParam Integer courierId,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows){
		AjaxJson j = new AjaxJson();
		
		j.setSuccess(false);
		if(orgId == null || orgId == 0){
			j.setMsg("区域ID不允许为空！");
			return j;
		}
		if(courierId == null || courierId == 0){
			j.setMsg("快递员ID不允许为空！");
			return j;
		}
		List<Map<String, Object>> map = courierService.queryNotScrambleOrderByOrgId(orgId, courierId, page, rows);
		if(map != null && map.size() > 0){
			j.setMsg("查询成功");
			j.setObj(map);
		} else{
			j.setMsg("无查询结果");
		}
		j.setSuccess(true);
		return j;
	}
	

	/**
	 * 查快递员拥有的区域
	 * @param orgId 区域id
	 * @param courierId 快递员id
	 * @param page 开始页, 第一页为1， 默认为1
	 * @param rows 每页行数，默认为10
	 * @return
	 */
	@RequestMapping(params = "queryFirstChildrenOrgs")
	@ResponseBody
	public AjaxJson queryFirstChildrenOrgs(@RequestParam Integer orgId,
			@RequestParam Integer courierId,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows){

		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		List<Map<String, Object>> map = courierService.queryFirstChildrenOrgs(orgId, courierId, page, rows);
		if(map != null && map.size() > 0){
			j.setMsg("查询成功");
			j.setObj(map);
		} else{
			j.setMsg("无查询结果");
		}
		j.setSuccess(true);
		return j;
	}
	

	/**
	 * 
	 * @param orgId
	 * @param keyword 关键字查询，可以搜索的字段为：排号， 电话，订单号，姓名
	 * @param state 状态为search，则查所有，否则填 scramble 抢单 , reminder 催单, 
	 * 			30minute 距离送餐时间还有30分钟的订单, 20minute 距离送餐时间还有20分钟的订单, 10minute 距离送餐时间还有10分钟的订单
	 * @param courierId 快递员id
	 * @param page 开始页, 第一页为1， 默认为1
	 * @param rows 每页行数，默认为10
	 * @return
	 */
	@RequestMapping(params = "searchCourierOrder")
	@ResponseBody
	public AjaxJson searchCourierOrder(@RequestParam Integer orgId,
			@RequestParam Integer courierId,
			@RequestParam(required = false) String keyword, 
			@RequestParam(required = true, defaultValue= "scramble") String state,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows){
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		List<Map<String, Object>> map = courierService.searchCourierOrder(orgId, courierId, keyword, state, page, rows);
		if(map != null && map.size() > 0){
			j.setMsg("查询成功");
			j.setObj(map);
		} else{
			j.setMsg("无查询结果");
		}
		j.setSuccess(true);
		return j;
	}
	


	/**
	 * 统计快递员管理版中的订单监控数，包括未抢单的数，催单数，30分钟，20分钟，10分钟的数
	 * 
	 * @param orgId
	 *            区域id
	 * @param courierId
	 *            快递员id
	 * @return
	 */
	@RequestMapping(params = "countOrderMonitor")
	@ResponseBody
	public AjaxJson countOrderMonitor(@RequestParam Integer orgId, @RequestParam Integer courierId){
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);
		
//		Map<String, Object> map = courierService.countOrderMonitor(orgId, courierId);
		
		// scramble 抢单 , reminder 催单, XXminute
		Map<String, Object> countMap = Maps.newHashMap();
		countMap.put("countScramble", 0);
		countMap.put("count30minute", 0);
		countMap.put("count20minute", 0);
		countMap.put("count10minute", 0);
		countMap.put("countReminder", 0);
		
		List<String> recursionQueryOrgIdList = orgService.recursionQueryOrgIdList(orgId, false);
		if(recursionQueryOrgIdList != null && recursionQueryOrgIdList.size() > 0){
			
			//	查未抢单的数
			Integer countScramble = courierService.getUnscrabledOrderNumber(courierId, orgId, null);
			countMap.put("countScramble", countScramble.toString());
			
			//	查XX分钟数
			Integer count30minute = courierService.getUnscrabledOrderNumber(courierId, orgId, 30);
			countMap.put("count30minute", count30minute.toString());
			
			Integer count20minute = courierService.getUnscrabledOrderNumber(courierId, orgId, 30);
			countMap.put("count20minute", count20minute.toString());

			Integer count10minute = courierService.getUnscrabledOrderNumber(courierId, orgId, 10);
			countMap.put("count10minute", count10minute.toString());

			//	查催单数
			Long countReminder = courierService.countCourierReminder(orgId, courierId);
			countMap.put("countReminder", countReminder);
		}
				
		if(countMap != null && countMap.size() > 0){
			j.setMsg("查询成功");
			j.setObj(countMap);
		} else{
			j.setMsg("无查询结果");
		}
		j.setSuccess(true);
		return j;
	}
	

	/**
	 * 查询快递员所在网点的所有商户
	 * @param courierId 快递员id
	 * @param page 开始页, 第一页为1， 默认为1
	 * @param rows 每页行数，默认为10
	 * @return
	 */
	@RequestMapping(params = "queryOrgMerchantList", method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson queryOrgMerchantList( 
			Integer courierId,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);

		if(courierId == null || courierId <= 0){
			j.setMsg("快递员id不能为空或小于0");
			return j;
		}
		List<CourierPositionVo> plist = courierService.queryPositionByUserId(courierId, 1, 10);
		if(plist == null || plist.isEmpty()){
			j.setMsg("快递员目前没有职位");
			return j;
		}
		boolean isManager = false;
		for (CourierPositionVo cpv : plist) {
			if("物流组长".equals(cpv.getPositionName())){
				isManager = true;
				break;
			}
		}
		if(!isManager){
			j.setMsg("快递员不是物流组长");
			return j;
		}
		List<Map<String,Object>> list = courierService.queryOrgMerchantList(courierId, page, rows);
		if(list != null && list.size() > 0){
			j.setMsg("查询列表成功");
			j.setObj(list);
		}else{
			j.setMsg("无查询结果");
		}
		j.setSuccess(true);
		return j;
	}
	
	
	/**
	 * 配送范围管理-查询物流组长的快递员成员
	 * @param courierId 快递员id
	 * @param page 开始页, 第一页为1， 默认为1
	 * @param rows 每页行数，默认为10
	 * @return
	 */
	@RequestMapping(params = "queryLogisticsLeaderMemberList", method=RequestMethod.POST)
	@ResponseBody
	public AjaxJson queryLogisticsLeaderMemberList( 
			Integer courierId,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(false);

		if(courierId == null || courierId <= 0){
			j.setMsg("快递员id不能为空或小于0");
			return j;
		}
		List<CourierPositionVo> plist = courierService.queryPositionByUserId(courierId, 1, 10);
		if(plist == null || plist.isEmpty()){
			j.setMsg("快递员目前没有职位");
			return j;
		}
		boolean isManager = false;
		for (CourierPositionVo cpv : plist) {
			if("物流组长".equals(cpv.getPositionName())){
				isManager = true;
				break;
			}
		}
		if(!isManager){
			j.setMsg("快递员不是物流组长");
			return j;
		}
		List<Map<String,Object>> list = courierService.queryLogisticsLeaderMemberList(courierId, page, rows);
		if(list != null && list.size() > 0){
			j.setMsg("查询列表成功");
			j.setObj(list);
		}else{
			j.setMsg("无查询结果");
		}
		j.setSuccess(true);
		return j;
	}

	/**
	 * 批量保存快递员与商户的关系
	 * @param courierId
	 *            快递员id
	 * @param merchantIds 商家id列表， 使用逗号分隔开，如(1,2,3,4)
	 */
	@RequestMapping(params = "batchSaveCourierMerchant", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson batchSaveCourierMerchant(Integer courierId, String merchantIds){
		AjaxJson j = new AjaxJson();
		String msg = "批量保存失败";
		j.setSuccess(false);
		if(courierId == null || courierId == 0 ){
			j.setMsg("快递员id不能为空或小于0");
			return j;
		}
		if(StringUtils.isBlank(merchantIds)){
			j.setMsg("楼层不能为空");
			return j;
		}
		msg = "保存成功";
		j.setMsg(msg);
		j.setSuccess(true);
		return j;
	}

	@RequestMapping(params = "confirm_follow")
	@ResponseBody
	public AjaxJson confirmFollow(@RequestParam Integer orderId, @RequestParam Integer userId){
		AjaxJson json = new AjaxJson();		
		try {
			Map<String, Object> followMap = new HashMap<String, Object>();
			followMap = courierService.getConfirmFollow(orderId, userId);
			if(followMap == null){
				Date followTime = DateTime.now().toDate();
				courierService.confirmFollow(orderId, userId, followTime);
				json.setMsg("操作成功");
				json.setSuccess(true);
			}
			else{
				json.setMsg("您已经回访过了");
				json.setSuccess(false);
			}
		} catch (Exception e) {			
			e.printStackTrace();
			json.setMsg("操作失败");
			json.setSuccess(false);
		}
		return json;
	}
	
	@RequestMapping(params = "getSupplyChainWarehouseCourierCount")
	@ResponseBody
	public AjaxJson getSupplyChainWarehouseCourierCount(String warehouseHandlerIdsStr){
		// 检查参数
		if(warehouseHandlerIdsStr == null || !warehouseHandlerIdsStr.matches("(\\d+,)*\\d+")){
			return AjaxJson.failJson("参数错误");
		}
		AjaxJson result = new AjaxJson();
		try {
			int courierCount = this.courierService.getSupplyChainWarehouseCourierCount(warehouseHandlerIdsStr);
			result.setObj(courierCount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = AjaxJson.failJson("获取供应链快递员数量失败");
		}
		return result;
	}
	
}

	
