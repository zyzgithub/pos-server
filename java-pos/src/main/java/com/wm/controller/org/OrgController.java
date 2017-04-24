package com.wm.controller.org;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jeecg.system.service.SystemService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.courier_mana.common.Constants;
import com.wm.entity.org.OrgEntity;
import com.wm.service.courierinfo.CourierInfoServiceI;
import com.wm.service.org.OrgServiceI;

/**   
 * @Title: Controller
 * @Description: 组织架构表
 * @author wuyong
 * @date 2015-08-28 09:22:47
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/orgController")
public class OrgController extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OrgController.class);

	@Autowired
	private OrgServiceI orgService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private CourierInfoServiceI courierInfoService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 组织架构表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "org")
	public ModelAndView org(HttpServletRequest request) {
		return new ModelAndView("com/wm/org/orgList");
	}


	/**
	 * 删除组织架构表
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(OrgEntity org, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		org = systemService.getEntity(OrgEntity.class, org.getId());
		message = "删除成功";
		orgService.delete(org);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加组织架构表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(OrgEntity org, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(org.getId())) {
			message = "更新成功";
			OrgEntity t = orgService.get(OrgEntity.class, org.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(org, t);
				orgService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			orgService.save(org);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		
		return j;
	}

	/**
	 * 组织架构表列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(OrgEntity org, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(org.getId())) {
			org = orgService.getEntity(OrgEntity.class, org.getId());
			req.setAttribute("orgPage", org);
		}
		return new ModelAndView("com/wm/org/org");
	}
	
	/**
	 * 获取机构id和机构名称
	 * @return
	 */
	@RequestMapping(params = "getOrgIdAndName")
	@ResponseBody
	public AjaxJson getCourierBlongAreaIdAndName(){
		AjaxJson json = new AjaxJson();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list= orgService.getOrgIdAndName(Constants.CITY_LEVEL);
			json.setObj(list);
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setObj(list);
			json.setSuccess(false);
			json.setMsg("获取城市失败");
			json.setStateCode("01");
		}
		return json;
	}
	
	
	/**
	 * 获取平台快递员绑定网点
	 * @param courierId 快递员id
	 * @return
	 */
	@RequestMapping(params = "getOrgByCourierId")
	@ResponseBody
	public AjaxJson getOrgCourierId(@RequestParam Integer courierId, @RequestParam int page, @RequestParam int rows){
		logger.info("快递员" + courierId + "获取绑定的网点");
		AjaxJson json = new AjaxJson();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			Integer courierType = courierInfoService.getCourierTypeByUserId(courierId);
			if(courierType.intValue() != 1){
				map.put("orgName", "");
				map.put("address", "");
				list.add(map);
				json.setMsg("您不是平台快递员,不需要绑定网点");
				json.setObj(list);
				json.setSuccess(false);
				json.setStateCode("03");
				return json;
			}
			list = orgService.getOrgByCourierId(courierId, page, rows);
			if(CollectionUtils.isEmpty(list)){
				json.setSuccess(false);
				json.setStateCode("02");
				json.setMsg("您没有绑定网点,请联系您的上级");
				if(page >= 2){
					json.setMsg("您没有绑定更多的网点");
				}
				return json;
			}
			json.setSuccess(true);
			json.setStateCode("00");
			json.setObj(list);
			json.setMsg("获取快递员网点成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("获取快递员绑定网点失败");
		}
		return json;
	}
}
