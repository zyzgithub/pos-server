package com.wm.controller.note;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.entity.note.NoteRecordEntity;
import com.wm.service.note.NoteRecordServiceI;

/**   
 * @Title: Controller
 * @Description: 短信发送记录表
 * @author wuyong
 * @date 2015-05-07 15:40:30
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/noteRecordController")
public class NoteRecordController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(NoteRecordController.class);

	@Autowired
	private NoteRecordServiceI noteRecordService;
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
	 * 短信发送记录表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "noteRecord")
	public ModelAndView noteRecord(HttpServletRequest request) {
		return new ModelAndView("com/wm/note/noteRecordList");
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
	public void datagrid(NoteRecordEntity noteRecord,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(NoteRecordEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, noteRecord);
		this.noteRecordService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除短信发送记录表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(NoteRecordEntity noteRecord, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		noteRecord = systemService.getEntity(NoteRecordEntity.class, noteRecord.getId());
		message = "删除成功";
		noteRecordService.delete(noteRecord);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加短信发送记录表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(NoteRecordEntity noteRecord, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(noteRecord.getId())) {
			message = "更新成功";
			NoteRecordEntity t = noteRecordService.get(NoteRecordEntity.class, noteRecord.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(noteRecord, t);
				noteRecordService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			noteRecordService.save(noteRecord);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 短信发送记录表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(NoteRecordEntity noteRecord, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(noteRecord.getId())) {
			noteRecord = noteRecordService.getEntity(NoteRecordEntity.class, noteRecord.getId());
			req.setAttribute("noteRecordPage", noteRecord);
		}
		return new ModelAndView("com/wm/note/noteRecord");
	}
}
