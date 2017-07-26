package com.dianba.pos.box.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.box.config.BoxAppConfig;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.po.BoxAccount;
import com.dianba.pos.box.service.BoxAccountManager;
import com.dianba.pos.box.util.DoorStatusUtil;
import com.dianba.pos.passport.service.LifeAchieveManager;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.passport.service.SMSManager;
import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.qrcode.service.PosQRCodeManager;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhangyong on 2017/7/17.
 */
@SuppressWarnings("all")
@Controller
@RequestMapping(BoxURLConstant.ACCOUNT)
public class BoxAccountController {

    private static Logger logger = LogManager.getLogger(BoxAccountController.class);
    @Autowired
    private SMSManager smsManager;

    @Autowired
    private BoxAccountManager boxAccountManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private PosQRCodeManager posQRCodeManager;
    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private BoxAppConfig boxAppConfig;
    @Autowired
    private LifeAchieveManager lifeAchieveManager;

    @ApiOperation("发送短信")
    @ResponseBody
    @RequestMapping(value = "sendSms", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResult sendSms(String phoneNumber) {
        return smsManager.sendSMSCode(phoneNumber);
    }

    @RequestMapping("showRegisterQRCode/{passportId}")
    public void showRegisterQRCode(HttpServletResponse response
            , @PathVariable("passportId") Long passportId) throws Exception {
        posQRCodeManager.generateQRCodeByContent(boxAppConfig.getBoxCallBackHost()
                        + BoxURLConstant.ACCOUNT + "qr_scan/" + passportId
                , 300, 300, response);
    }

    @ApiOperation("box注册")
    @ResponseBody
    @RequestMapping(value = "register", method = {RequestMethod.POST, RequestMethod.GET})
    public BasicResult register(BoxAccount boxAccount, String smsCode) {
        return boxAccountManager.registerBoxAccount(boxAccount, smsCode);
    }

    /**
     * 扫码接口实现微信授权
     */
    @RequestMapping(value = "qr_scan/{passportId}", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView qrScan(@PathVariable("passportId") Long passportId) throws Exception {
        String redirectUri = boxAppConfig.getBoxCallBackHost() + BoxURLConstant.ACCOUNT_CALLBACK_URL + passportId;
        String params = wechatConfig.getAuthCodeParam(redirectUri);
        ModelAndView modelAndView = new ModelAndView("account/auth_code");
        modelAndView.addObject("params", params);
        modelAndView.addObject("passportId", passportId);
        return modelAndView;
    }

    /**
     * 微信授权回调
     */
    @RequestMapping("authorization/{passportId}")
    public ModelAndView authorization(@PathVariable(name = "passportId") Long passportId
            , String code, String state) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        String openId = boxAccountManager.getOpenId(code, state);
        boolean isRegistered = boxAccountManager.checkIsRegistered(openId);
        if (isRegistered) {
            return position(passportId, openId);
        } else {
            modelAndView.setViewName("account/register");
        }
        modelAndView.addObject("passportId", passportId);
        modelAndView.addObject("openId", openId);
        return modelAndView;
    }

    @RequestMapping("position")
    public ModelAndView position(Long passportId, String openId) {
        JSONObject param = boxAccountManager.position(passportId);
        ModelAndView modelAndView = new ModelAndView("account/position");
        modelAndView.addObject("passportId", passportId);
        modelAndView.addObject("openId", openId);
        modelAndView.addObject("longitude", param.getString("longitude"));
        modelAndView.addObject("latitude", param.getString("latitude"));
        String securityKey = DoorStatusUtil.getDoorSecurityKey(passportId);
        modelAndView.addObject("securitykey", securityKey);
        logger.info("开门安全校验KEY：" + securityKey);
        return modelAndView;
    }
}
