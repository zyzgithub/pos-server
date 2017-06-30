package com.dianba.pos.qrcode.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.base.exception.PosAccessDeniedException;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.payment.util.MD5Util;
import com.dianba.pos.qrcode.config.QRCodeURLConstant;
import com.dianba.pos.qrcode.po.PosQRCode;
import com.dianba.pos.qrcode.service.PosQRCodeManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api("商家二维码管理器")
@Controller
@RequestMapping(QRCodeURLConstant.MANAGER)
public class QRCodeController {

    private static Logger logger = LogManager.getLogger(QRCodeController.class);

    @Autowired
    private PosQRCodeManager posQRCodeManager;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private WechatConfig wechatConfig;

    @ApiOperation("通过CODE下载商家二维码")
    @RequestMapping(value = "get_qrcode/{code}", method = {RequestMethod.GET})
    public void getQRCodeByCode(HttpServletRequest request, HttpServletResponse response
            , @PathVariable("code") String code) throws Exception {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + code + ".png");
        posQRCodeManager.putQRCodeInOutPutStrem(code, 300, 300, response);
    }

    @ApiOperation("通过CODE暂时商家二维码")
    @RequestMapping(value = "show_qrcode/{code}", method = {RequestMethod.GET})
    public void showQRCodeByCode(HttpServletRequest request, HttpServletResponse response
            , @PathVariable("code") String code) throws Exception {
        posQRCodeManager.putQRCodeInOutPutStrem(code, 300, 300, response);
    }

    /**
     * 扫码跳转页面-（判定扫码设备，以及微信鉴权）
     */
    @RequestMapping(value = "qr_scan/{code}", method = RequestMethod.GET)
    public ModelAndView qrScan(@PathVariable("code") String code)
            throws Exception {
        ModelAndView modelAndView = new ModelAndView("auth_code");
        PosQRCode posQRCode = posQRCodeManager.getQRCodeByCode(code);
        String authCodeUrl = wechatConfig.getAuthCodeUrl(appConfig.getPosCallBackHost()
                + QRCodeURLConstant.WECHAT_CALLBACK_URL + posQRCode.getMerchantId());
        modelAndView.addObject("url", authCodeUrl);
        modelAndView.addObject("passportId", posQRCode.getMerchantId());
        return modelAndView;
    }

    /**
     * 微信授权回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("to_pay/{passportId}")
    public ModelAndView toPay(HttpServletRequest request, HttpServletResponse response
            , @PathVariable(name = "passportId") Long passportId
            , String code, String state) throws Exception {
        ModelAndView modelAndView = new ModelAndView("pay");
        modelAndView.addObject("passportId", passportId);
        if (code == null || state == null) {
            throw new PosIllegalArgumentException("网页授权失败！");
        }
        String rightState = MD5Util.md5(appConfig.getPosCallBackHost() + appConfig.getPosAppKey());
        if (!rightState.equals(state)) {
            throw new PosAccessDeniedException("鉴权失败！访问拒绝！");
        }
        logger.info("微信授权回调开始！");
        logger.info(code + " " + state);
        String authTokenUrl = wechatConfig.getAccessTokenUrl(code);
        JSONObject jsonObject = HttpUtil.post(authTokenUrl, new JSONObject());
        if (jsonObject != null) {
            if (null == jsonObject.get("errcode")) {
                modelAndView.addObject("access_token", jsonObject.getString("access_token"));
                modelAndView.addObject("expires_in", jsonObject.getString("expires_in"));
                modelAndView.addObject("refresh_token", jsonObject.getString("refresh_token"));
                modelAndView.addObject("openId", jsonObject.getString("openid"));
                modelAndView.addObject("scope", jsonObject.getString("scope"));
            } else {
                throw new PosIllegalArgumentException(jsonObject.toJSONString());
            }
        }
        logger.info("微信授权回调结束！");
        return modelAndView;
    }
}
