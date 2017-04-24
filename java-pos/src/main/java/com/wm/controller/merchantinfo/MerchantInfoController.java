package com.wm.controller.merchantinfo;
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

import com.wm.entity.merchantinfo.MerchantInfoEntity;
import com.wm.entity.user.WUserEntity;
import com.wm.service.merchantinfo.MerchantInfoServiceI;
import com.wm.service.user.WUserServiceI;

/**   
 * @Title: Controller
 * @Description: 商家最低提成价格设置表
 * @author wuyong
 * @date 2015-09-17 15:49:27
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/merchantInfoController")
public class MerchantInfoController extends BaseController {

	@Autowired
	private MerchantInfoServiceI merchantInfoService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private WUserServiceI wUserService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 商家最低提成价格设置表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "merchantInfo")
	public ModelAndView merchantInfo(HttpServletRequest request) {
		return new ModelAndView("com/wm/merchantinfo/merchantInfoList");
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
	public void datagrid(MerchantInfoEntity merchantInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(MerchantInfoEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, merchantInfo);
		this.merchantInfoService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除商家最低提成价格设置表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(MerchantInfoEntity merchantInfo, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		merchantInfo = systemService.getEntity(MerchantInfoEntity.class, merchantInfo.getId());
		message = "删除成功";
		merchantInfoService.delete(merchantInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加商家最低提成价格设置表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(MerchantInfoEntity merchantInfo,int userId, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		WUserEntity user = wUserService.get(WUserEntity.class, userId);
		if (user == null) {
			j.setMsg("用户为空");
			j.setSuccess(false);
			j.setStateCode("01");
			return j;
		}
		if(merchantInfo.getIsTakeout()==0 && merchantInfo.getIsHallFood() == 0){
			j.setMsg("外卖和堂食是否设置回扣必须填写一个为是");
			j.setSuccess(false);
			j.setStateCode("01");
			return j;
		}
		if (StringUtil.isNotEmpty(merchantInfo.getId())) {
			message = "更新成功";
			MerchantInfoEntity t = merchantInfoService.get(MerchantInfoEntity.class, merchantInfo.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(merchantInfo, t);
				merchantInfoService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			merchantInfoService.save(merchantInfo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		j.setSuccess(true);
		j.setStateCode("00");
		return j;
	}

	/**
	 * 商家最低提成价格设置表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(MerchantInfoEntity merchantInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(merchantInfo.getId())) {
			merchantInfo = merchantInfoService.getEntity(MerchantInfoEntity.class, merchantInfo.getId());
			req.setAttribute("merchantInfoPage", merchantInfo);
		}
		return new ModelAndView("com/wm/merchantinfo/merchantInfo");
	}
	
	
}
