package com.dianba.pos.push.util;
import cn.jpush.api.push.model.*;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;


public class PushExample {
    /**
     *  所有平台，所有设备，内容为 【成佩涛发送过来的!】 的通知
     *
     *  */
    public static PushPayload buildPushObjectAllAlert(String name) {
        return PushPayload.alertAll(name);
    }

    /**
     /**
     *  所有平台，推送目标是别名为 "alias1"，通知内容为  【神马都是浮云!】
     *  apnsProduction true 线上环境 false 测试环境
     *
     *  */
    public static PushPayload buildPushAlertAndMsgByAlias(String alis,String content, String sms) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setNotification(
                        Notification
                                .newBuilder()
                                .addPlatformNotification(
                                        AndroidNotification.newBuilder()
                                                .setAlert(content).build())
                                .addPlatformNotification(IosNotification.newBuilder().setAlert(content)
                                        .addExtra("content", sms).build())
                                .build())
                .setAudience(
                        Audience.newBuilder()
                                .addAudienceTarget(AudienceTarget.alias(alis))
                                .build())
                .setMessage(
                        Message.newBuilder().setMsgContent(sms).addExtra("content",sms).build())
                .setOptions(Options.newBuilder().setApnsProduction(true).build())
                .build();
    }
    /**
     /**
     *  所有平台，推送目标是别名为 "alias1"，通知内容为  【神马都是浮云!】
     *
     *  */
    public static PushPayload buildPushSmsByAlias(String alis, String sms) {
        Message message = Message.newBuilder().setMsgContent(sms).build();
        return PushPayload.newBuilder().setPlatform(Platform.all())
                .setAudience(Audience.registrationId(alis))
                .setAudience(
                        Audience.newBuilder()
                                .addAudienceTarget(AudienceTarget.alias(alis))
                                .build())
      //       .setNotification(Notification.alert(pushData.getContent()))//通知
                .setMessage(message)//使用自定义消息推送
                .build();
    }

    public static PushPayload messageAll(String msgContent, SMS sms) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setMessage(Message.content(msgContent))
                .setSMS(sms)
                .build();
    }

}
