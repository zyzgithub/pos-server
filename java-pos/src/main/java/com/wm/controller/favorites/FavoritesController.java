package com.wm.controller.favorites;

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

import com.wm.entity.favorites.FavoritesEntity;
import com.wm.service.favorites.FavoritesServiceI;

/**
 * @Title: Controller
 * @Description: favorites
 * @author wuyong
 * @date 2015-01-07 09:57:02
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("ci/favoritesController")
public class FavoritesController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FavoritesController.class);

	@Autowired
	private FavoritesServiceI favoritesService;
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
	 * favorites列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "favorites")
	public ModelAndView favorites(HttpServletRequest request) {
		return new ModelAndView("com/wm/favorites/favoritesList");
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
	public void datagrid(FavoritesEntity favorites, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(FavoritesEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
				favorites);
		this.favoritesService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除favorites
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(FavoritesEntity favorites, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		favorites = systemService.getEntity(FavoritesEntity.class,
				favorites.getId());
		message = "删除成功";
		favoritesService.delete(favorites);
		systemService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 添加favorites
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(FavoritesEntity favorites, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(favorites.getId())) {
			message = "更新成功";
			FavoritesEntity t = favoritesService.get(FavoritesEntity.class,
					favorites.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(favorites, t);
				favoritesService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE,
						Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			favoritesService.save(favorites);
			systemService.addLog(message, Globals.Log_Type_INSERT,
					Globals.Log_Leavel_INFO);
		}

		return j;
	}

	/**
	 * favorites列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(FavoritesEntity favorites,
			HttpServletRequest req) {
		if (StringUtil.isNotEmpty(favorites.getId())) {
			favorites = favoritesService.getEntity(FavoritesEntity.class,
					favorites.getId());
			req.setAttribute("favoritesPage", favorites);
		}
		return new ModelAndView("com/wm/favorites/favorites");
	}

	@RequestMapping(params = "collectOrCancle")
	@ResponseBody
	public AjaxJson collectOrCancle(String type, int userid, int itemId) {
		AjaxJson j = new AjaxJson();

		String result = favoritesService.collectOrCancle(type, userid, itemId);

		if ("cancle".equals(result)) {
			j.setMsg("取消收藏");
		} else if ("collect".equals(result)) {
			j.setMsg("收藏成功");
		} else {
			j.setSuccess(false);
			j.setStateCode("01");
			j.setMsg("操作异常");
		}

		return j;
	}

}
