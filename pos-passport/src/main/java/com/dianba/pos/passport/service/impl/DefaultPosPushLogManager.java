package com.dianba.pos.passport.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.JPushTypeEnum;
import com.dianba.pos.passport.service.PosPushLogManager;
import com.dianba.pos.passport.support.JPushRemoteService;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyong on 2017/7/12.
 */
@Service
public class DefaultPosPushLogManager extends JPushRemoteService implements PosPushLogManager {
    @Override
    public void posJPush(String passportId, String msg) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("type", JPushTypeEnum.SPEAK.getKey());
        jsonObject.put("msg",JPushTypeEnum.SPEAK.getMsg()+msg+"元");
        sendPushWithAlias(passportId,JPushTypeEnum.SPEAK.getTitle(),jsonObject.toJSONString());
    }
}
