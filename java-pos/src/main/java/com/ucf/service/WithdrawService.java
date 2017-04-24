package com.ucf.service;

import java.security.GeneralSecurityException;

import org.jeecgframework.core.common.service.CommonService;

import com.alibaba.fastjson.JSONObject;
import com.ucf.common.WithdrawParam;
import com.ucf.sdk.CoderException;

public interface WithdrawService extends CommonService {

	/**
	 * 代付
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws GeneralSecurityException
	 * @throws CoderException
	 */
	public JSONObject withdraw(WithdrawParam param) ;
	
}
