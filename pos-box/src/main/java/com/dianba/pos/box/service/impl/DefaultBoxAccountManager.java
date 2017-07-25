package com.dianba.pos.box.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.box.config.BoxAppConfig;
import com.dianba.pos.box.po.BoxAccount;
import com.dianba.pos.box.repository.BoxAccountJpaRepository;
import com.dianba.pos.box.service.BoxAccountManager;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.passport.po.LifeAchieve;
import com.dianba.pos.passport.service.LifeAchieveManager;
import com.dianba.pos.passport.service.SMSManager;
import com.dianba.pos.payment.config.WechatConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyong on 2017/7/17.
 */
@Service
public class DefaultBoxAccountManager implements BoxAccountManager {

    private static Logger logger = LogManager.getLogger(DefaultBoxAccountManager.class);
    @Autowired
    private BoxAccountJpaRepository posBoxAccountJpaRepository;
    @Autowired
    private SMSManager smsManager;
    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private BoxAppConfig boxAppConfig;
    @Autowired
    private BoxAccountJpaRepository boxAccountJpaRepository;
    @Autowired
    private LifeAchieveManager lifeAchieveManager;

    @Override
    public BasicResult registerBoxAccount(BoxAccount posBoxAccount, String smsCode) {
        BasicResult result = smsManager.verifySMSCode(posBoxAccount.getPhoneNumber(), smsCode);
        if (result.isSuccess()) { //验证成功.
            posBoxAccountJpaRepository.save(posBoxAccount);
            return BasicResult.createSuccessResult("注册成功");
        } else {
            return BasicResult.createFailResult("验证码输入有误!");
        }
    }

    @Override
    public JSONObject authorizationOpenDoor(Long passportId, String code, String state) throws Exception {
        JSONObject param = new JSONObject();
        logger.info("微信回调参数：passportId" + passportId);
        logger.info("微信回调参数：code" + code);
        logger.info("微信回调参数：state" + state);
        logger.info("微信授权回调开始！");
        logger.info(code + " " + state);
        String authTokenUrl = wechatConfig.getAccessTokenUrl(code);
        JSONObject jsonObject = HttpUtil.post(authTokenUrl, new JSONObject());
        if (jsonObject != null) {
            if (null == jsonObject.get("errcode")) {
                String openId = jsonObject.getString("openid");
                param.put("openId", openId);
                //判断openId 是否被注册过
                BoxAccount boxAccount = boxAccountJpaRepository.findByOpenId(openId);
                LifeAchieve lifeAchieve = lifeAchieveManager.findByPassportId(passportId);
                //定位当前
                if (boxAccount == null) {
                    //  param.put("view", "account/register");
                    param.put("isFlag", false);
                } else {
                    param.put("longitude", lifeAchieve.getLongitude());
                    param.put("latitude", lifeAchieve.getLatitude());
                    // param.put("view", "account/position");
                    param.put("isFlag", true);
                }
            } else {
                throw new PosIllegalArgumentException(jsonObject.toJSONString());
            }
        }
        logger.info("微信授权回调结束！");
        return param;
    }

    @Override
    public JSONObject position(Long passportId) {
        JSONObject param = new JSONObject();
        LifeAchieve lifeAchieve = lifeAchieveManager.findByPassportId(passportId);
        if (lifeAchieve != null) {
            param.put("longitude", lifeAchieve.getLongitude());
            param.put("latitude", lifeAchieve.getLatitude());
        }
        return param;
    }
}
