package com.dianba.pos.passport.controller;

import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.common.util.MapUtil;
import com.dianba.pos.passport.config.PassportProperties;
import com.dianba.pos.passport.config.PassportURLConstant;
import com.dianba.pos.passport.vo.PassportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(PassportURLConstant.LOGIN_URL)
public class PassportController {

    @Autowired
    private PassportProperties passportProperties;

    @RequestMapping(value = "loginPassport")
    public AjaxJson loginPassport(HttpServletResponse response, PassportVo
            passportVo) {

        AjaxJson ajaxJson = new AjaxJson();

        Map<String, Object> map = MapUtil.beanToMap(passportVo);

        String result = HttpUtil.sendPost(response, passportProperties.getLogin(), map, "UTF-8");

        return ajaxJson;
    }
}
