package com.courier_mana.personal.controller;

import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.courier_mana.common.contoller.BasicController;
import com.courier_mana.personal.service.CourierNoticeService;


@Controller
@RequestMapping("/ci/courier/notice")
public class CourierNoticeController extends BasicController{

	private final static Logger logger = LoggerFactory.getLogger(CourierNoticeController.class);
	
	@Autowired
	private CourierNoticeService courierNoticeService;
	
	/**
	 * 获取系统消息列表
	 * @return
	 */
	@RequestMapping("/getNoticeList")
	@ResponseBody
	public AjaxJson getNoticeList(Integer page, Integer rows){
		if(page==null){
			page = 1;
			rows = 10;
		}
		try {
			List<Map<String,Object>> noticeList = courierNoticeService.getNoticeList(page, rows);
			logger.info("return result:{}", JSON.toJSONString(noticeList));
			return SUCCESS(noticeList);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return FAIL("01", "获取系统消息失败");
		}
	}
	
}
