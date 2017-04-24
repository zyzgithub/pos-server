package com.wm.controller.bank;
import java.util.ArrayList;
import java.util.List;

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

import com.wm.entity.bank.BankEntity;
import com.wm.service.bank.BankServiceI;

/**   
 * @Title: Controller
 * @Description: bank
 * @author wuyong
 * @date 2015-01-07 09:45:18
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/bankController")
public class BankController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(BankController.class);

	@Autowired
	private BankServiceI bankService;
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
	 * bank列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "bank")
	public ModelAndView bank(HttpServletRequest request) {
		return new ModelAndView("com/wm/bank/bankList");
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
	public void datagrid(BankEntity bank,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(BankEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, bank);
		this.bankService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除bank
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(BankEntity bank, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		bank = systemService.getEntity(BankEntity.class, bank.getId());
		message = "删除成功";
		bankService.delete(bank);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加bank
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(BankEntity bank, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(bank.getId())) {
			message = "更新成功";
			BankEntity t = bankService.get(BankEntity.class, bank.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(bank, t);
				bankService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			bankService.save(bank);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * bank列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(BankEntity bank, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(bank.getId())) {
			bank = bankService.getEntity(BankEntity.class, bank.getId());
			req.setAttribute("bankPage", bank);
		}
		return new ModelAndView("com/wm/bank/bank");
	}
	
	/**
	 * 获取银行信息
	 * @return
	 */
	@RequestMapping(params = "getBank")
	@ResponseBody
	public AjaxJson getBank(){
		AjaxJson json = new AjaxJson();
		List<BankEntity> bankList = new ArrayList<BankEntity>();
		try {				
			bankList = bankService.getBank();		
			json.setObj(bankList);
		} catch (Exception e) {			
			e.printStackTrace();
			json.setMsg("获取银行信息失败");
			json.setSuccess(false);
		}
		return json;
		
	}
}
