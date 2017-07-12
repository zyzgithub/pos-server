package com.dianba.pos.passport.support;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.JPushTypeEnum;
import com.dianba.pos.common.util.PushExample;
import com.dianba.pos.passport.config.JPushConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zhangyong on 2017/7/12.
 * 极光推送业务处理
 */
public class JPushRemoteService {

    @Autowired
    private JPushConfig jPushConfig;

    protected String sendPushWithAlias(String passportId,String content,String msg){
        PushResult result=null;
        JPushClient jpushClient = new JPushClient(jPushConfig.getPosJPushSecret(),jPushConfig.getPosJPushKey());
        PushPayload payload = PushExample.buildPushObject_all_alias_alert(passportId,content,msg);
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

    protected void posJPush(String passportId,String msg){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("type", JPushTypeEnum.SPEAK.getKey());
        jsonObject.put("msg",JPushTypeEnum.SPEAK.getMsg()+msg+"元");
        sendPushWithAlias(passportId,JPushTypeEnum.SPEAK.getTitle(),jsonObject.toJSONString());
    }
}
