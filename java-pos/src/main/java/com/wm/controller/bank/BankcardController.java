package com.wm.controller.bank;
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

import com.wm.dto.bank.BankCardDto;
import com.wm.entity.bank.BankcardEntity;
import com.wm.service.bank.BankcardServiceI;
import com.wm.util.spring.json.JsonParam;

/**   
 * @Title: Controller
 * @Description: bank_card
 * @author wuyong
 * @date 2015-01-07 09:49:41
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/bankcardController")
public class BankcardController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(BankcardController.class);

	@Autowired
	private BankcardServiceI bankcardService;
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
	 * bank_card列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "bankcard")
	public ModelAndView bankcard(HttpServletRequest request) {
		return new ModelAndView("com/wm/bank/bankcardList");
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
	public void datagrid(BankcardEntity bankcard,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(BankcardEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, bankcard);
		this.bankcardService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除bank_card
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(BankcardEntity bankcard, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		bankcard = systemService.getEntity(BankcardEntity.class, bankcard.getId());
		message = "删除成功";
		bankcardService.delete(bankcard);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加bank_card
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(BankcardEntity bankcard, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(bankcard.getId())) {
			message = "更新成功";
			BankcardEntity t = bankcardService.get(BankcardEntity.class, bankcard.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(bankcard, t);
				bankcardService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			bankcardService.save(bankcard);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}
	
	/**
	 * 添加bank_card
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveBankCard")
	@ResponseBody
	public AjaxJson saveBankCard(@JsonParam(keyname="datas", isResolveBody=true) BankCardDto dto) {
		AjaxJson j = new AjaxJson();
		
		String sql = " insert into bank_card(user_id,bank_id,card_no,`default`,name,phone,source_bank) "
				+ "values (?,?,?,?,?,?,?)";
		
		bankcardService.executeSql(sql,dto.getUserId(),dto.getBankId(),dto.getCardNo(),"Y",dto.getName(),dto.getPhone(),dto.getSourceBank());
		
		return j;
	}

	/**
	 * bank_card列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(BankcardEntity bankcard, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(bankcard.getId())) {
			bankcard = bankcardService.getEntity(BankcardEntity.class, bankcard.getId());
			req.setAttribute("bankcardPage", bankcard);
		}
		return new ModelAndView("com/wm/bank/bankcard");
	}
}
