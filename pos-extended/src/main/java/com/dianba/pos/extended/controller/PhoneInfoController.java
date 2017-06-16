package com.dianba.pos.extended.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.exception.lang.AbstractApiController;
import com.dianba.pos.extended.config.ExtendedUrlConstant;
import com.dianba.pos.extended.po.PosPhoneInfo;
import com.dianba.pos.extended.service.PosPhoneInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(ExtendedUrlConstant.PHONE_INFO)
public class PhoneInfoController extends AbstractApiController {

    @Autowired
    private PosPhoneInfoManager phoneInfoManager;

    @ResponseBody
    @RequestMapping("get_phone")
    public BasicResult getPhoneInfo(Long phoneNumber) {
       ;
        PosPhoneInfo phoneInfo = phoneInfoManager.findByMobileNumber(phoneNumber);

        JSONObject jsonObject=(JSONObject) JSONObject.toJSON(phoneInfo);
        return BasicResult.createSuccessResult("请求成功",jsonObject);
    }
}
