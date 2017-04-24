package com.wm.controller.rebate;

import java.util.Date;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courier_mana.common.contoller.BasicController;
import com.wm.service.rebate.RebateServiceI;

@Controller
@RequestMapping("/rebate")
public class RebateController extends BasicController{
	
	@Autowired
	RebateServiceI rebateService;
	
	/**
	 * 统计商家返点奖励
	 */
	@RequestMapping("/statMerchantRebate")
	@ResponseBody
	public AjaxJson statMerchantRebateByEveryday(){
		rebateService.statMerchantRebateByEveryday();
		return SUCCESS();
	}
	
	/**
	 * 统计商家返点奖励(一段时间内)
	 * 测试机：http://apptest.0085.com/rebate/statMerchantRebateByBefore.do?merchant_Id=XX
	 */
	@RequestMapping("/statMerchantRebateByBefore")
	@ResponseBody
	public AjaxJson statMerchantRebateByBefore(@DateTimeFormat(pattern="yyyy-MM-dd") Date beginDate, @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate, Integer merchant_Id){
		rebateService.statMerchantRebateByBefore(beginDate, endDate, merchant_Id);
		return SUCCESS();
	}
	
	/**
	 * 统计快递员返点奖励
	 * http://localhost:8080/WM/rebate/statCourierRebate.do
	 */
	@RequestMapping("/statCourierRebate")
	@ResponseBody
	public AjaxJson statCourierRebateByEveryday(){
		rebateService.statCourierRebateByEveryday();
		return SUCCESS();
	}
	
	/**
	 * 统计快递员返点奖励(一段时间内)
	 * http://localhost:8080/WM/rebate/statCourierRebateByBefore.do?beginDate=2015-12-01&endDate=2016-02-02
	 */
	@RequestMapping("/statCourierRebateByBefore")
	@ResponseBody
	public AjaxJson statCourierRebateByBefore(@DateTimeFormat(pattern="yyyy-MM-dd") Date beginDate, @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate){
		rebateService.statCourierRebateByBefore(beginDate, endDate);
		return SUCCESS();
	}
	
	/**
	 * 发放商家奖励
	 * http://apptest.0085.com/rebate/payMerchantRebate.do?date=2016-02-01&merchantId=XX
	 */
	@RequestMapping("/payMerchantRebate")
	@ResponseBody
	public AjaxJson payMerchantRebate(@DateTimeFormat(pattern="yyyy-MM-dd") Date date, Integer merchantId){
		rebateService.payMerchantRebate(date, merchantId);
		return SUCCESS();
	}
	
	/**
	 * 发放快递员奖励
	 */
	/*@RequestMapping("/payCourierRebate")
	@ResponseBody
	public AjaxJson payCourierRebate(){
		rebateService.payCourierRebate();
		return SUCCESS();
	}*/
	
	/**
	 * 手工发放商家奖励接口
	 */
	@RequestMapping("/manualGrant")
	@ResponseBody
	public AjaxJson manualGrant(Integer grantId, String key){
		if(!"cb33d3e817fbcd24d7e76a8e596c6bfb".equals(key)){
			return FAIL("没有访问权限");
		}
		return rebateService.manualGrant(grantId);
	}
	
}
