package com.dianba.pos.passport.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.JPushTypeEnum;
import com.dianba.pos.common.util.StringUtil;
import com.dianba.pos.passport.po.PosPushLog;
import com.dianba.pos.passport.repository.PosPushLogJpaRepository;
import com.dianba.pos.passport.service.PosPushLogManager;
import com.dianba.pos.passport.support.JPushRemoteService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by zhangyong on 2017/7/12.
 */
@Service
public class DefaultPosPushLogManager extends JPushRemoteService implements PosPushLogManager {

    private String getPrettyNumber(String number) {
        return BigDecimal.valueOf(Double.parseDouble(number))
                .stripTrailingZeros().toPlainString();
    }
    private  Logger logger = LogManager.getLogger(DefaultPosPushLogManager.class);
    @Autowired
    private PosPushLogJpaRepository pushLogJpaRepository;

    @Autowired
    private  PosPushLogManager posPushLogManager;
    @Override
    public void posJPush(String passportId, String msg,String orderNum) {
        JSONObject jsonObject=new JSONObject();
        String mon=getPrettyNumber(msg);
        jsonObject.put("type", JPushTypeEnum.SPEAK.getKey());
        jsonObject.put("msg",JPushTypeEnum.SPEAK.getMsg()+mon+"元");
        logger.info("推送的商家id:"+passportId);
        logger.info("推送的内容:"+jsonObject);
        String result= sendPushWithAlias(passportId,JPushTypeEnum.SPEAK.getTitle(),jsonObject.toJSONString());
        if(!StringUtil.isEmpty(result)){
            logger.info("极光推送返回:=="+result);
            JSONObject object= JSON.parseObject(result);
                int code=object.getIntValue("statusCode");
                if(code==0){
                    PosPushLog posPushLog=new PosPushLog();
                    posPushLog.setPassportId(Long.parseLong(passportId));
                    posPushLog.setMessage(jsonObject.toJSONString());
                    posPushLog.setTitle(JPushTypeEnum.SPEAK.getTitle());
                    posPushLog.setSequenceNumber(orderNum);
                    posPushLog.setState("success");
                    pushLogJpaRepository.save(posPushLog);
                    logger.info("======推送成功====");
                }

        }else {
            PosPushLog posPushLog=new PosPushLog();
            posPushLog.setPassportId(Long.parseLong(passportId));
            posPushLog.setMessage(jsonObject.toJSONString());
            posPushLog.setTitle(JPushTypeEnum.SPEAK.getTitle());
            posPushLog.setSequenceNumber(orderNum);
            posPushLog.setState("error");
            pushLogJpaRepository.save(posPushLog);
            logger.info("======推送失败====");
        }
    }

    @Override
    public void posJPushMsg(String passportId, String msg,String orderNum) {
        JSONObject jsonObject=new JSONObject();
        String mon=getPrettyNumber(msg);
        jsonObject.put("type", JPushTypeEnum.SPEAK.getKey());
        jsonObject.put("msg",JPushTypeEnum.SPEAK.getMsg()+mon+"元");
        logger.info("推送的商家id:"+passportId);
        logger.info("推送的内容:"+jsonObject);
        String result=  sendPushWithAliasAndSms(passportId,jsonObject.toJSONString());
        if(!StringUtil.isEmpty(result)){
             JSONObject object= JSON.parseObject(result);
                int code=object.getIntValue("statusCode");
                if(code==0){
                    PosPushLog posPushLog=new PosPushLog();
                    posPushLog.setPassportId(Long.parseLong(passportId));
                    posPushLog.setMessage(jsonObject.toJSONString());
                    posPushLog.setSequenceNumber(orderNum);
                    posPushLog.setTitle("");
                    posPushLog.setState("success");
                    pushLogJpaRepository.save(posPushLog);
                    logger.info("========推送成功=====");
                }


        }else {
            PosPushLog posPushLog=new PosPushLog();
            posPushLog.setPassportId(Long.parseLong(passportId));
            posPushLog.setMessage(jsonObject.toJSONString());
            posPushLog.setSequenceNumber(orderNum);
            posPushLog.setTitle("");
            posPushLog.setState("error");
            pushLogJpaRepository.save(posPushLog);
            logger.info("========推送失败=====");
        }
    }

    @Override
    public void posJPushByBlackList(String passportId,String orderNum) {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("type", JPushTypeEnum.BLACKLIST.getKey());
        jsonObject.put("msg",JPushTypeEnum.BLACKLIST.getMsg());
        logger.info("推送的商家id:"+passportId);
        logger.info("推送的内容:"+jsonObject);
        String result= sendPushWithAlias(passportId,JPushTypeEnum.BLACKLIST.getTitle(),jsonObject.toJSONString());
        if(!StringUtil.isEmpty(result)){
            logger.info("极光推送返回:=="+result);
            JSONObject object= JSON.parseObject(result);
            int code=object.getIntValue("statusCode");
            if(code==0){
                PosPushLog posPushLog=new PosPushLog();
                posPushLog.setPassportId(Long.parseLong(passportId));
                posPushLog.setMessage(jsonObject.toJSONString());
                posPushLog.setTitle(JPushTypeEnum.BLACKLIST.getTitle());
                posPushLog.setSequenceNumber(orderNum);
                posPushLog.setState("success");
                pushLogJpaRepository.save(posPushLog);
                logger.info("======推送成功=====");
            }
        }else {
            PosPushLog posPushLog=new PosPushLog();
            posPushLog.setPassportId(Long.parseLong(passportId));
            posPushLog.setMessage(jsonObject.toJSONString());
            posPushLog.setTitle(JPushTypeEnum.BLACKLIST.getTitle());
            posPushLog.setSequenceNumber(orderNum);
            posPushLog.setState("error");
            pushLogJpaRepository.save(posPushLog);
            logger.info("======推送失败=====");
        }
    }


}
