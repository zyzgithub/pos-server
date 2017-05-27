package com.dianba.pos.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.common.util.AjaxJson;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.passport.config.PassportProperties;
import com.dianba.pos.passport.config.PassportURLConstant;
import com.dianba.pos.passport.vo.PassportVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(PassportURLConstant.LOGIN_URL)
public class PassportController {


    private static Logger logger = LogManager.getLogger(PassportController.class);

    @Autowired
    private PassportProperties passportProperties;


    @RequestMapping("loginPassport")
    @ResponseBody
    public AjaxJson loginPassport(PassportVo passportVo) {


        AjaxJson ajaxJson = new AjaxJson();

        JSONObject jsonObject = HttpUtil.post(passportProperties.getLogin(), passportVo);
        logger.info("pos 端登录：" + jsonObject.toJSONString());
        if (jsonObject.getIntValue("code") != 0) {
            ajaxJson.setStateCode(AjaxJson.STATE_CODE_FAIL);
            ajaxJson.setSuccess(false);
        }
        String res = jsonObject.get("response").toString();
        String msg = jsonObject.getString("msg");
        ajaxJson.setMsg(msg);
        ajaxJson.setObj(res);
        return ajaxJson;
    }
}
