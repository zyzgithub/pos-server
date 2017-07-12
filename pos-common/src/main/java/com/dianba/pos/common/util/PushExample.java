package com.dianba.pos.common.util;
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
    public static PushPayload buildPushObject_all_all_alert(String name) {
        return PushPayload.alertAll(name);
    }
    /**
     *  所有平台，推送目标是别名为 "alias1"，通知内容为  【神马都是浮云!】
     *
     *  */
    public static PushPayload buildPushObject_all_alias_alert(String alis,String content) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(alis))
                .setNotification(Notification.alert(content))
                .build();
    }
    /**
     /**
     *  所有平台，推送目标是别名为 "alias1"，通知内容为  【神马都是浮云!】
     *
     *  */
    public static PushPayload buildPushObject_all_alias_alert(String alis,String content, String sms) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setNotification(
                        Notification
                                .newBuilder()
                                .addPlatformNotification(
                                        AndroidNotification.newBuilder()
                                                .setAlert(content).build())
                                .addPlatformNotification(IosNotification.newBuilder().setAlert(content).addExtra("content", sms).build())
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
    public static PushPayload buildPushObject_all_alias_sms(String alis, String sms) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setNotification(
                        Notification
                                .newBuilder()
                                .addPlatformNotification(
                                        AndroidNotification.newBuilder()
                                               .build())
                                .addPlatformNotification(IosNotification.newBuilder().addExtra("content", sms).build())
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
     *  平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 【这是内容】，并且标题为 【这是标题】。
     *
     *  */
    public static PushPayload buildPushObject_android_tag_alertWithTitle(String shebei,String content,String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(shebei))
                .setNotification(Notification.android(content,title, null))
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
