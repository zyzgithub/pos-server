package com.dianba.pos.passport.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.passport.config.PassportProperties;
import com.dianba.pos.passport.config.PassportURLConstant;
import com.dianba.pos.passport.vo.PassportVo;
import com.dianba.pos.passport.vo.RegisterVo;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(PassportURLConstant.BASE_URL)
public class PassportController {


    private static Logger logger = LogManager.getLogger(PassportController.class);

    @Autowired
    private PassportProperties passportProperties;


    @RequestMapping("loginPassport")
    @ResponseBody
    public BasicResult loginPassport(PassportVo passportVo) {

        logger.info("登录用户名："+passportVo.getUsername());
        JSONObject jsonObject = HttpUtil.post(passportProperties.getLogin(), passportVo);
        logger.info(passportProperties.getLogin());
        JSONObject response = jsonObject.getJSONObject("response");
        String msg = jsonObject.getString("msg");
        logger.info("pos 端登录返回结果：" + jsonObject.toJSONString());
        if (jsonObject.getIntValue("code") != 0) {
            return  BasicResult.createFailResult(msg);
        }else {


            return  BasicResult.createSuccessResult(msg,response);
        }


    }

    /***
     * 注册收银员账号
     * @return passportId - long 通行证ID
               showName - String 可用于展示的名字
               phoneNumber - String 手机号（已隐藏部分字符，前端直接展示即可）
               headImage - String 头像地址
               roleValue - int 角色类型值，参考：PassportRoleTypeEnum
               accessToken - String 访问令牌(暂时未使用)
     */
    @ResponseBody
    @RequestMapping("registerPosAccount")
    public BasicResult registerPosAccount(RegisterVo registerVo){

        logger.info("====================收银员账号注册==================");

        logger.info("注册账号："+registerVo.getName());
        JSONObject jsonObject = HttpUtil.post(passportProperties.getRegister(), registerVo);
        logger.info("注册返回："+jsonObject.toJSONString());

        return BasicResult.createSuccessResult();
    }
}
