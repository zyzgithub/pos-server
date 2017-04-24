package com.validate.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jeecg.system.service.SystemService;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.validate.util.RandomValidateUtil;

@Controller
@RequestMapping("ci/validateCodeController")
public class ValidateCodeController {
	
	private static final Logger logger = LoggerFactory.getLogger(ValidateCodeController.class);

	@Autowired
	private SystemService systemService;

	/**
	 * 获取图片验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "getImgValidateCode")
	@ResponseBody
	public void getImgValidateCode(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute(RandomValidateUtil.RANDOMCODEKEY);
		logger.info("sessionId:{}", request.getSession().getId());
		RandomValidateUtil randomValidateUtil = new RandomValidateUtil();
		randomValidateUtil.getRandcode(request, response);
	}

	/**
	 * 验证验证码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "validateCode")
	@ResponseBody
	public AjaxJson validateCode(HttpServletRequest request, HttpServletResponse response, String validateCodeStr) {
		AjaxJson j = new AjaxJson();
		j.setStateCode("01");
		j.setMsg("验证码已过期,请重新获取");
		j.setSuccess(false);

		String seesionValidateCode = null;
		HttpSession session = request.getSession();
		if (session != null) {
			if(validateCodeStr.length() > 11){
				validateCodeStr = validateCodeStr.substring(11, validateCodeStr.length());
			}
			seesionValidateCode = (String) session.getAttribute(RandomValidateUtil.RANDOMCODEKEY);
			if (seesionValidateCode == null) {
				logger.warn("validateCodeStr:{} is timeout", validateCodeStr);
			} else if (!validateCodeStr.toUpperCase().equals(seesionValidateCode)) {
				logger.warn("validateCodeStr:{} is invalid", validateCodeStr);
				j.setMsg("验证码错误");
			} else {
				j.setStateCode("00");
				j.setSuccess(true);
				j.setMsg("验证通过");
				session.removeAttribute(RandomValidateUtil.RANDOMCODEKEY);
			}
		}

		return j;
	}
	
	/**
	 * 验证验证码
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "validateCodeNew")
	@ResponseBody
	public AjaxJson validateCodeNew(HttpServletRequest request, HttpServletResponse response, String phone, String validateCodeStr) {
		logger.info("validateCodeNew phone:{}, validateCodeStr:{}", phone, validateCodeStr);
		return this.validateCode(request, response, validateCodeStr);
	}

	/**
	 * 清空session中的验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "cleanValidateCode")
	@ResponseBody
	public AjaxJson cleanValidateCode(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		request.getSession().removeAttribute(RandomValidateUtil.RANDOMCODEKEY);
		j.setSuccess(true);
		return j;
	}
}
