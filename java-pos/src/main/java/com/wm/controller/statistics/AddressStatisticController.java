package com.wm.controller.statistics;

import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.service.address.AddressServiceI;
import com.wm.util.PageList;

@Controller
@RequestMapping("/statistic/address")
public class AddressStatisticController extends BaseController {

	@Autowired
	private AddressServiceI addressService;
	
	@RequestMapping(value="/list.do", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> list(
			@RequestParam(required=false, defaultValue="1") Integer pageIndex,
			@RequestParam(required=false, defaultValue="15") Integer pageSize,
			String startDate, String endDate
			) {
		Map<String, Object> map = new HashMap<String, Object>();
		PageList page = addressService.queryStatistic(pageIndex, pageSize, startDate, endDate);
		
		map.put("state", "success");
		map.put("page", page);
		return map;
	}
	
	@RequestMapping(value="/orderlist.do", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> orderListByBuilding(
			@RequestParam Integer buildingId,
			@RequestParam(required=false, defaultValue="1") Integer pageIndex,
			@RequestParam(required=false, defaultValue="15") Integer pageSize,
			String startDate, String endDate
			) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		PageList page = addressService.queryStatisticByBuildingId(buildingId, pageIndex, pageSize, startDate, endDate);
		map.put("state", "success");
		map.put("page", page);
		return map;
	}
}
