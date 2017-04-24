package com.wm.controller.orderstate;
import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wm.service.orderstate.OrderStateServiceI;

/**   
 * @Title: Controller
 * @Description: 订单状态
 * @author wuyong
 * @date 2015-02-02 16:14:30
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/orderStateController")
public class OrderStateController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(OrderStateController.class);

	@Autowired
	private OrderStateServiceI orderStateService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	
}
