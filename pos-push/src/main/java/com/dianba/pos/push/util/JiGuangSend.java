package com.dianba.pos.push.util;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;


public class JiGuangSend {

    public static String sendPushWithAliasAndAlert(String alias, String alert, String msg) {
        PushResult result = null;
        JPushClient jpushClient = new JPushClient(ConstantsUtil.MERCHANT_JIGUANG_SECRET, ConstantsUtil
                .MERCHANT_JIGUANG_KEY);
        PushPayload payload = PushExample.buildPushAlertAndMsgByAlias(alias, alert, msg);
        try {
            result = jpushClient.sendPush(payload);
            System.out.println(result);

        } catch (APIConnectionException e) {

        } catch (APIRequestException e) {

        }
        if (result == null) {
            return "null";
        } else {
            return result.toString();
        }
    }

    public static String getPrettyNumber(String number) {
        return BigDecimal.valueOf(Double.parseDouble(number))
                .stripTrailingZeros().toPlainString();
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", JPushTypeEnum.SETTLEMENT.getKey());
        jsonObject.put("msg", JPushTypeEnum.SETTLEMENT.getMsg());
        sendPushWithAliasAndAlert("1913491", JPushTypeEnum.SETTLEMENT.getTitle(), jsonObject.toString());
    }


}
