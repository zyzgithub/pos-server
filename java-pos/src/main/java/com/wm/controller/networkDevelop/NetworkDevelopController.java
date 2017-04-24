package com.wm.controller.networkDevelop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
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
import com.wm.controller.networkDevelop.dto.CommunityInfoDTO;
import com.wm.controller.networkDevelop.dto.CompetitionAnalysisDTO;
import com.wm.controller.networkDevelop.dto.MerchantInfoDTO;
import com.wm.controller.networkDevelop.dto.ShopDtlDTO;
import com.wm.service.category.CategoryServiceI;
import com.wm.service.networkDevelop.NetworkDevelopServiceI;

@Controller
@RequestMapping("ci/networkDevelopController")
public class NetworkDevelopController {
	private static final Logger logger = Logger.getLogger(NetworkDevelopController.class);
	
	@Autowired
	private NetworkDevelopServiceI ndService;
	
	@Autowired
	private CategoryServiceI categoryService;
	
	/**
	 * 保存社区资料
	 * @param merchant
	 * @return
	 */
	@RequestMapping(params = "saveCommunityInfo")
	@ResponseBody
	public AjaxJson saveCommunityInfo(@Valid CommunityInfoDTO dto, Errors errors){
		AjaxJson json = new AjaxJson();
		logger.info("传入的 参数: " + JSON.toJSONString(dto));
		if(errors.hasErrors()){
			List<FieldError> errorsList = errors.getFieldErrors();
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			for(int i = 0; i < errorsList.size(); i++){
				errorsMap.put(errorsList.get(i).getField(), "错误值： " + errorsList.get(i).getRejectedValue());
			}
			logger.warn("参数错误, 参数: " + JSON.toJSONString(dto));
			json.setSuccess(false);			
			json.setObj(errorsMap);
			json.setMsg("传入了无效的值");
			logger.info("参数错误  return:" + JSON.toJSONString(json));
			return json;
		}
		return ndService.saveCommunityInfo(dto);		
	}
	
	/**
	 * 保存竞争分析
	 * @param merchant
	 * @return
	 */
	@RequestMapping(params = "saveCompetitionAnalysis")
	@ResponseBody
	public AjaxJson save(@Valid CompetitionAnalysisDTO dto, Errors errors){
		AjaxJson json = new AjaxJson();
		logger.info("传入的 参数: " + JSON.toJSONString(dto));
		if(errors.hasErrors()){
			List<FieldError> errorsList = errors.getFieldErrors();
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			for(int i = 0; i < errorsList.size(); i++){
				errorsMap.put(errorsList.get(i).getField(), "错误值： " + errorsList.get(i).getRejectedValue());
			}
			logger.warn("参数错误, 参数: " + JSON.toJSONString(dto));
			json.setSuccess(false);			
			json.setObj(errorsMap);
			json.setMsg("传入了无效的值");
			logger.info("参数错误  return:" + JSON.toJSONString(json));
			return json;
		}
		return ndService.saveCompetitionAnalysis(dto);		
	}
	
	/**
	 * 保存商家情况
	 * @param merchant
	 * @return
	 */
	@RequestMapping(params = "saveMerchantInfo")
	@ResponseBody
	public AjaxJson save(@Valid MerchantInfoDTO dto, Errors errors){
		AjaxJson json = new AjaxJson();
		logger.info("传入的 参数: " + JSON.toJSONString(dto));
		if(errors.hasErrors()){
			List<FieldError> errorsList = errors.getFieldErrors();
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			for(int i = 0; i < errorsList.size(); i++){
				errorsMap.put(errorsList.get(i).getField(), "错误值： " + errorsList.get(i).getRejectedValue());
			}
			logger.warn("参数错误, 参数: " + JSON.toJSONString(dto));
			json.setSuccess(false);			
			json.setObj(errorsMap);
			json.setMsg("传入了无效的值");
			logger.info("参数错误  return:" + JSON.toJSONString(json));
			return json;
		}
		return ndService.saveMerchantInfo(dto);		
	}
	
	/**
	 * 保存店铺详情
	 * @param merchant
	 * @return
	 */
	@RequestMapping(params = "saveShopDtl")
	@ResponseBody
	public AjaxJson saveShopDtl(@Valid ShopDtlDTO dto, Errors errors){
		AjaxJson json = new AjaxJson();
		logger.info("传入的 参数: " + JSON.toJSONString(dto));
		if(errors.hasErrors()){
			List<FieldError> errorsList = errors.getFieldErrors();
			Map<String, Object> errorsMap = new HashMap<String, Object>();
			for(int i = 0; i < errorsList.size(); i++){
				errorsMap.put(errorsList.get(i).getField(), "错误值： " + errorsList.get(i).getRejectedValue());
			}
			logger.warn("参数错误, 参数: " + JSON.toJSONString(dto));
			json.setSuccess(false);			
			json.setObj(errorsMap);
			json.setMsg("传入了无效的值");
			logger.info("参数错误  return:" + JSON.toJSONString(json));
			return json;
		}
		return ndService.saveShopDtl(dto);		
	}
	
	/**
	 * 获取网点开拓详情
	 * @param merchant
	 * @return
	 */
	@RequestMapping(params = "getNetworkDevDtl")
	@ResponseBody
	public AjaxJson getNetworkDevDtl(@RequestParam Integer id){
		logger.info("传入的 参数: " + JSON.toJSONString(id));
		return ndService.getNetworkDevDtl(id);
	}
	
	/**
	 * 获取初始化店铺描述列表
	 * @param merchant
	 * @return
	 */
	@RequestMapping(params = "getShopList")
	@ResponseBody
	public AjaxJson getShopList(){
		logger.info("获取门店类型");
		AjaxJson json = new AjaxJson();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> shopTypeMap = new HashMap<String, Object>();
		try{
		List<Map<String, Object>> shopTypeList = categoryService.getCategoryGroup("communityShop");
		if(CollectionUtils.isEmpty(shopTypeList)){
			logger.warn("没有获取到门店类型");
			json.setObj(list);
			json.setMsg("找不到店铺描述列表");
			json.setSuccess(false);
			json.setStateCode("02");
		}
		for(Map<String, Object> map : shopTypeList){
			shopTypeMap.put("state", 0);
			shopTypeMap.put("type", map.get("id"));
			shopTypeMap.put("typeName", map.get("name"));
			list.add(shopTypeMap);
			shopTypeMap = new HashMap<String, Object>();
		}
		json.setObj(list);
		json.setSuccess(true);
		json.setStateCode("00");
		json.setMsg("获取初始化店铺描述列表成功");
	} catch (Exception e) {
		e.printStackTrace();
		json.setMsg("获取初始化店铺描述列表失败");
		json.setSuccess(false);
		json.setStateCode("01");
	}
	return json;
}
	
	@RequestMapping(params = "getNetworkRecord")
	@ResponseBody
	public AjaxJson getNetworkRecord(@RequestParam Integer courierId, @RequestParam int page, @RequestParam int rows){
		AjaxJson json = new AjaxJson();
		logger.info("快递员" + courierId + "获取开拓网点历史记录");
		try {
			List<Map<String, Object>> list = ndService.getNetworkRecord(courierId, page, rows);
			if(CollectionUtils.isEmpty(list)){
				json.setMsg("没有更多的开拓网点的记录");
				json.setSuccess(false);
				json.setStateCode("02");
				return json;
			}
			json.setMsg("获取开拓网点历史记录成功");
			json.setObj(list);
			json.setSuccess(true);
			json.setStateCode("00");
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("获取开拓网点历史记录失败");
			json.setSuccess(true);
			json.setStateCode("01");
		}
		return json;
	}
	
}
