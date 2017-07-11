package com.dianba.pos.common.util;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

public class JiGuangSend {
	
	public static PushResult sendPushWithAlias(String alias,String content){
		PushResult result=null;
		JPushClient  jpushClient = new JPushClient(ConstantsUtil.YUN_POS_JIGUANG_SERCRET,ConstantsUtil
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

		return result;
	}

	public static String sendPushWithAlias(String alias,String content,String message){
		PushResult result=null;
		JPushClient  jpushClient = new JPushClient(ConstantsUtil.YUN_POS_JIGUANG_SERCRET,ConstantsUtil
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


	public static void main(String[] args) {
		sendPushWithAlias("100348","标题","推送测试海龙");
	}


}
