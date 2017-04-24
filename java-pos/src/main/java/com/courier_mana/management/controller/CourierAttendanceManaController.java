package com.courier_mana.management.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.common.vo.SearchVo;
import com.courier_mana.management.service.CourierAttendanceManaService;

/**(OvO)
 * 管理-考勤管理模块Controller
 * @author hyj
 *
 */
@Controller
@RequestMapping("/ci/courier/management")
public class CourierAttendanceManaController extends BasicController {
	private final static Logger logger = LoggerFactory.getLogger(CourierAttendanceManaController.class);
	
	@Autowired
	private CourierAttendanceManaService courierAttendanceManaService;
	
	/**
	 * 获取快递员考勤信息
	 * @param courierId	快递员ID(必选)
	 * @param vo		搜索条件(时间, 区域)VO
	 * @return	返回管理-考勤管理页面所有信息
	 */
	@RequestMapping("/courierAttendance")
	@ResponseBody
	public AjaxJson courierAttendance(Integer courierId, SearchVo vo){
		logger.info("Invoke method: realTimeStatistic， params: courierId - {}, vo.timeType - {}, vo.beginTime - {}, vo.endTime - {}, vo.orgId - {}",
																courierId, vo.getTimeType(), vo.getBeginTime(), vo.getEndTime(), vo.getOrgId());
		/**
		 * 检查必要的参数
		 */
		if(courierId == null){
			return FAIL("01","参数: 快递员ID,不能为空");
		}
		try{
			return SUCCESS(this.courierAttendanceManaService.courierAttendance(vo, courierId));
		}catch(Exception e){
			e.printStackTrace();
			return FAIL("02", "获取快递员考勤信息失败: " + e.getMessage());
		}
	}
}
