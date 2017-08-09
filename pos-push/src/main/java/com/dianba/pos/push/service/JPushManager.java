package com.dianba.pos.push.service;

/**
 * Created by zhangyong on 2017/8/9.
 */
public interface JPushManager {
    /**
     * 通知加自定义消息(语言播报)
     * @param passportId
     * @param msg
     */
    void posJPush(String passportId,String msg,String orderNum);

    /**推送自定义消息(语言播报)**/
    void posJPushMsg(String passportId,String msg,String orderNum);

    /**拉黑推送**/
    void posJPushByBlackList(String passportId,String orderNum);

    /**商家结算清单推送**/
    void merchantJPushBySettlement(String passportId);
}
