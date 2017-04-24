package com.wm.controller.attendance;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.Constants;
import com.wm.controller.courier.CourierLocation;
import com.wm.controller.courier.SessionUtils;
import com.wm.controller.takeout.vo.AttendanceVo;
import com.wm.controller.takeout.vo.CourierOrgVo;
import com.wm.entity.attendance.AttendanceEntity;
import com.wm.entity.org.OrgEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.courier.CourierServiceI;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.service.couriersalary.CourierSalaryServiceI;
import com.wm.service.order.OrderServiceI;
import com.wm.service.systemconfig.SystemconfigServiceI;


/**
 * @Title: Controller
 * @Description: 考勤记录表
 * @author wuyong
 * @date 2015-08-25 15:00:10
 * @version V1.0
 *
 */
@Controller
@RequestMapping("ci/attendanceController")
public class AttendanceController extends BaseController {
	private final static Logger logger =    LoggerFactory.getLogger(AttendanceController.class);

	@Autowired
	private AttendanceServiceI attendanceService;
	@Autowired
	private CourierServiceI courierService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private OrderServiceI orderServiceI;
	@Autowired 
	private CourierSalaryServiceI courierSalaryService;
	@Autowired
	private SystemconfigServiceI systemconfigService;
	@Autowired
	private CourierInfoServiceI courierInfoService;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 考勤记录表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "attendance")
	public ModelAndView attendance(HttpServletRequest request) {
		return new ModelAndView("com/wm/attendance/attendanceList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(AttendanceEntity attendance,
			HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AttendanceEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
				attendance);
		this.attendanceService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除考勤记录表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(AttendanceEntity attendance, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		attendance = systemService.getEntity(AttendanceEntity.class,
				attendance.getId());
		message = "删除成功";
		attendanceService.delete(attendance);
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 添加考勤记录表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(AttendanceEntity attendance, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(attendance.getId())) {
			message = "更新成功";
			AttendanceEntity t = attendanceService.get(AttendanceEntity.class,
					attendance.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(attendance, t);
				attendanceService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE,
						Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			attendanceService.save(attendance);
			systemService.addLog(message, Globals.Log_Type_INSERT,
					Globals.Log_Leavel_INFO);
		}

		return j;
	}

	/**
	 * 考勤记录表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(AttendanceEntity attendance,
			HttpServletRequest req) {
		if (StringUtil.isNotEmpty(attendance.getId())) {
			attendance = attendanceService.getEntity(AttendanceEntity.class,
					attendance.getId());
			req.setAttribute("attendancePage", attendance);
		}
		return new ModelAndView("com/wm/attendance/attendance");
	}

	/**
	 * 保存快递员考勤记录
	 * 
	 * @param userId 用户
	 * @param type
	 * @param longitude
	 * @param latitude
	 * @param address
	 * @return
	 */
	@RequestMapping(params = "saveAttendance")
	@ResponseBody
	public AjaxJson saveAttendance(@RequestParam int userId, @RequestParam int type, @RequestParam String longitude,
			@RequestParam String latitude, String address,String deviceInfo) {
		AjaxJson j = new AjaxJson();		
		try {
			if (userId != 0) {
				logger.info("快递员id: " + userId +"  type :"+type + " 经度:" + longitude + " 纬度：" + latitude);
				Integer courierType = courierInfoService.getCourierTypeByUserId(userId);
				if(courierType != null && courierType.intValue() == Constants.COURIER){
					boolean isInTheRang = attendanceService.isInTheRange(userId, Double.valueOf(longitude), Double.valueOf(latitude));
					if(!isInTheRang){
						j.setSuccess(false);
						j.setMsg("请在规定地点打卡" );
						j.setStateCode("02");	
						return j;
					}
				}
				
				WUserEntity wUserEntity = systemService.get(WUserEntity.class, userId);
				if(wUserEntity == null){
					j.setSuccess(false);
					j.setMsg("没有找到该快递员  " );
					j.setStateCode("01");	
					return j;
				}
				if(wUserEntity.getUserState() !=null && wUserEntity.getUserState()==5){
					j.setSuccess(false);
					j.setMsg("您的账户已被锁定,请与您的上级联系" );
					j.setStateCode("01");	
					return j;
				}
				if(type == 1){
					String time = DateTime.now().toString("yyyy-MM-dd");
					Integer  orderCount = orderServiceI.getOrderNumber(userId, time);
					if(orderCount.intValue() != 0){
						j.setSuccess(false);
						j.setMsg("您还有未完成的订单，请完成所有订单以后再下班哦");
						j.setStateCode("01");	
						return j;
					}
				if (!attendanceService.isOnDuty(userId)) {
					j.setSuccess(false);
					j.setMsg("您当前是下班状态，暂时无法打卡下班");
					j.setStateCode("01");	
					return j;
					}
				}else if(type == 0){
				if (attendanceService.isOnDuty(userId)) {
					j.setSuccess(false);
					j.setMsg("您当前是上班状态，暂时无法打卡上班");
					j.setStateCode("01");	
					return j;
					}
				}
				AttendanceEntity attendance = new AttendanceEntity();
				attendance.setLatitude(new BigDecimal(latitude));
				attendance.setLongitude(new BigDecimal(longitude));
				attendance.setAddress(address);
				attendance.setType(type);
				attendance.setUserId(userId);
				attendance.setCreateTime(DateUtils.getSeconds());
				attendance.setDeviceInfo(deviceInfo);
				attendanceService.save(attendance);
				
				try {
					courierService.saveUserLocation(userId, Double.parseDouble(longitude), Double.parseDouble(latitude));
					SessionUtils.broadcast(JSON.toJSONString(new CourierLocation(Double.parseDouble(longitude), Double.parseDouble(latitude), userId)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				//默认是5分钟
				Integer confValue = 300;
				try {
					confValue = Integer.parseInt(systemconfigService.getValByCode("report_pos_frequency"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Map<String, Object> obj = new HashMap<String, Object>();
				obj.put("reportPositionFrequency", confValue);
				j.setObj(obj);
				j.setSuccess(true);
				j.setMsg("保存成功");
				j.setStateCode("00");
			}				
			 else {
				j.setSuccess(false);
				j.setMsg("参数错误");
				j.setStateCode("01");
			}
		} catch (NumberFormatException e) {			
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("保存失败");
			j.setStateCode("01");
		}
		return j;
	}

	/**
	 * 获得快递员当天考勤记录列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param page
	 *            起始行
	 * @param rows
	 *            每页显示行数
	 * @return
	 */
	@RequestMapping(params = "queryAttendancesByUserId")
	@ResponseBody
	public AjaxJson queryAttendancesByUserId(int userId, int page, int rows) {
		logger.info("获取快递员" + userId + "当天的考勤记录");
		AjaxJson j = new AjaxJson();
		if (userId != 0) {
			if (page == 0) {
				page = 1;
			}					
			
			String begin = DateTime.now().toString("yyyy-MM-dd");
			String end = DateTime.now().toString("yyyy-MM-dd");
			
			List<AttendanceVo> attendances = attendanceService.queryByUserId(
					userId, begin, end, page, rows);
			if (attendances != null && attendances.size() > 0) {
				j.setObj(attendances);
				j.setSuccess(true);
				j.setStateCode("00");
			} else {
				j.setMsg("暂无考勤记录");
				j.setSuccess(false);
				j.setStateCode("01");
			}
		} else {
			j.setMsg("查询考勤记录失败，快递员ID不允许为空！");
			j.setSuccess(false);
		}
		return j;
	}
	
	
	/**
	 * 获得快递员一个月考勤记录列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param page
	 *            起始行
	 * @param rows
	 *            每页显示行数
	 * @return
	 */
	@RequestMapping(params = "queryMonthAttendancesByUserId")
	@ResponseBody
	public AjaxJson queryMonthAttendancesByUserId(int userId, int page, int rows) {
		logger.info("查询快递员" + userId + "一个月的考勤记录");
		AjaxJson j = new AjaxJson();
		if (userId != 0) {
			if (page == 0) {
				page = 1;
			}						
			
			String end = DateTime.now().toString("yyyy-MM-dd");
			String begin = DateTime.now().minusDays(31).toString("yyyy-MM-dd");
			
			List<AttendanceVo> attendances = attendanceService.queryByUserId(
					userId, begin, end, page, rows);
			if (CollectionUtils.isNotEmpty(attendances)) {
				List<AttendancDto> attendanceDtoList = new ArrayList<AttendancDto>();
				
				AttendanceVo pre = null;
				
				AttendancDto eachDayAttendancDto = null;
				for(int i = 0; i < attendances.size(); i++){
					AttendanceVo cur = attendances.get(i);
					String preDateTime = "";
					if(pre != null){
						preDateTime = pre.getCreateTime().substring(0, 10);
					}
					String curDateTime = cur.getCreateTime().substring(0, 10);
					//第一次迭代或者当前的记录时间与上一条不一样
					if(pre == null || !curDateTime.equals(preDateTime)){
						eachDayAttendancDto = new AttendancDto();
						eachDayAttendancDto.setDate(curDateTime);
						eachDayAttendancDto.addAttendRecord(cur);
						attendanceDtoList.add(eachDayAttendancDto);
					}
					else{
						eachDayAttendancDto.addAttendRecord(cur);
					}
					//每次迭代pre都指向当条记录
					pre = cur;
				}				
				j.setObj(attendanceDtoList);
				j.setSuccess(true);
				j.setStateCode("00");
			} else {
				j.setMsg("暂无考勤记录");
				j.setSuccess(false);
				j.setStateCode("01");
			}
		} else {
			j.setMsg("查询考勤记录失败，快递员ID不允许为空！");
			j.setSuccess(false);
		}
		return j;
	}


	/**
	 * 根据区域获得快递员考勤记录列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param orgName
	 * @param startTime
	 *            (格式：YYYY-MM-dd)
	 * @param endTime
	 *            (格式：YYYY-MM-dd)
	 * @param page
	 *            起始行
	 * @param rows
	 *            每页显示行数
	 * @return
	 */
	@RequestMapping(params = "queryAttendances")
	@ResponseBody
	public AjaxJson queryAttendances(int userId, String orgName,
			String startTime, String endTime, int page, int rows) {
		AjaxJson j = new AjaxJson();
		j.setMsg("暂无信息");
		j.setSuccess(false);
		j.setStateCode("01");
		List<List<AttendanceVo>> attendanceVoList = new ArrayList<List<AttendanceVo>>();
		if (userId != 0) {
			if (StringUtil.isEmpty(startTime)) {
				startTime = DateTime.now().toString("yyyy-MM-dd");
			}
			if (StringUtil.isEmpty(endTime)) {
				endTime = DateTime.now().toString("yyyy-MM-dd");
			}
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
										for (int k = 0; k < courierOrgs.size(); k++) {
											CourierOrgVo courierOrgVo = courierOrgs
													.get(k);
											if (courierOrgVo != null) {
												List<AttendanceVo> attendances = attendanceService
														.queryByUserId(
																courierOrgVo
																		.getCourierId(),
																startTime,
																endTime, page,
																rows);
												attendanceVoList
														.add(attendances);
											}
										}
										if (attendanceVoList != null
												&& attendanceVoList.size() > 0) {
											j.setObj(attendanceVoList);
											j.setMsg("成功");
											j.setSuccess(true);
											j.setStateCode("00");
											return j;
										}

									}
								}
							}
						} else {
							OrgEntity org = orgList.get(0);
							List<CourierOrgVo> courierOrgs = courierService
									.queryOrgByOrgName(org.getOrgName(), page,
											rows);
							if (courierOrgs != null && courierOrgs.size() > 0) {
								for (int k = 0; k < courierOrgs.size(); k++) {
									CourierOrgVo courierOrgVo = courierOrgs
											.get(k);
									if (courierOrgVo != null) {
										List<AttendanceVo> attendances = attendanceService
												.queryByUserId(courierOrgVo
														.getCourierId(),
														startTime, endTime,
														page, rows);
										attendanceVoList.add(attendances);
									}
								}
								if (attendanceVoList != null
										&& attendanceVoList.size() > 0) {
									j.setObj(attendanceVoList);
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

		}
		return j;
	}
	
	@RequestMapping(params = "isOnDuty")
	@ResponseBody
	public AjaxJson isOnDuty(@RequestParam Integer courierId){
		AjaxJson ajaxJson = new AjaxJson();
		try {
			logger.info("isOnDuty, request param:"+ courierId);
			double lowestWorkhours = courierSalaryService.getLowestWorkHoursPerDay();
			WUserEntity courier = systemService.get(WUserEntity.class, courierId);
			if(courier == null){
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("无此快递员!");
				ajaxJson.setStateCode("01");
			}else {
				boolean onduty = attendanceService.isOnDuty(courierId);
				ajaxJson.setSuccess(true);
				ajaxJson.setMsg("获取成功");
				
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("isOnduty", onduty);
				result.put("lowestWorkhours", lowestWorkhours);
				ajaxJson.setObj(result);
			}
			logger.info("isOnDuty,return:"+ JSON.toJSONString(ajaxJson));
			return ajaxJson;
		} catch (Exception e) {
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("获取失败");
			logger.warn("isOnDuty, return:"+JSON.toJSONString(ajaxJson));
			return ajaxJson;
		}
	}
	
	@RequestMapping(params = "getEffectiveTime")
	@ResponseBody
	public AjaxJson getEffectiveTime(Integer courierId){
		AjaxJson json = new AjaxJson();
		Map<String, Object> effectiveMap = new HashMap<String, Object>();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateTime dateTime = DateTime.now();
			String time = dateTime.toString("yyyy-MM-dd");
			double lowestWorkhours = courierSalaryService.getLowestWorkHoursPerDay();
			Integer state = 1;
			Map<String, Object> firstAttendanceTimeMap = attendanceService.getFirstAttendanceTime(courierId, time);
			if(firstAttendanceTimeMap == null){
				json.setMsg("您还没有打卡上班");
				json.setSuccess(false);
				json.setStateCode("02");
				return json;
			}
			
			if(firstAttendanceTimeMap.get("onduttyTime") != null && firstAttendanceTimeMap.get("offduttyTime") == null){
				String onduttyTime = firstAttendanceTimeMap.get("onduttyTime").toString();
				DateTime start = new DateTime(format.parse(onduttyTime));
				Integer minutes = Minutes.minutesBetween(start, dateTime).getMinutes();
				double hours = minutes/60;
				if(hours < lowestWorkhours){
					state = 0;
				}
				String effevtiveTime = ((Hours.hoursBetween(start, dateTime).getHours() < 10) ? ("0" + Hours.hoursBetween(start, dateTime).getHours()): (Hours.hoursBetween(start, dateTime).getHours())) + ":" 
				+ ((minutes%60 < 10 )? ("0" + minutes%60): minutes%60);
				effectiveMap.put("effectiveTime", effevtiveTime);
				effectiveMap.put("state", state);
				json.setObj(effectiveMap);
				json.setSuccess(true);
				json.setStateCode("00");
				return json;
			}
			String onduttyTime = firstAttendanceTimeMap.get("onduttyTime").toString();
			String offduttyTime = firstAttendanceTimeMap.get("offduttyTime").toString();
			DateTime dt1 = new DateTime(format.parse(onduttyTime));
			DateTime dt2 = new DateTime(format.parse(offduttyTime));
			Integer minutes = Minutes.minutesBetween(dt1, dt2).getMinutes();
			double hours = minutes/60;
			if(hours < lowestWorkhours){
				state = 0;
			}
			String effevtiveTime = Hours.hoursBetween(dt1, dt2).getHours() + "小时" + minutes%60 + "分";
			effectiveMap.put("effectiveTime", effevtiveTime);
			effectiveMap.put("state", state);
			json.setObj(effectiveMap);
			json.setSuccess(true);
			json.setStateCode("00");
			
		} catch (ParseException e) {
			e.printStackTrace();
			json.setMsg("获取有效工作时间失败");
			json.setSuccess(false);
			json.setStateCode("01");
		}
		return json;
	}
}
