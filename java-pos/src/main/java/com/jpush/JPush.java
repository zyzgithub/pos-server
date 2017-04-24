package com.jpush;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.wm.entity.merchant.MerchantEntity;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.base.enums.AppTypeConstants;

public class JPush {

    protected static final Logger logger = LoggerFactory.getLogger(JPush.class);

    // 快递员版
    private static final String appKeyCourier = "ac61d66dc3f68c71c26df547";
    private static final String masterSecretCourier = "396336d24aed7ea24804b023";

    // 商家版
    private static final String appKeyMerchant = "e09338a244b13f841ebeaa90";
    private static final String masterSecretMerchant = "93a6905bdb0ccf0be51a593e";

    //乡村基
    private static final String appKeyRuralbase = "07e031d604a9311d8d8bd700";
    private static final String masterSecretRuralbase = "05450c07fa17bf161aeec88c";

    // APNs 的推送环境，默认是false
    private static final Boolean apnProduction = true;

    // 消息缓存时常，默认24小数
    private static final Integer timeToLive = 1 * 3600;

    public static void pushSunmiOrder(Integer orderId, Integer merchantId) {
        String[] targets = new String[]{"sunmi" + merchantId};
        logger.info("pushSunmiOrder merchantId is : " +merchantId+"");

        Map<String, String> extras = new HashMap<String, String>();
        extras.put("appType", AppTypeConstants.APP_TYPE_RURALBASE);
        extras.put("orderId", orderId + "");
        extras.put("action",  "printOrder");
        extras.put("accessToken", UUID.randomUUID().toString().replace("-", ""));
        JPush.push(targets, "您有一条新的订单", "您有一条新的订单", SoundFile.SOUND_NEW_ORDER, extras);
    }

    /**
     * 极光推送API
     *
     * @param targets
     * @param title
     * @param content
     * @param voiceFile
     * @param extras
     */
    public static String push(String[] targets, String title, String content, String voiceFile, Map<String, String> extras) {

        logger.info("JPush Extras    [" + extras + "]");

        String appKey = appKeyCourier;
        String masterSecret = masterSecretCourier;

        // 应用类型：Courier（默认）、Merchant、Ruralbase
        String appType = extras.get("appType");
        if (AppTypeConstants.APP_TYPE_MERCHANT.equals(appType)) {
            appKey = appKeyMerchant;
            masterSecret = masterSecretMerchant;
        } else if (AppTypeConstants.APP_TYPE_RURALBASE.equals(appType)) {
            appKey = appKeyRuralbase;
            masterSecret = masterSecretRuralbase;
        }

        if (StringUtils.isEmpty(title)) {
            title = "【一号外卖】消息提醒";
        }
        if (StringUtils.isEmpty(content)) {
            content = "【一号外卖】您有一条提醒消息";
        }

        JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        PushPayload.Builder payload = PushPayload.newBuilder();
        // 语音提醒
        if (StringUtils.isNotEmpty(voiceFile)) {
            extras.put("voiceFile", voiceFile);
        }

        //推送类型默认推送通知，如果 pushType：Message 则消息推送
        String pushType = extras.get("pushType");
        payload.setOptions(Options.newBuilder().setApnsProduction(apnProduction).setTimeToLive(timeToLive).build())
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(targets));
        if ("Message".equals(pushType)) {
            payload.setMessage(Message.newBuilder().setTitle(title).setMsgContent(content).addExtras(extras).build());
        } else {
            payload.setNotification(Notification.newBuilder()
                    .addPlatformNotification(IosNotification.newBuilder().setAlert(title).setSound(voiceFile).addExtras(extras).build())
                    .addPlatformNotification(AndroidNotification.newBuilder().setAlert(title).setTitle(content).addExtras(extras).build()).build());
        }

        PushPayload pushPayload = payload.build();
        logger.info("JPush payload:{}", pushPayload.toString());
        PushResult result;
        try {
            result = jpushClient.sendPush(pushPayload);
            logger.info("JPush Receivers [" + ArrayUtils.toString(targets) + "]");
            logger.info("JPush Result    [" + result + "]");
            return result.toString();
        } catch (APIConnectionException e) {
            e.printStackTrace();
            logger.error("推送服务连接失败，请及时联系极光服务团队【QQ:2501528415】！！！！！\n", e);
        } catch (APIRequestException e) {
            e.printStackTrace();
            logger.info("JPush APIRequestException", e);
            int errorCode = e.getErrorCode();
            if (1 == errorCode) {
                logger.error("推送失败：快递员未上线，或者已注销");
            }
            return "ErrorCode:" + errorCode + ", ErrorMsg:" + e.getErrorMessage();
        }
        return "failed";
    }

    public static void main(String[] args) {
        String[] targets = new String[]{"3369"};
        Map<String, String> extras = new HashMap<String, String>();
        extras.put("appType", AppTypeConstants.APP_TYPE_MERCHANT);
        extras.put("orderId", "123");
//		extras.put("scramble", "5");// 可抢单数
//		extras.put("sound", "new_comment.mp3");// 可抢单数
//		extras.put("alert", "new_comment_5");// 可抢单数
//		JPush.push(targets, "测试3", "语音测试3", SoundFile.SOUND_URGENT_ORDER, extras);
//		JPush.push(targets, "测试2", "语音测试2", SoundFile.SOUND_NEW_COMMENT, extras);
        JPush.push(targets, "测试1", "语音测试1", SoundFile.SOUND_NEW_ORDER, extras);
//		JPush.push(targets, "1", "2", "", extras);

//		JPush.push(targets, "您有一条新的评论", "您有一条新的评论", SoundFile.SOUND_NEW_COMMENT, extras);
    }

}
