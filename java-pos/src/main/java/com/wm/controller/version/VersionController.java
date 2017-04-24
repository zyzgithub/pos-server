package com.wm.controller.version;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wm.service.merchant.MerchantServiceI;
import jeecg.system.service.SystemService;

import org.apache.commons.lang3.StringUtils;
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

import com.wm.entity.version.VersionEntity;
import com.wm.service.version.VersionServiceI;

/**   
 * @Title: Controller
 * @Description: 客户端版本管理
 * @author Simon
 * @date 2015-08-11 13:09:24
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/versionController")
public class VersionController extends BaseController {

	@Autowired
	private VersionServiceI versionService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private MerchantServiceI merchantService;
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 客户端版本管理列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "version")
	public ModelAndView version(HttpServletRequest request) {
		return new ModelAndView("com/wm/version/versionList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(VersionEntity version,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(VersionEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, version);
		this.versionService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除客户端版本管理
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(VersionEntity version, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		version = systemService.getEntity(VersionEntity.class, version.getId());
		message = "删除成功";
		versionService.delete(version);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加客户端版本管理
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(VersionEntity version, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(version.getId())) {
			message = "更新成功";
			VersionEntity t = versionService.get(VersionEntity.class, version.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(version, t);
				versionService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			versionService.save(version);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 客户端版本管理列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(VersionEntity version, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(version.getId())) {
			version = versionService.getEntity(VersionEntity.class, version.getId());
			req.setAttribute("versionPage", version);
		}
		return new ModelAndView("com/wm/version/version");
	}
	
	
	/**
	 * 获取客户端最新版本(例如：http://XXXXXX/versionController.do?getLastVersion&type=xxxxx)
	 * @param type 客户端类型
	 * @return  操作结果,json格式
	 */
	@RequestMapping(params="getLastVersion", method=RequestMethod.GET)
	@ResponseBody
	public AjaxJson getLastVersion(HttpServletRequest request,HttpServletResponse response,String type){
		AjaxJson result = new AjaxJson();
		result.setSuccess(false);
		message = "获取客户端最新版本失败";
		result.setMsg(message);
		if(StringUtil.isNotEmpty(type)){

			// 系统分切,引导客户去新的下载地址下载APP
			/*String merchantId = request.getParameter("merchantId");
			if(StringUtils.isNotBlank(merchantId)){
				Integer intMchId = Integer.parseInt(merchantId);
				String platformType = merchantService.getMerchantPlatformType(intMchId);
				if("2".equals(platformType)){
					type = (Integer.parseInt(type) + 100) + "";
				}
			}*/

			VersionEntity version = versionService.getLastVersion(type);
			result.setObj(version);
			result.setSuccess(true);
			message = "获取客户端最新版本成功";
			result.setMsg(message);
		}
		systemService.addLog(message, Globals.Log_Type_OTHER, Globals.Log_Leavel_INFO);
		return result;
	}
}
