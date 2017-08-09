package com.dianba.pos.push.support;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.dianba.pos.push.config.JPushConfig;
import com.dianba.pos.push.util.PushExample;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhangyong on 2017/7/12.
 * 极光推送业务处理
 */
public class JPushRemoteService {

    @Autowired
    private JPushConfig jPushConfig;

    /**
     * 语音播报 通知加自定义消息
     * @param passportId
     * @param content
     * @param msg
     * @return
     */
    protected String sendPushAlertAndMsgByAlias(String passportId,String content,String msg){
        PushResult result=null;
        JPushClient jpushClient = new JPushClient(jPushConfig.getPosJPushSecret(),jPushConfig.getPosJPushKey());
        PushPayload payload = PushExample.buildPushAlertAndMsgByAlias(passportId,content,msg);
        try {
            result = jpushClient.sendPush(payload);
            System.out.println(result);

        } catch (APIConnectionException e) {

        } catch (APIRequestException e) {
        }
        if(result==null){
            return "null";
        }else{
            return result.toString();
        }
    }

    /**
     * 语言播报 自定义消息
     * @param passportId
     * @param msg
     * @return
     */
    protected String sendPushMsgByAlias(String passportId,String msg){
        PushResult result=null;
        JPushClient jpushClient = new JPushClient(jPushConfig.getPosJPushSecret(),jPushConfig.getPosJPushKey());
        PushPayload payload = PushExample.buildPushSmsByAlias(passportId,msg);
        try {
            result = jpushClient.sendPush(payload);
            System.out.println(result);

        } catch (APIConnectionException e) {

        } catch (APIRequestException e) {
        }
        if(result==null){
            return "null";
        }else{
            return result.toString();
        }
    }
}
