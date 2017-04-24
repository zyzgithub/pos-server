package com.wm.controller.statistics;

import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.service.user.WUserServiceI;

@Controller
@RequestMapping("/statistic/courier")
public class CourierStatisticController extends BaseController{
	
	@Autowired
	private WUserServiceI wUserService;

	@RequestMapping(value="/rank.do", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> courierRank() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		//wUserService.getCourierRank(courierId, startDate, endDate, isRankByArea, start, num)
		return map;
	}
}
