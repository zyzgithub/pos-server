package com.wm.controller.statistics;
import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wm.service.statistics.CourierStatisticsDalyServiceI;

/**   
 * @Title: Controller
 * @Description: 统计
 * @author wuyong
 * @date 2015-08-29 15:16:45
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/statisticsController")
public class StatisticsController extends BaseController {

	@Autowired
	private CourierStatisticsDalyServiceI courierStatisticsDalyService;
	
	
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
