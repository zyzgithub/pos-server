package com.wm.controller.card;
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

import com.wm.entity.card.CardEntity;
import com.wm.service.card.CardServiceI;

/**   
 * @Title: Controller
 * @Description: card
 * @author wuyong
 * @date 2015-01-07 11:03:26
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/cardController")
public class CardController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(CardController.class);

	@Autowired
	private CardServiceI cardService;
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
	 * card列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "card")
	public ModelAndView card(HttpServletRequest request) {
		return new ModelAndView("com/wm/card/cardList");
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
	public void datagrid(CardEntity card,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CardEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, card);
		this.cardService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除card
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CardEntity card, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		card = systemService.getEntity(CardEntity.class, card.getId());
		message = "删除成功";
		cardService.delete(card);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加card
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CardEntity card, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(card.getId())) {
			message = "更新成功";
			CardEntity t = cardService.get(CardEntity.class, card.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(card, t);
				cardService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			cardService.save(card);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * card列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CardEntity card, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(card.getId())) {
			card = cardService.getEntity(CardEntity.class, card.getId());
			req.setAttribute("cardPage", card);
		}
		return new ModelAndView("com/wm/card/card");
	}
}
