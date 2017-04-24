package com.courier_mana.personal.controller;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.personal.service.WithDrawService;



@Controller
@RequestMapping("ci/courier/admin/withDraw")
public class WithDrawController extends BasicController {
	
	private static Logger logger = LoggerFactory.getLogger(WithDrawController.class);
	
	@Autowired
	private WithDrawService withDrawService;
	
	
	/**
	 *获得快递员的提成列表
	 *deduct_type=0 送餐提成 deduct_type=1管理提成 deduct_type=2 网点xxx提成
	 * @param courierId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/getCourierStatistics")
	@ResponseBody
	public AjaxJson getCourierStatistics(@RequestParam Integer courierId,@RequestParam(required=false,value="page") Integer page,@RequestParam(required=false,value="page") Integer rows){
		AjaxJson ajaxJson = null;
		logger.info("invoke method getCourierStatistics,parames:{}{}{}",new Object[]{courierId,page,rows});
		//参数检查
		if(page==null&&rows==null){
			page=1;
			rows=5;
		}
		try {
			if(courierId == null&&page==null&&rows==null){
				ajaxJson = FAIL("01","参数错误");
			}else {
				ajaxJson = SUCCESS(withDrawService.getCourierStatistics(courierId, page, rows));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02","获取快递员提成列表失败");
		}
		logger.info("return result:{}",ajaxJson);
		return ajaxJson;
	}
	
	/**
	 * 跳转到提现页面
	 * @param userId
	 * @return
	 */
	@RequestMapping("/toWithDraw")
	@ResponseBody
	public AjaxJson toWithDraw(@RequestParam Integer userId){
		AjaxJson ajaxJson = null;
		logger.info("invoke method toWithDraw,params:{}",userId);
		//参数检查
		
		try {
			if(userId==null){
				ajaxJson = FAIL("01","参数错误");
			}else {
				
				ajaxJson  = SUCCESS(withDrawService.getCourierCardId(userId));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson = FAIL("02","获取快递员银行卡信息失败");
		}
		logger.info("return result:{}",JSON.toJSON(ajaxJson));
		return ajaxJson;
	}
	
	
	/**
	 * @param courierId
	 * @param bankCardId
	 * @param getMoney
	 * @param password
	 * @return
	 */
	@RequestMapping("/applyFor")
	@ResponseBody
	public AjaxJson applyFor(@RequestParam Integer courierId, @RequestParam Long bankCardId,@RequestParam Double getMoney,@RequestParam String password){
		AjaxJson ajaxJson = null;
		logger.info("invoke method applyFor ,params:{},{},{},{}",courierId,bankCardId,getMoney,password);
		
		//参数检查
		try {
			if(courierId==null&&bankCardId==null&&getMoney==null&&password==null){
				ajaxJson=FAIL("01","参数错误");
			}else{
				ajaxJson = SUCCESS(withDrawService.applyFor(courierId, bankCardId, getMoney, password));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson= FAIL("02",e.getMessage());
		}
		logger.info("return result:{}",JSON.toJSON(ajaxJson));
		return ajaxJson;
	}
	
	
	@RequestMapping("/getWithRecord")
	@ResponseBody
	public AjaxJson getWithRecord(@RequestParam Integer courierId,@RequestParam(required=false,value="page") Integer page,@RequestParam(required=false,value="rows") Integer rows){
		AjaxJson ajaxJson = null;
		logger.info("invoke method applyFor ,params:{}",courierId);
		
		//参数检查
		try {
			if(courierId==null){
				ajaxJson=FAIL("01","参数错误");
			}else{
				ajaxJson = SUCCESS(withDrawService.getWithRecord(courierId,page,rows));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxJson= FAIL("02","查询失败");
		}
		logger.info("return result:{}",JSON.toJSON(ajaxJson));
		return ajaxJson;
	}
	
}
