package com.dianba.pos.payment.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.base.exception.PosAccessDeniedException;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.order.service.QROrderManager;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.payment.config.AlipayConfig;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.payment.service.PaymentManager;
import com.dianba.pos.payment.service.WapPaymentManager;
import com.dianba.pos.payment.util.*;
import com.dianba.pos.qrcode.po.PosQRCode;
import com.dianba.pos.qrcode.service.PosQRCodeManager;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(PaymentURLConstant.WAP)
public class WapPaymentController {

    private static Logger logger = LogManager.getLogger(WapPaymentController.class);

    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private QROrderManager qrOrderManager;
    @Autowired
    private PosQRCodeManager posQRCodeManager;
    @Autowired
    private WapPaymentManager wapPaymentManager;
    @Autowired
    private PaymentManager paymentManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private AppConfig appConfig;

    /**
     * 扫码跳转页面-（判定扫码设备，以及微信鉴权）
     */
    @RequestMapping(value = "qr_scan/{code}", method = RequestMethod.GET)
    public ModelAndView qrScan(@PathVariable("code") String code) throws Exception {
        ModelAndView modelAndView = new ModelAndView("auth_code");
        PosQRCode posQRCode = posQRCodeManager.getQRCodeByCode(code);
        String authCodeUrl = wechatConfig.getAuthCodeUrl(appConfig.getPosCallBackHost()
                + PaymentURLConstant.WAP_CALLBACK_URL + posQRCode.getMerchantId());
        logger.info("扫码牌号：" + posQRCode.getCode() + "扫码对应商家：" + posQRCode.getMerchantId());
        modelAndView.addObject("url", authCodeUrl);
        modelAndView.addObject("passportId", posQRCode.getMerchantId());
        modelAndView.addObject("pay_url", PaymentURLConstant.WAP_CALLBACK_URL);
        return modelAndView;
    }

    /**
     * 支付宝跳转/微信授权回调
     */
    @RequestMapping("to_pay/{passportId}")
    public ModelAndView toPay(@PathVariable(name = "passportId") Long passportId
            , String code, String state) throws Exception {
        ModelAndView modelAndView = new ModelAndView("pay");
        Passport passport = passportManager.findById(passportId);
        modelAndView.addObject("passportId", passportId);
        modelAndView.addObject("showName", passport.getShowName());
        modelAndView.addObject("paymentType", PaymentTypeEnum.WEIXIN_JS.getKey());
        if (code == null || state == null) {
            modelAndView.addObject("paymentType", PaymentTypeEnum.ALIPAY.getKey());
            //支付宝直接返回
            return modelAndView;
        }
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


    /**
     * 支付宝WAP支付
     *
     * @param request
     * @param response
     * @param sequenceNumber
     * @throws Exception
     */
    @RequestMapping("alipay/{sequenceNumber}")
    public void aliPay(HttpServletRequest request, HttpServletResponse response
            , @PathVariable(name = "sequenceNumber") String sequenceNumber) throws Exception {
        wapPaymentManager.aliPayByOutHtml(response, sequenceNumber);
    }

    @RequestMapping("aliPayReturnUrl")
    public ModelAndView aliPayReturnUrl(HttpServletRequest request, HttpServletResponse response) {
        logger.info("支付宝同步回调开始！");
        ModelAndView modelAndView = new ModelAndView();
        try {
            boolean verifyResult = AlipaySignature.rsaCheckV1(ParamUtil.convertRequestMap(request)
                    , alipayConfig.getAlipayPublicKey()
                    , AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);//调用SDK验证签名
            logger.info("验签结果：" + verifyResult);
            if (verifyResult) {
                String amount = request.getParameter("total_amount");
                modelAndView.setViewName("pay_success");
                modelAndView.addObject("amount", amount);
            }
        } catch (AlipayApiException e) {
            modelAndView.setViewName("pay_error");
            e.printStackTrace();
        }
        logger.info("支付宝同步回调结束！");
        return modelAndView;
    }

    @RequestMapping("aliPayNotifyUrl")
    public void aliPayNotifyUrl(HttpServletRequest request, HttpServletResponse response) {
        logger.info("支付宝异步通知开始！");
        String text = "success";
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(ParamUtil.convertRequestMap(request)
                    , alipayConfig.getAlipayPublicKey()
                    , AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE); //调用SDK验证签名
            logger.info("验签结果：" + signVerified);
            if (signVerified) {
                if (AlipayResultUtil.isSuccess(request)) {
                    String sequenceNumber = request.getParameter("out_trade_no");
                    String buyerId = request.getParameter("buyer_id");
                    logger.info("支付宝扫码订单更新！" + sequenceNumber);
                    paymentManager.processPaidOrder(sequenceNumber, buyerId, PaymentTypeEnum.ALIPAY
                            , true, false);
                }
            }
        } catch (AlipayApiException e) {
            text = "failure";
            e.printStackTrace();
        }
        AlipayResultUtil.writeResult(response, text);
        logger.info("支付宝异步通知结束！");
    }

    /**
     * 微信预支付下单支付参数封装
     *
     * @param request
     * @param response
     * @param sequenceNumber
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("wechatPay/{sequenceNumber}")
    public ModelAndView wechatPay(HttpServletRequest request, HttpServletResponse response
            , @PathVariable(name = "sequenceNumber") String sequenceNumber)
            throws Exception {
        logger.info("微信预支付下单！");
        ModelAndView modelAndView = new ModelAndView();
        String ip = IPUtil.getRemoteIp(request);
        BasicResult basicResult = wapPaymentManager.wechatPay(sequenceNumber, ip);
        if (basicResult.isSuccess()) {
            modelAndView.addAllObjects(basicResult.getResponse());
            modelAndView.setViewName("wechat_js_pay");
        } else {
            logger.info(basicResult.getResponse().toJSONString());
            modelAndView.setViewName("pay_error");
        }
        logger.info("微信预支付下单结束!");
        return modelAndView;
    }

    /**
     * 微信JS支付回调
     */
    @RequestMapping("wechatNotifyUrl")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("开始接收微信支付异步回调消息：");
        Map<String, String> resultMap = XMLUtil.getRequestXML(request);
        if (WechatResultUtil.isSuccess(resultMap)) {
            String squenceNumber = resultMap.get("out_trade_no");
            String totalAmount = resultMap.get("total_fee");
            String openId = resultMap.get("openid");
            paymentManager.processPaidOrder(squenceNumber, openId, PaymentTypeEnum.WEIXIN_JS
                    , true, false);
        } else {
            String errMsg = WechatResultUtil.getErrorMsg(resultMap);
            logger.info("确认支付信息失败！" + errMsg);
            WechatResultUtil.writeFailResult(response, errMsg);
            return;
        }
        WechatResultUtil.writeSuccessResult(response);
        logger.info("微信支付异步回调结束！");
    }
}
