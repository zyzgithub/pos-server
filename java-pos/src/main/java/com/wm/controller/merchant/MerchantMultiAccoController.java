package com.wm.controller.merchant;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ListGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.wm.dto.merchant.MerchantCashflowDto;
import com.wm.entity.merchant.MerchantCashflowEntity;
import com.wm.service.merchant.MerchantMultiAccoServiceI;
import com.wm.util.HttpUtils;
import com.wm.util.spring.json.JsonParam;

@Controller
@RequestMapping("/merchantMultiAccoController")
public class MerchantMultiAccoController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MerchantMultiAccoServiceI merchantMultiAccoServiceI;

	/**
	 * 获取总店和分店列表
	 * @param merchantId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params="getALLStores")
	public AjaxJson getALLStores(Integer merchantId, @RequestParam(value="page",defaultValue="1") Integer page, 
			@RequestParam(value="rows",defaultValue="10") Integer rows){
		ListGrid grid = new ListGrid();
		List<Map<String, Object>> allstores = merchantMultiAccoServiceI.getALLStoresPageList(merchantId, page, rows);
		Long listSize = merchantMultiAccoServiceI.getBranchStoresCount(merchantId); //分店总数
		grid.setList(allstores);
		grid.setListSize(listSize);
		AjaxJson json = new AjaxJson(true, "操作成功", grid, "00");
		return json;
	}
	
	/**
	 * 获取分店列表
	 * @param merchantId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params="getBranchStores")
	public AjaxJson getBranchStores(Integer merchantId, @RequestParam(value="page",defaultValue="1") Integer page, 
			@RequestParam(value="rows",defaultValue="10") Integer rows){
		List<Map<String, Object>> branchstores = merchantMultiAccoServiceI.getBranchStoresPageList(merchantId, page, rows);
		AjaxJson json = new AjaxJson(true, "操作成功", branchstores, "00");
		return json;
	}
	
	/**
	 * 获取商家总账记录
	 * @param merchantId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params="getALLStoresAccount")
	public AjaxJson getALLStoresAccount(Integer merchantId, @RequestParam(value="page",defaultValue="1") Integer page, 
			@RequestParam(value="rows",defaultValue="10") Integer rows){
		List<Map<String, Object>> allstoresAccount = merchantMultiAccoServiceI.getALLStoresAccountPageList(merchantId, page, rows);
		AjaxJson json = new AjaxJson(true, "操作成功", allstoresAccount, "00");
		return json;
	}
	
	/**
	 * 获取商家账户转入记录
	 * @param merchantId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params="getMerchantCashflow")
	public AjaxJson getMerchantCashflow(Integer merchantId, @RequestParam(value="page",defaultValue="1") Integer page, 
			@RequestParam(value="rows",defaultValue="10") Integer rows){
		List<Map<String, Object>> merchantCashflowList = merchantMultiAccoServiceI.getMerchantCashflowPageList(merchantId, page, rows);
		AjaxJson json = new AjaxJson(true, "操作成功", merchantCashflowList, "00");
		return json;
	}
	
	/**
	 * 分店余额转入到主店余额
	 * @param dto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params="addMerchantCashflow")
	public AjaxJson addMerchantCashflow(@JsonParam(keyname="datas", isResolveBody=true) MerchantCashflowDto dto){
		logger.info("分店余额转入到主店余额,主店ID="+dto.getMainStoreId());
		AjaxJson json = new AjaxJson(true, "操作成功", "00");
		try {
			merchantMultiAccoServiceI.addMerchantCashflow(dto);
		} catch (Exception e) {
			json = new AjaxJson(false, "分店余额不足，操作失败", "01");
		}
		return json;
	}
	
	/**
	 * 极光推送文本消息给商家
	 * @param userId
	 * @param title
	 * @param content
	 * @return
	 */
	@ResponseBody
	@RequestMapping(params="jpushMsgToMerchant")
	public AjaxJson jpushMsgToMerchant(Integer userId, String title, String content){
		merchantMultiAccoServiceI.jpushMsgToMerchant(userId, title, content);
		AjaxJson json = new AjaxJson(true, "操作成功", "00");
		return json;
	}
	
	/**
	 * 用于测试余额转入
	 * @param userId
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JsonProcessingException 
	 */
	@RequestMapping(params = "test")
	@ResponseBody
	public String test(String key) throws UnsupportedEncodingException, JsonProcessingException{
		String url = "http://localhost:8080/WM/merchantMultiAccoController.do?addMerchantCashflow";
		MerchantCashflowDto dto = new MerchantCashflowDto();
		
		MerchantCashflowEntity[] branchDetail = new MerchantCashflowEntity[2];
		//商家1
		MerchantCashflowEntity cash = new MerchantCashflowEntity();
		cash.setMoney(new BigDecimal("0.02"));
		cash.setOutMerchantId(3327);
		branchDetail[0] = cash;
		//商家2
		MerchantCashflowEntity cash2 = new MerchantCashflowEntity();
		cash2.setMoney(new BigDecimal("0.03"));
		cash2.setOutMerchantId(40);
		branchDetail[1] = cash2;
		
		dto.setBranchDetail(branchDetail);
		dto.setTotalMoney(new BigDecimal("0.05"));
		dto.setMainStoreId(3318);
		
		//Map<String, Object> map = new HashMap<String, Object>();
		//map.put("sessionkey", key);
		//map.put("datas", JacksonUtil.writeValueAsString(dto));
		//map.put("datas", dto);
		
		String result = HttpUtils.postBody(url, JSON.parseObject(JSON.toJSONString(dto))).toJSONString();
		System.out.println(JSON.toJSON(result));
		return "hello";
	}
}
