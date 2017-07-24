package com.dianba.pos.box.service;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.box.po.BoxAccount;
/**
 * Created by zhangyong on 2017/7/17.
 */
public interface BoxAccountManager {
    /**注册box账号**/
    BasicResult registerBoxAccount(BoxAccount posBoxAccount, String smsCode);
    /**是否注册过账号**/
    JSONObject authorizationOpenDoor(Long passportId, String code, String state) throws Exception ;
    JSONObject position(Long passportId) ;
}
