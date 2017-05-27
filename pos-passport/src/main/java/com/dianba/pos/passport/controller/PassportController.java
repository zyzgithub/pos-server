package com.dianba.pos.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.passport.config.PassportProperties;
import com.dianba.pos.passport.config.PassportURLConstant;
import com.dianba.pos.passport.vo.PassportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(PassportURLConstant.LOGIN_URL)
public class PassportController {

    @Autowired
    private PassportProperties passportProperties;

    @RequestMapping(value = "loginPassport")
    public AjaxJson loginPassport(HttpServletResponse response, PassportVo
            passportVo) {

        AjaxJson ajaxJson = new AjaxJson();

        JSONObject jsonObject = HttpUtil.post(passportProperties.getLogin(), passportVo);

        return ajaxJson;
    }
}
