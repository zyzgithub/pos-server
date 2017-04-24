package com.wm.controller.attendance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.controller.takeout.vo.AttendanceVo;
import com.wm.entity.attendance.AttendanceEntity;
import com.wm.service.attendance.AttendanceServiceI;
import com.wm.service.courier.CourierServiceI;
import com.wm.util.PageList;

/**
 * 
* @ClassName: AttendanceAdminController 
* @Description: 考勤记录表
* @author 黄聪
* @date 2015年9月18日 上午11:39:29 
*
 */
@Controller
@RequestMapping("attendanceAdminController")
public class AttendanceAdminController extends BaseController {

	@Autowired
	private AttendanceServiceI attendanceService;
	@Autowired
	private CourierServiceI courierService;
	@Autowired
	private SystemService systemService;
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
	 * 根据考勤记录表字段查询考勤记录分页列表
	 * 
	 * @param request
	 * @param attendance
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "findByEntityList")
	@ResponseBody
	public AjaxJson findByEntityList(HttpServletRequest request,
			AttendanceVo attendance,
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer rows) {
		AjaxJson j = new AjaxJson();
		if(StringUtil.isEmpty(attendance.getStartTime())){
			attendance.setStartTime(DateTime.now().toString("yyyy-MM-dd"));
		}
		if(StringUtil.isEmpty(attendance.getEndTime())){
			attendance.setEndTime(DateTime.now().toString("yyyy-MM-dd"));
		}
		PageList list = attendanceService.findByEntityList(attendance, page, rows);
		j.setObj(list);
		j.setStateCode("00");
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
}
