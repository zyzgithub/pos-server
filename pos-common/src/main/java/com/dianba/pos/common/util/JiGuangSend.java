package com.dianba.pos.common.util;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.alibaba.fastjson.JSONObject;


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
	public static void main(String[] args) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("type", JPushTypeEnum.SPEAK.getKey());
        jsonObject.put("msg",JPushTypeEnum.SPEAK.getMsg()+"20元");
	    sendPushWithAlias("100045",JPushTypeEnum.SPEAK.getTitle(),jsonObject.toJSONString());
		//System.out.println(jsonObject);
	}


}
