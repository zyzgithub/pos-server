package com.wm.controller.job;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.controller.open_api.OpenResult.State;
import com.wm.service.menu.MenuServiceI;
import com.wm.util.SafeUtil;

@Controller
@RequestMapping("/jobController/menu")
public class InitRepJobController {
	
	private final static Logger logger = LoggerFactory.getLogger(InitRepJobController.class);

	@Autowired
	private MenuServiceI menuService;

	/**
	 * 初始化库存
	 */
	@RequestMapping("/initRep")
	@ResponseBody
	public AjaxJson initRep(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		
		menuService.initRep();
		j.setSuccess(true);
		j.setMsg("初始化库存完成");
		return j;
	}

}
