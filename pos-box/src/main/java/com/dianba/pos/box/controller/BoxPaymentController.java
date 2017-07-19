package com.dianba.pos.box.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.box.config.BoxAppConfig;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.service.BoxItemLabelManager;
import com.dianba.pos.box.service.BoxOrderManager;
import com.dianba.pos.box.util.ScanItemsUtil;
import com.dianba.pos.box.vo.BoxItemVo;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.payment.config.AlipayConfig;
import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.payment.service.PaymentManager;
import com.dianba.pos.payment.service.WapPaymentManager;
import com.dianba.pos.payment.util.AlipayResultUtil;
import com.dianba.pos.payment.util.IPUtil;
import com.dianba.pos.payment.util.ParamUtil;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(BoxURLConstant.PAYMENT)
public class BoxPaymentController {

    private static Logger logger = LogManager.getLogger(BoxPaymentController.class);

    @Autowired
    private BoxItemLabelManager boxItemLabelManager;
    @Autowired
    private BoxOrderManager boxOrderManager;
    @Autowired
    private BoxAppConfig boxAppConfig;
    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private WechatConfig wechatConfig;
    @Autowired
    private WapPaymentManager wapPaymentManager;
    @Autowired
    private PaymentManager paymentManager;
    @Autowired
    private PassportManager passportManager;
    @Autowired
    private LifeOrderManager lifeOrderManager;

    /**
     * 扫码跳转页面-（判定扫码设备，以及微信鉴权）
     */
    @RequestMapping(value = "qr_scan/{passportId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView qrScan(@PathVariable("passportId") String passportId) throws Exception {
        ModelAndView modelAndView = new ModelAndView("payment/auth_code");
        String redirectUri = boxAppConfig.getBoxCallBackHost() + BoxURLConstant.CALLBACK_URL + passportId;
        String params = wechatConfig.getAuthCodeParam(redirectUri);
        modelAndView.addObject("params", params);
        modelAndView.addObject("passportId", passportId);
        modelAndView.addObject("alipay_url", BoxURLConstant.CALLBACK_URL + passportId);
        return modelAndView;
    }

    /**
     * 支付宝跳转/微信授权回调
     */
    @RequestMapping("to_pay/{passportId}")
    public ModelAndView toPay(@PathVariable(name = "passportId") Long passportId
            , String code, String state) throws Exception {
        ModelAndView modelAndView = new ModelAndView("payment/item_list");
        Passport passport = passportManager.findById(passportId);
        String rfids = ScanItemsUtil.getRFIDItems(passportId);
        List<BoxItemVo> boxItemVos = new ArrayList<>();
        if (!StringUtils.isEmpty(rfids)) {
            boxItemVos = boxItemLabelManager.getItemsByRFID(passportId, rfids, true);
        }
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (int i = boxItemVos.size() - 1; i >= 0; i--) {
            totalPrice = totalPrice.add(boxItemVos.get(i).getTotalPrice());
        }
        modelAndView.addObject("passportId", passportId);
        modelAndView.addObject("showName", passport.getShowName());
        modelAndView.addObject("items", boxItemVos);
        modelAndView.addObject("totalPrice", totalPrice);
        modelAndView.addObject("showName", passport.getShowName());
        modelAndView.addObject("paymentType", PaymentTypeEnum.WEIXIN_JS.getKey());
        if (code == null || state == null) {
            modelAndView.addObject("paymentType", PaymentTypeEnum.ALIPAY_JS.getKey());
            //支付宝直接返回
            return modelAndView;
        }
        logger.info("微信回调参数：passportId" + passportId);
        logger.info("微信回调参数：code" + code);
        logger.info("微信回调参数：state" + state);
        logger.info("微信授权回调开始！");
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
        wapPaymentManager.aliPayByOutHtml(response, sequenceNumber
                , boxAppConfig.getBoxCallBackHost() + BoxURLConstant.ALIPAY_RETURN_URL
                , boxAppConfig.getBoxCallBackHost() + BoxURLConstant.ALIPAY_NOTIFY_URL);
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
                    paymentManager.processPaidOrder(sequenceNumber, buyerId, PaymentTypeEnum.ALIPAY_JS
                            , true, false);
                    LifeOrder lifeOrder = lifeOrderManager.getLifeOrder(sequenceNumber, false);
                    boxItemLabelManager.updateItemLabelToPaid(lifeOrder.getRemark());
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
            modelAndView.setViewName("payment/wechat_js_pay");
        } else {
            logger.info(basicResult.getResponse().toJSONString());
            modelAndView.setViewName("payment/pay_error");
        }
        logger.info("微信预支付下单结束!");
        return modelAndView;
    }
}
