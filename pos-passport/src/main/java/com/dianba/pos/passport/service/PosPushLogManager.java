package com.dianba.pos.passport.service;

/**
 * Created by zhangyong on 2017/7/12.
 */
public interface PosPushLogManager {

    /**
     * 通知加自定义消息(语言播报)
     * @param passportId
     * @param msg
     */
    void posJPush(String passportId,String msg,String orderNum);

    /**推送自定义消息(语言播报)**/
    void posJPushMsg(String passportId,String msg,String orderNum);

    void posJPushByBlackList(String passportId,String orderNum);
}
