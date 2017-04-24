package com.wm.controller.image;
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

import com.wm.entity.image.ImageEntity;
import com.wm.service.image.ImageServiceI;

/**   
 * @Title: Controller
 * @Description: image
 * @author wuyong
 * @date 2015-01-07 09:58:19
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/imageController")
public class ImageController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(ImageController.class);

	@Autowired
	private ImageServiceI imageService;
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
	 * image列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "image")
	public ModelAndView image(HttpServletRequest request) {
		return new ModelAndView("com/wm/image/imageList");
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
	public void datagrid(ImageEntity image,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ImageEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, image);
		this.imageService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除image
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(ImageEntity image, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		image = systemService.getEntity(ImageEntity.class, image.getId());
		message = "删除成功";
		imageService.delete(image);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加image
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ImageEntity image, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(image.getId())) {
			message = "更新成功";
			ImageEntity t = imageService.get(ImageEntity.class, image.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(image, t);
				imageService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			imageService.save(image);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * image列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(ImageEntity image, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(image.getId())) {
			image = imageService.getEntity(ImageEntity.class, image.getId());
			req.setAttribute("imagePage", image);
		}
		return new ModelAndView("com/wm/image/image");
	}
}
