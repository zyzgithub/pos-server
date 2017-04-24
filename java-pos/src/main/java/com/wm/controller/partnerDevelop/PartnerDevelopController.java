package com.wm.controller.partnerDevelop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wm.controller.partnerDevelop.dto.PartnerDevelopDTO;
import com.wm.service.partnerDevelop.PartnerDevelopServiceI;
import com.wm.util.StringUtil;

@Controller
@RequestMapping("ci/partnerDevelopController")
public class PartnerDevelopController {

	private static final Logger logger = Logger.getLogger(PartnerDevelopController.class);
	
	@Autowired
	private PartnerDevelopServiceI pdservice;
	/**
	 * 保存合作商开拓记录
	 * @param merchant
	 * @return
	 */
	@RequestMapping(params = "savePartnerDevelop")
	@ResponseBody
	public AjaxJson savePartnerDevelop(@Valid PartnerDevelopDTO partnerDevelop, Errors errors){
		AjaxJson json = new AjaxJson();
		logger.info("传入的 参数: " + JSON.toJSONString(partnerDevelop));
		if(errors.hasErrors()){
			List<FieldError> errorsList = errors.getFieldErrors();
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			for(int i = 0; i < errorsList.size(); i++){
				errorsMap.put(errorsList.get(i).getField(), "错误值： " + errorsList.get(i).getRejectedValue());
			}
			logger.error("参数错误, 参数: " + JSON.toJSONString(partnerDevelop));
			json.setSuccess(false);			
			json.setObj(errorsMap);
			json.setMsg("保存合作商开拓信息失败");
			json.setStateCode("02");
			logger.info("参数错误  return:" + JSON.toJSONString(json));
			return json;
			}
			//验证身份证号码
			if(!StringUtil.isIdCard(partnerDevelop.getIdCard())){
				json.setMsg("您输入的身份证号码无效");
				json.setSuccess(false);
				json.setStateCode("01");
				return  json;
			}
			//验证手机号码
			if(!StringUtil.isMobileNumber(partnerDevelop.getPhone())){
				json.setSuccess(false);
				json.setStateCode("03");
				json.setMsg("您输入的手机号码无效");
				return json;
			}
			return pdservice.savePartnerDevelop(partnerDevelop);			
	}
	
	/**
	 * 根据业务员id获取合作商开拓历史
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(params = "getPartnerDevelopList")
	@ResponseBody
	public AjaxJson getPartnerDevelopList(@RequestParam Integer courierId, int page, int rows){
		AjaxJson json = new AjaxJson();
		try {
			List<Map<String, Object>> list = pdservice.getPartnerDevelopHis(courierId,page,rows);
			if(list.size() > 0){
				json.setObj(list);
				json.setSuccess(true);
				json.setStateCode("00");
				json.setMsg("获取合作商开拓记录成功");
			}
			else{
				json.setObj(list);
				json.setSuccess(false);
				json.setStateCode("02");
				json.setMsg("您没有合作商开拓记录");
			}
			
		} catch (Exception e) {			
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("获取合作商开拓记录失败");
		}
		return json;
	}
	
	/**
	 * 获取开拓合作商的详细资料
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "getPartnerDevelopDtl")
	@ResponseBody
	public AjaxJson getPartnerDevelopDtl(@RequestParam Integer id){
		AjaxJson json = new AjaxJson();
		PartnerDevelopDTO dto=null;
		try {
			dto = pdservice.getPartnerDevelopDtl(id);
			if(dto == null){
				json.setObj("");
				json.setSuccess(false);
				json.setStateCode("02");
				json.setMsg("找不到对应的合作商开拓详情");
			} 
			else{
				json.setObj(dto);
				json.setSuccess(true);
				json.setStateCode("00");
				json.setMsg("获取合作商开拓详情成功");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("获取合作商开拓详情失败");
		}
		return json;
	}
	
	/**
	 * 获取开拓合作商可签约的账号数量
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "getSignAmount")
	@ResponseBody
	public AjaxJson getSignAmount(){
		AjaxJson json = new AjaxJson();
		try {
			List<String> amountList=pdservice.getSignAmount();
			if(amountList == null){
				json.setObj("");
				json.setSuccess(false);
				json.setStateCode("02");
				json.setMsg("找不到对应的合作商可签约账号数量");
			} 
			else{
				json.setObj(amountList);
				json.setSuccess(true);
				json.setStateCode("00");
				json.setMsg("获取合作商可签约账号数量");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setStateCode("01");
			json.setMsg("获取合作商可签约账号数量失败");
		}
		return json;
	}
}
