package com.wm.controller.transfers;
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

import com.wm.entity.transfers.TransfersEntity;
import com.wm.service.transfers.TransfersServiceI;

/**   
 * @Title: Controller
 * @Description: transfers
 * @author wuyong
 * @date 2015-01-07 10:05:35
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/transfersController")
public class TransfersController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(TransfersController.class);

	@Autowired
	private TransfersServiceI transfersService;
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
	 * transfers列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "transfers")
	public ModelAndView transfers(HttpServletRequest request) {
		return new ModelAndView("com/wm/transfers/transfersList");
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
	public void datagrid(TransfersEntity transfers,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TransfersEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, transfers);
		this.transfersService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除transfers
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(TransfersEntity transfers, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		transfers = systemService.getEntity(TransfersEntity.class, transfers.getId());
		message = "删除成功";
		transfersService.delete(transfers);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加transfers
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(TransfersEntity transfers, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(transfers.getId())) {
			message = "更新成功";
			TransfersEntity t = transfersService.get(TransfersEntity.class, transfers.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(transfers, t);
				transfersService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			transfersService.save(transfers);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * transfers列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(TransfersEntity transfers, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(transfers.getId())) {
			transfers = transfersService.getEntity(TransfersEntity.class, transfers.getId());
			req.setAttribute("transfersPage", transfers);
		}
		return new ModelAndView("com/wm/transfers/transfers");
	}
}
