package com.wm.controller.category;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wm.entity.category.CategoryEntity;
import com.wm.service.category.CategoryServiceI;

/**   
 * @Title: Controller
 * @Description: category
 * @author wuyong
 * @date 2015-01-07 09:55:55
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/categoryController")
public class CategoryController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(CategoryController.class);

	@Autowired
	private CategoryServiceI categoryService;
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
	 * 获取店铺分类
	 * @return
	 */
	@RequestMapping(value="/group.do", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getGroup() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<CategoryEntity> list = categoryService.findByZone("group");
		
		map.put("state", "success");
		map.put("obj", list);
		
		return map;
	}
	
	/**
	 * category列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "category")
	public ModelAndView category(HttpServletRequest request) {
		return new ModelAndView("com/wm/category/categoryList");
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
	public void datagrid(CategoryEntity category,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CategoryEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, category);
		this.categoryService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除category
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(CategoryEntity category, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		category = systemService.getEntity(CategoryEntity.class, category.getId());
		message = "删除成功";
		categoryService.delete(category);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加category
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(CategoryEntity category, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(category.getId())) {
			message = "更新成功";
			CategoryEntity t = categoryService.get(CategoryEntity.class, category.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(category, t);
				categoryService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			categoryService.save(category);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * category列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(CategoryEntity category, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(category.getId())) {
			category = categoryService.getEntity(CategoryEntity.class, category.getId());
			req.setAttribute("categoryPage", category);
		}
		return new ModelAndView("com/wm/category/category");
	}
}
