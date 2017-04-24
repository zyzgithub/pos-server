package com.wm.controller.couriergroupmember;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jeecg.system.service.SystemService;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.entity.couriergroupmember.CourierGroupMemberEntity;
import com.wm.service.couriergroupmember.CourierGroupMemberServiceI;

/**   
 * @Title: Controller
 * @Description: 关联表：快递员组-成员
 * @author wuyong
 * @date 2015-08-28 09:31:38
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/courierGroupMemberController")
public class CourierGroupMemberController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CourierGroupMemberController.class);

	@Autowired
	private CourierGroupMemberServiceI courierGroupMemberService;
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
	 * 关联表：快递员组-成员列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "courierGroupMember")
	public ModelAndView courierGroupMember(HttpServletRequest request) {
		return new ModelAndView("com/wm/couriergroupmember/courierGroupMemberList");
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
	public void datagrid(CourierGroupMemberEntity courierGroupMember,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CourierGroupMemberEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, courierGroupMember);
		this.courierGroupMemberService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除关联表：快递员组-成员
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CourierGroupMemberEntity courierGroupMember, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		courierGroupMember = systemService.getEntity(CourierGroupMemberEntity.class, courierGroupMember.getId());
		message = "删除成功";
		courierGroupMemberService.delete(courierGroupMember);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加关联表：快递员组-成员
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CourierGroupMemberEntity courierGroupMember, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(courierGroupMember.getId())) {
			message = "更新成功";
			CourierGroupMemberEntity t = courierGroupMemberService.get(CourierGroupMemberEntity.class, courierGroupMember.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(courierGroupMember, t);
				courierGroupMemberService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			courierGroupMemberService.save(courierGroupMember);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 关联表：快递员组-成员列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CourierGroupMemberEntity courierGroupMember, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(courierGroupMember.getId())) {
			courierGroupMember = courierGroupMemberService.getEntity(CourierGroupMemberEntity.class, courierGroupMember.getId());
			req.setAttribute("courierGroupMemberPage", courierGroupMember);
		}
		return new ModelAndView("com/wm/couriergroupmember/courierGroupMember");
	}
}
