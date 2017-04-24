package com.wm.controller.courierdevelop;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jeecg.system.service.SystemService;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.wm.controller.courier.dto.CourierDevMerchantPhase;
import com.wm.controller.courier.dto.DevMerchantPhase;
import com.wm.controller.courierdevelop.dto.MerchantDevRecDTO;
import com.wm.service.courierdevlop.CourierMerchantDepPhaseServiceI;

@Controller
@RequestMapping("ci/courierDevelopController")
public class CourierDevelopController extends BasicController{
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CourierDevelopController.class);
	
	@Autowired
	private CourierMerchantDepPhaseServiceI courierMerchantDepPhaseService;
	
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
	 * 保存招商录入记录信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(MerchantDevRecDTO vo, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (vo.getId() != 0) {
			message = "更新成功";
			try {
				j = courierMerchantDepPhaseService.updateCourierDevPhase(vo);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			message = "添加成功";
			j = courierMerchantDepPhaseService.createCourierDevPhase(vo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		return j;
	}
	
	
	@RequestMapping(params = "getDevMerchantDefinition")
	@ResponseBody
	public AjaxJson getDevMerchantDefinition(){
		AjaxJson ajaxJson = new AjaxJson();
		try {
			List<DevMerchantPhase> phases = courierMerchantDepPhaseService.getDevMerchantDefinition();
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("获取招商入阶段定义成功");
			ajaxJson.setObj(phases);
			ajaxJson.setStateCode("00");
			return ajaxJson;
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("获取成功招商入阶段失败");
			ajaxJson.setStateCode("01");
			return ajaxJson;
		}
	}
	
	@RequestMapping(params = "getCourierDevMerchantPhase")
	@ResponseBody
	public AjaxJson getCourierDevMerchantPhase(@RequestParam Integer devId){
		AjaxJson ajaxJson = new AjaxJson();
		try {
			CourierDevMerchantPhase devMerchantPhase = courierMerchantDepPhaseService.getCourierDevMerchantPhase(devId);
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("获取招商入阶段的状态成功");
			ajaxJson.setObj(devMerchantPhase);
			ajaxJson.setStateCode("00");
			return ajaxJson;
		} 
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("获取招商入阶段的状态失败");
			ajaxJson.setStateCode("01");
			return ajaxJson;
		}
	}
	
	
	/**
	 * 根据快递员ID查询此快递员的招商录入列表
	 * @param courierId
	 * @param page 开始页, 第一页为1，不能为0, 默认为1
	 * @param rows 每页行数，默认为10
	 * @return
	 */
	@RequestMapping(params = "getCourierDevMerchantHistory")
	@ResponseBody
	public AjaxJson queryMerchantDevList(@RequestParam int courierId,
			@RequestParam(value = "page" ,defaultValue = "1") Integer page,
			@RequestParam(value = "rows" ,defaultValue = "10") Integer rows) {
		AjaxJson j = new AjaxJson();
		try {
			if (courierId != 0) {
				if (page == 0) {
					page = 1;
				}
				List<Map<String, Object>> scopeList = courierMerchantDepPhaseService.getCourierDevMerchantHistory(courierId, page, rows);
				if (scopeList != null && scopeList.size() > 0) {
					j.setObj(scopeList);
					j.setSuccess(true);
					j.setMsg("获取快递员的招商录入记录成功");
				} else {
					j.setMsg("暂无快递员的招商录入记录");
					j.setSuccess(false);
				}
			} else {
				j.setMsg("查询快递员的招商录入记录，快递员ID不允许为空！");
				j.setSuccess(false);
			}
		} catch (Exception e) {
			j.setSuccess(false);
			j.setState("01");
			j.setMsg("获取招商记录失败！");
		}
		return j;
	}
}
