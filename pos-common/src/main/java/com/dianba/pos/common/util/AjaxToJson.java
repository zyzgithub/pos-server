package com.dianba.pos.common.util;

import com.alibaba.fastjson.JSONObject;






public class AjaxToJson {

	/**
	 * 普通json数据传输
	 * @param
	 * @param msg
	 * @param code
	 * @param response
	 */
	public static String jsonToStr(String msg,Integer code,Object response){


		JSONObject jo=new JSONObject();
		
		jo.put("msg", msg);
		
		jo.put("code",code);
		
		jo.put("response", response);

		return jo.toString();
		
	}
	/**
	 * 普通json数据传输
	 * @param
	 * @param msg
	 * @param code
	 * @param response
	 */
	public static String jsonToList(String msg,Integer code,Object response){


		JSONObject jo=new JSONObject();

		jo.put("msg", msg);

		jo.put("code",code);

		JSONObject datas=new JSONObject();

		datas.put("datas",response);

		jo.put("response", datas);
		return jo.toString();

	}

}
