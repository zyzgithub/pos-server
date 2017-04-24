package com.wm.controller.job;
import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wm.controller.open_api.OpenResult.State;
import com.wm.util.SafeUtil;
import com.wp.AccessTokenContext;

@Controller
@RequestMapping("/jobController/sys")
public class SysJobController {
	
	private final static Logger logger = LoggerFactory.getLogger(SysJobController.class);

	@Autowired
	private SystemService systemService;

	/**
	 * 清除历史数据
	 */
	@RequestMapping("/clearLog")
	@ResponseBody
	public AjaxJson clearLog(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		try {
			systemService.clearLogPerWeek();
			j.setSuccess(true);
			j.setMsg("清除历史数据成功");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
			logger.error("清除历史数据失败！", e);
		}
		return j;
	}
	
	/**
	 * 重新获取微信公众号ACCESS_TOKEN
	 */
	@RequestMapping("/refreshToken")
	@ResponseBody
	public AjaxJson refreshToken(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		try {
			logger.info("定时任务-重新获取微信公众号ACCESS_TOKEN");
			AccessTokenContext.setAccessToken();
			AccessTokenContext.setJsapiTicket();
			j.setSuccess(true);
			j.setMsg("重新获取微信公众号ACCESS_TOKEN成功");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
			logger.error("重新获取微信公众号ACCESS_TOKEN失败！", e);
		}
		return j;
	}
	
	/**
	 * 备份历史订单
	 */
	@RequestMapping("/backupOrderData")
	@ResponseBody
	public AjaxJson backupOrderData(String sign, String timestamp) {
		AjaxJson j = new AjaxJson();
		logger.info("sign:{}, timestamp:{}, " , sign, timestamp);
		State check = SafeUtil.checkSign(sign, timestamp);
		if(null != check) {
			j.setSuccess(false);
			j.setMsg("不合法的访问，签名失败！" + check.ret().toString());
			logger.error("不合法的访问，签名失败！" + check.ret().toString());
			return j;
		}
		try {
			systemService.backupOrderData();
			j.setSuccess(true);
			j.setMsg("备份历史订单成功");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg(e.getMessage());
			logger.error("备份历史订单失败！", e);
		}
		return j;
	}

}
