package com.dianba.pos.common.util;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;


public class JiGuangSend {
	
	public static String sendPushWithAlias(String alias,String content){
		PushResult result=null;
		JPushClient  jpushClient = new JPushClient(ConstantsUtil.YUN_POS_JIGUANG_SECRET,ConstantsUtil
				.YUN_POS_JIGUANG_KEY);

		PushPayload payload = PushExample.buildPushObject_all_alias_alert(alias,content);
				//PushResult result = jpushClient.sendAndroidMessageWithAlias("Test SMS", "rwerwe", "13660633666");
		try {
			result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
		System.out.println(result);

		return result.toString();
	}

	public static String sendPushWithAlias(String alias,String content,String message){
		PushResult result=null;
		JPushClient  jpushClient = new JPushClient(ConstantsUtil.YUN_POS_JIGUANG_SECRET,ConstantsUtil
				.YUN_POS_JIGUANG_KEY);
		PushPayload payload = PushExample.buildPushObject_all_alias_alert(alias,content,message);
		   try {
				//PushResult result = jpushClient.sendAndroidMessageWithAlias("Test SMS", "rwerwe", "13660633666");
		    result = jpushClient.sendPush(payload);
			System.out.println(result);

		} catch (APIConnectionException e) {

		} catch (APIRequestException e) {

		}
		   if(result==null)
			   return "null";
		   else
		   return result.toString();
	}

	public static String sendPushWithAliasAndSms(String alias,String message){
		PushResult result=null;
		JPushClient  jpushClient = new JPushClient(ConstantsUtil.YUN_POS_JIGUANG_SECRET,ConstantsUtil
				.YUN_POS_JIGUANG_KEY);
		PushPayload payload = PushExample.buildPushObject_all_alias_sms(alias,message);
		try {
			//PushResult result = jpushClient.sendAndroidMessageWithAlias("Test SMS", "rwerwe", "13660633666");
			result = jpushClient.sendPush(payload);
			System.out.println(result);

		} catch (APIConnectionException e) {

		} catch (APIRequestException e) {

		}
		if(result==null)
			return "null";
		else
			return result.toString();
	}
	public static String getPrettyNumber(String number) {
		return BigDecimal.valueOf(Double.parseDouble(number))
				.stripTrailingZeros().toPlainString();
	}

	public static void main(String[] args) {
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("type", JPushTypeEnum.SPEAK.getKey());
//		String mon=	getPrettyNumber("20.00");
//		System.out.println(mon);
//        jsonObject.put("msg",JPushTypeEnum.SPEAK.getMsg()+mon+"元");
//        for(int i=0;i<5;i++){
//			String aa=sendPushWithAliasAndSms("104",jsonObject.toJSONString());
//			String result=sendPushWithAliasAndSms("100045",jsonObject.toJSONString());
//		}
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("type", JPushTypeEnum.BLACKLIST.getKey());
		jsonObject.put("msg",JPushTypeEnum.BLACKLIST.getMsg());
		sendPushWithAlias("100045",JPushTypeEnum.BLACKLIST.getTitle(),jsonObject.toJSONString());
//		if(result!=null){
//			JSONObject object= JSON.parseObject(result);
//			int code=object.getIntValue("statusCode");
//			if(code==0){
//				System.out.println("-----------------------------");
//			}
//		}
		//System.out.println(jsonObject);
	}


}
