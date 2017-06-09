package com.dianba.pos.extended.controller;

import com.dianba.pos.common.exception.lang.AbstractApiController;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.extended.config.ExtendedUrlConstant;
import com.dianba.pos.extended.po.PhoneInfo;
import com.dianba.pos.extended.service.PhoneInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(ExtendedUrlConstant.PHONE_INFO)
public class PhoneInfoController extends AbstractApiController {

    @Autowired
    private PhoneInfoManager phoneInfoManager;

    @ResponseBody
    @RequestMapping("get_phone")
    public AjaxJson getPhoneInfo(Long phoneNumber) {
        AjaxJson ajaxJson = AjaxJson.successJson("请求成功");
        PhoneInfo phoneInfo = phoneInfoManager.findByMobileNumber(phoneNumber);
        ajaxJson.setObj(phoneInfo);
        return ajaxJson;
    }
}
