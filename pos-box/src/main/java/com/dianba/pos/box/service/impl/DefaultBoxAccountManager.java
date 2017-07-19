package com.dianba.pos.box.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.base.exception.PosAccessDeniedException;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.box.po.BoxAccount;
import com.dianba.pos.box.repository.BoxAccountJpaRepository;
import com.dianba.pos.box.service.BoxAccountManager;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.passport.service.SMSManager;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.payment.util.MD5Util;
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
    private AppConfig appConfig;

    @Autowired
    private BoxAccountJpaRepository boxAccountJpaRepository;
    @Override
    public BasicResult registerBoxAccount(BoxAccount posBoxAccount, String smsCode) {
         BasicResult result= smsManager.verifySMSCode(posBoxAccount.getPhoneNumber(),smsCode);
         if(result.isSuccess()){ //验证成功.
             posBoxAccountJpaRepository.save(posBoxAccount);
             return BasicResult.createSuccessResult("注册成功");
         }else {
             return BasicResult.createFailResult("验证码输入有误!");
         }
    }

    @Override
    public JSONObject authorizationOpenDoor(Long passportId, String code, String state) throws Exception {
        JSONObject param=new JSONObject();
        if (code == null || state == null) {
            //支付宝直接返回
            String  openId=passportId.toString()    ;
            BoxAccount boxAccount=  boxAccountJpaRepository.findByOpenId(openId);
            if(boxAccount==null){
                param.put("view","register");
            }else {
                param.put("view","lock");
            }
        }else {
            logger.info("微信回调参数：passportId" + passportId);
            logger.info("微信回调参数：code" + code);
            logger.info("微信回调参数：state" + state);
            String rightState = MD5Util.md5(appConfig.getPosCallBackHost()
                    + PaymentURLConstant.WAP_CALLBACK_URL + passportId
                    + wechatConfig.getPublicAppSecrect());
            logger.info("参数验签：state:" + state);
            logger.info("自主验签：rightState:" + rightState);
            if (!rightState.equals(state)) {
                throw new PosAccessDeniedException("鉴权失败！访问拒绝！");
            }
            logger.info("微信授权回调开始！");
            logger.info(code + " " + state);
            String authTokenUrl = wechatConfig.getAccessTokenUrl(code);
            JSONObject jsonObject = HttpUtil.post(authTokenUrl, new JSONObject());
            if (jsonObject != null) {
                if (null == jsonObject.get("errcode")) {
                    String openId=jsonObject.getString("openid");
                    param.put("openId",openId);
                    //判断openId 是否被注册过
                    BoxAccount boxAccount=  boxAccountJpaRepository.findByOpenId(openId);
                    if(boxAccount==null){
                        param.put("view","register");
                    }else {
                        param.put("view","lock");
                    }
                } else {
                    throw new PosIllegalArgumentException(jsonObject.toJSONString());
                }
            }
            logger.info("微信授权回调结束！");
        }

        return param;
    }
}
