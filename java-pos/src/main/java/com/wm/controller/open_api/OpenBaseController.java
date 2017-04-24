package com.wm.controller.open_api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wm.controller.open_api.OpenResult.State;
import com.wm.controller.open_api.tswj.PortConfig;
import com.wm.entity.user.WUserEntity;
import com.wm.service.user.WUserServiceI;
import com.wm.util.StringUtil;
import com.wm.util.security.OpenSign;

public class OpenBaseController extends BaseController {
	public static Logger logger = Logger.getLogger(OpenBaseController.class);
	
	@Autowired
	protected WUserServiceI wUserService;
	
	protected String getBashPath(HttpServletRequest request) {
		return request.getScheme()+"://"+request.getServerName()+(request.getServerPort()==80?"":":"+request.getServerPort())+request.getContextPath();
		
	}
	
	/**
	 * 生成签名后的字符串
	 * @param token
	 * @param timestamp
	 * @param params 请求参数 不包含timestamp与token
	 * @return
	 */
	protected String getSign(String token, Long timestamp, Map<String, Object> params){
		//组装参数
		if(null != timestamp) params.put("timestamp", timestamp);
		params.put("token", token);
		params.put("appKey", PortConfig.SYSTEM_VALIDE_KEY);

		//生成签名
		return OpenSign.sign(params);
	}
	
	/**
	 * 获取解密后的用户数据 json格式
	 * @param token
	 * @param timestamp
	 * @param sign
	 * @param params 请求参数 不包含timestamp与token, 为null时自动转为new HashMap
	 * @return 返回类型为OpenResult表示有错误返回，String格式正确解析
	 */
	protected Object getDesToken(String token, Long timestamp, String sign, Map<String, Object> params){
		if(null == params) params = new HashMap<String, Object>();
		//校验传入参数
		if(StringUtil.isEmpty(token, sign)) return State.ParamError.ret();
		if(timestamp < 1) return State.ParamError.ret(); 
		
		//校验签名
		String mySign = getSign(token, timestamp, params);
		if(!sign.equals(mySign)) return State.SignError.ret();
		
		//解密token
		String tokenStr = OpenSign.DecToken(token, PortConfig.TOKEN_KEY);
		if(StringUtil.isEmpty(tokenStr)) return State.ParamError.ret();
		//转成json
		JSONObject tokenJson =  JSON.parseObject(tokenStr, JSONObject.class);
		if(null == tokenJson) return State.TokenError.ret();
		//参数校验
		String openId = tokenJson.getString("openId");
		String appId = tokenJson.getString("appId");
		if(StringUtil.isEmpty(openId, appId)) return State.ParamError.ret();
		if(!appId.equals(PortConfig.TSWJ_APPID)) return State.ParamError.ret();
		
		//查询用户数据
		WUserEntity user = wUserService.findByOpenId(openId);
		if(null == user) return State.NotUser.ret();
		
		return user;
	}
}
