package com.wm.controller.supermarket;

import com.alibaba.fastjson.JSON;
import com.base.VO.PageVO;
import com.base.constant.ErrorCode;
import com.base.exception.BusinessException;
import com.sms.service.SmsByBusinessServiceI;
import com.wm.controller.user.vo.CashierVo;
import com.wm.service.impl.user.CashierLoginLogServiceImpl.MerchantCashierLoginLog;
import com.wm.service.user.CashLoginLogServiceI;
import com.wm.service.user.CashierServiceI;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("ci/cashierController")
public class CashierController {

	private final static Logger logger = LoggerFactory.getLogger(CashierController.class);

	@Autowired
	private CashierServiceI cashierService;

	@Autowired
	private SmsByBusinessServiceI smsForCashierLoginService;

	@Autowired
	private CashLoginLogServiceI cashierLoginLogService;


	@RequestMapping(params = "login")
	@ResponseBody
	public AjaxJson login(@RequestParam String mobile,
			@RequestParam String password){
		logger.info("超市收银员登录, mobile:{}, password:{}", mobile, password);
		AjaxJson ajaxJson = null;
		try {
			Map<String, Object> result = cashierService.login(mobile, password);
			boolean success = (Boolean)result.get("success");
			if(success){
				ajaxJson = AjaxJson.successJson("登录成功");
				ajaxJson.setObj(result.get("cashierInfo"));
			}
			else {
				ajaxJson = AjaxJson.failJson(result.get("msg").toString());
			}
		}
		catch (BusinessException e) {
			ajaxJson = AjaxJson.failJson(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = AjaxJson.failJson("登录失败");
		}
		logger.info("超市收银员登录,return:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}


	@RequestMapping(params = "exitLogin")
	@ResponseBody
	public AjaxJson exitLogin(@RequestParam Integer cashierId){
		logger.info("超市收银员退出登录,cashierId:{}", cashierId);
		AjaxJson ajaxJson = null;
		try {
			Map<String, Object> result = cashierService.exitLogin(cashierId);
			boolean success = (Boolean)result.get("success");
			if(success){
				ajaxJson = AjaxJson.successJson("退出登录成功");
				ajaxJson.setObj(result.get("cashierInfo"));
			}
			else {
				ajaxJson = AjaxJson.failJson(result.get("msg").toString());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = AjaxJson.failJson("退出登录失败");
		}
		logger.info("超市收银员退出登录,return:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}

	@RequestMapping(params = "loginByVerfiyCode")
	@ResponseBody
	public AjaxJson loginByVerfiyCode(@RequestParam Integer merchantId, @RequestParam String mobile,
			@RequestParam String verifyCode){
		logger.info("超市收银员登录,merchantId:{}, mobile:{}, verifyCode:{}", merchantId, mobile, verifyCode);
		AjaxJson ajaxJson = null;
		try {
			Map<String, Object> result = cashierService.loginByVerifyCode(merchantId, mobile, verifyCode);
			boolean success = (Boolean)result.get("success");
			if(success){
				ajaxJson = AjaxJson.successJson("登录成功");
				ajaxJson.setObj(result.get("cashierInfo"));
			}
			else {
				ajaxJson = AjaxJson.successJson(result.get("msg").toString());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = AjaxJson.successJson("登录失败");
		}
		logger.info("超市收银员登录,return:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}

	@RequestMapping(params = "getVerifyCodeForLogin")
	@ResponseBody
	public AjaxJson getVerifyCodeForLogin(@RequestParam String mobile){
		try{
			return smsForCashierLoginService.sendVerifyCode(mobile);
		}
		catch (Exception e) {
			e.printStackTrace();
			return AjaxJson.failJson("发送短信验证码失败");
		}
	}

	@RequestMapping(params = "saveOrUpdateCashier")
	@ResponseBody
	public AjaxJson saveOrUpdateCashier(@Valid CashierVo vo, Errors errors){
		logger.info("添加收银员,params:{}", JSON.toJSONString(vo));
		AjaxJson ajaxJson = null;
		try{
			if(errors.hasErrors()){
				List<FieldError> errorsList = errors.getFieldErrors();
				Map<String, Object> errorsMap = new HashMap<String, Object>();
				for(int i = 0; i < errorsList.size(); i++){
					errorsMap.put(errorsList.get(i).getField(), "错误值： " + errorsList.get(i).getRejectedValue());
				}
				logger.warn("参数错误, 参数: " + JSON.toJSONString(errorsMap));
				ajaxJson = AjaxJson.failJson("参数错误");
				return ajaxJson;
			}

			cashierService.saveOrUpdate(vo);
			ajaxJson = AjaxJson.successJson("添加或修改收银员成功");
		}
		catch(BusinessException e){
			ajaxJson = AjaxJson.failJson(e.getMessage());
//			ajaxJson.setStateCode(String.valueOf(e.getErrCode()));
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = AjaxJson.failJson("添加或修改收银员失败");
		}
		logger.info("添加收银员员,return:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}

	@RequestMapping(params = "getCashiers")
	@ResponseBody
	public AjaxJson getCashiers(@RequestParam Integer merchantId,
			@RequestParam(defaultValue="1") Integer pageNo,
			@RequestParam(defaultValue="10") Integer pageSize){
		logger.info("添加收银员列表,merchantId:{}, pageNo:{}, pageSize:{}", merchantId, pageNo, pageSize);
		AjaxJson ajaxJson = null;
		try{
			PageVO<Map<String, Object>> page = cashierService.getCashiersByPage(merchantId, pageNo, pageSize);
			ajaxJson = AjaxJson.successJson("获取收银员列表成功");
			ajaxJson.setObj(page);
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = AjaxJson.failJson("获取收银员列表失败");
		}
		logger.info("获取收银员列表,return:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}


	@RequestMapping(params = "getCashierInfo")
	@ResponseBody
	public AjaxJson getCashierInfo(@RequestParam Integer cashierId){
		logger.info("获取收银员信息,cashieId:{}", cashierId);
		AjaxJson ajaxJson = null;
		try{
			CashierVo vo = cashierService.get(cashierId);
			if(vo != null){
				ajaxJson = AjaxJson.successJson("获取收银员信息成功");
				ajaxJson.setObj(vo);
			}
			else {
				ajaxJson = AjaxJson.failJson("获取收银员信息失败");
				ajaxJson.setStateCode(String.valueOf(ErrorCode.INVALID_ARGUMENT));
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = AjaxJson.failJson("获取收银员信息失败");
		}
		logger.info("获取收银员信息,return:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}

	@RequestMapping(params = "deleteCashier")
	@ResponseBody
	public AjaxJson deleteCashier(@RequestParam Integer cashierId){
		logger.info("删除收银员,cashieId:{}", cashierId);
		AjaxJson ajaxJson = null;
		try{
			boolean success = cashierService.delete(cashierId);
			if(success){
				ajaxJson = AjaxJson.successJson("删除收银员成功");
			}
			else {
				ajaxJson = AjaxJson.failJson("删除收银员失败");
				ajaxJson.setStateCode(String.valueOf(ErrorCode.INVALID_ARGUMENT));
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = AjaxJson.failJson("获取收银员信息失败");
		}
		logger.info("删除收银员,return:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}

	@RequestMapping(params = "getCashierLoginLogs")
	@ResponseBody
	public AjaxJson getCashierLoginLogs(@RequestParam Integer merchantId,
			@RequestParam(defaultValue="0") Integer pageNo,
			@RequestParam(defaultValue="10") Integer pageSize){
		logger.info("获取收银员登录日志,merchantId:{}", merchantId);
		AjaxJson ajaxJson = null;
		try{
			PageVO<MerchantCashierLoginLog> page = cashierLoginLogService.getLoginLogs(merchantId, pageNo, pageSize);
			ajaxJson = AjaxJson.successJson("获取收银员登录日志成功");
			ajaxJson.setObj(page);
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = AjaxJson.failJson("获取收银员登录日志失败");
		}
		logger.info("获取收银员登录日志,return:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}

	@RequestMapping(params = "newLogin")
	@ResponseBody
	public AjaxJson newLogin(@RequestParam String mobile,
			@RequestParam String password, String deviceCode, @RequestParam(defaultValue = "1") int posEdition){
		logger.info("超市收银员登录, mobile:{}, password:{}, deviceCode:{}, posEdition:{}", mobile, password, deviceCode, posEdition);
		AjaxJson ajaxJson = null;
		try {
			Map<String, Object> result = cashierService.newLogin(mobile, password, deviceCode, posEdition);
			boolean success = (Boolean)result.get("success");
			if(success){
				ajaxJson = AjaxJson.successJson("登录成功");
				ajaxJson.setObj(result.get("cashierInfo"));
			}
			else {
				ajaxJson = AjaxJson.failJson(result.get("msg").toString());
			}
		}
		catch (BusinessException e) {
			ajaxJson = AjaxJson.failJson(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			ajaxJson = AjaxJson.failJson("登录失败");
		}
		logger.info("超市收银员登录,return:{}", JSON.toJSONString(ajaxJson));
		return ajaxJson;
	}

}
