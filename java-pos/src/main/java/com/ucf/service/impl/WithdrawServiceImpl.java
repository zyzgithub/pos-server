package com.ucf.service.impl;


import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.base.config.EnvConfig;
import com.ucf.common.WithdrawParam;
import com.ucf.sdk.CoderException;
import com.ucf.sdk.UcfForOnline;
import com.ucf.service.WithdrawService;
import com.wm.util.HttpUtils;

/**
 * 先锋-代付接口
 * @author Simon
 */
@Service("ucfWithdrawalService")
@Transactional
public class WithdrawServiceImpl extends CommonServiceImpl implements WithdrawService {
	
	private static final Logger logger = LoggerFactory.getLogger(WithdrawServiceImpl.class);

	public JSONObject withdraw(WithdrawParam param) {
		try {
			Map<String, String> mapParam = parseToMap(param);
			String signValue = UcfForOnline.createSign(EnvConfig.ucf.merRSAKey, "sign", mapParam, "RSA");
			logger.info("RsaCoder.encryptByPublicKey: {}", signValue);
			mapParam.put("sign", signValue);
			JSONObject jsonParam = parseToJsonObj(mapParam);
			JSONObject result = HttpUtils.postForm(EnvConfig.ucf.gateway, jsonParam);
			logger.info("ucf witdraw result:{}", result);
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return error("提现失败");
	}

	public static Map<String, String> parseToMap(Object object) throws IllegalArgumentException, IllegalAccessException {
		Map<String, String> map = new HashMap<String, String>();
		Class<? extends Object> c = object.getClass();
		getClassFieldMap(object, map, c);
		Class<?> superClass = c.getSuperclass();
		logger.info("superClass:{}", superClass.getName());
		if(null != superClass || !Object.class.getName().equals(superClass.getName())){
			getClassFieldMap(object, map, superClass);
		}
		return map;
	}

	/**
	 * @param object
	 * @param map
	 * @param c
	 * @throws IllegalAccessException
	 */
	private static void getClassFieldMap(Object object, Map<String, String> map, Class<? extends Object> c) throws IllegalAccessException {
		Field[] fields = c.getDeclaredFields();
		if(null != fields && fields.length > 0){
			for(Field field : fields){
				field.setAccessible(true);
				String fieldName = field.getName();
				if("serialVersionUID".equals(fieldName)){
					continue;
				}
				Object fieldObjValue = field.get(object);
				if(null == fieldObjValue){
					continue;
				}
				map.put(fieldName, field.get(object).toString());
				logger.info("fieldName:{}, fieldValue:{}", fieldName, fieldObjValue);
			}
		}
	}
	
	public static JSONObject parseToJsonObj(Map<String, String> map) {
		List<String> keys = new ArrayList<String>(map.keySet());
		JSONObject obj = new JSONObject();
		for (String key : keys) {
			String value = map.get(key);
			obj.put(key, value);
		}
		return obj;
	}
	
	/*public static JSONObject parseToJsonObj(Object object) throws IllegalArgumentException, IllegalAccessException {
		JSONObject jsonObject = new JSONObject();
		Field[] fields = object.getClass().getDeclaredFields();
		if(null != fields && fields.length > 0){
			for(Field field : fields){
				field.setAccessible(true);
				String fieldName = field.getName();
				if("serialVersionUID".equals(fieldName)){
					continue;
				}
				Object fieldObjValue = field.get(object);
				if(null == fieldObjValue){
					continue;
				}
				jsonObject.put(fieldName, field.get(object).toString());
				logger.info("fieldName:{}, fieldValue:{}", fieldName, fieldObjValue);
			}
		}
		return jsonObject;
	}*/
	
	private JSONObject error(String msg) {
		JSONObject ajaxJson = new JSONObject();
		ajaxJson.put("success", false);
		ajaxJson.put("status", "01");
		ajaxJson.put("msg", msg);
		return ajaxJson;
	}
	
	public static void main(String[] args) {
		WithdrawParam param = new WithdrawParam();
		try {
			WithdrawServiceImpl.parseToMap(param);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
