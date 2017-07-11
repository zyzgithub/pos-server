package com.dianba.pos.box.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.dianba.pos.box.config.BoxAppConfig;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.service.BoxItemLabelManager;
import com.dianba.pos.box.service.BoxOrderManager;
import com.dianba.pos.box.util.ScanItemsUtil;
import com.dianba.pos.box.vo.BoxItemVo;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.passport.po.Passport;
import com.dianba.pos.passport.service.PassportManager;
import com.dianba.pos.payment.config.AlipayConfig;
import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.payment.service.PaymentManager;
import com.dianba.pos.payment.service.WapPaymentManager;
import com.dianba.pos.payment.util.AlipayResultUtil;
import com.dianba.pos.payment.util.ParamUtil;
import com.xlibao.common.constant.payment.PaymentTypeEnum;
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
import java.math.BigDecimal;
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
    @RequestMapping(value = "qr_scan/{passportId}", method = RequestMethod.GET)
    public ModelAndView qrScan(@PathVariable("passportId") String passportId) throws Exception {
        ModelAndView modelAndView = new ModelAndView("auth_code");
//        PosQRCode posQRCode = posQRCodeManager.getQRCodeByCode(code);
//        String authCodeUrl = wechatConfig.getAuthCodeUrl(appConfig.getPosCallBackHost()
//                + PaymentURLConstant.WAP_CALLBACK_URL + posQRCode.getMerchantId());
//        logger.info("扫码牌号：" + posQRCode.getCode() + "扫码对应商家：" + posQRCode.getMerchantId());
//        modelAndView.addObject("url", authCodeUrl);
        modelAndView.addObject("passportId", passportId);
        modelAndView.addObject("pay_url", BoxURLConstant.CALLBACK_URL + passportId);
        return modelAndView;
    }

    /**
     * 支付宝跳转/微信授权回调
     */
    @RequestMapping("to_pay/{passportId}")
    public ModelAndView toPay(@PathVariable(name = "passportId") Long passportId
            , String code, String state) throws Exception {
        ModelAndView modelAndView = new ModelAndView("item_list");
        Passport passport = passportManager.findById(passportId);
//        ScanItemsUtil.writeScanItems(passportId, "E004015073E2E7A8,E004015073E28139");
        String rfids = ScanItemsUtil.getRFIDItems(passportId);
        List<BoxItemVo> boxItemVos = boxItemLabelManager.getItemsByRFID(passportId, rfids, true);
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (int i = boxItemVos.size() - 1; i >= 0; i--) {
            totalPrice = totalPrice.add(boxItemVos.get(i).getTotalPrice());
        }
        modelAndView.addObject("passportId", passportId);
        modelAndView.addObject("showName", passport.getShowName());
        modelAndView.addObject("items", boxItemVos);
        modelAndView.addObject("paymentType", PaymentTypeEnum.ALIPAY.getKey());
        modelAndView.addObject("totalPrice", totalPrice);
//        modelAndView.addObject("showName", passport.getShowName());
//        modelAndView.addObject("paymentType", PaymentTypeEnum.WEIXIN_JS.getKey());
        if (code == null || state == null) {
//            modelAndView.addObject("paymentType", PaymentTypeEnum.ALIPAY.getKey());
            //支付宝直接返回
            return modelAndView;
        }
//        logger.info("微信回调参数：passportId" + passportId);
//        logger.info("微信回调参数：code" + code);
//        logger.info("微信回调参数：state" + state);
//        String rightState = MD5Util.md5(appConfig.getPosCallBackHost()
//                + PaymentURLConstant.WAP_CALLBACK_URL + passportId
//                + wechatConfig.getPublicAppSecrect());
//        logger.info("参数验签：state:" + state);
//        logger.info("自主验签：rightState:" + rightState);
//        if (!rightState.equals(state)) {
//            throw new PosAccessDeniedException("鉴权失败！访问拒绝！");
//        }
//        logger.info("微信授权回调开始！");
//        logger.info(code + " " + state);
//        String authTokenUrl = wechatConfig.getAccessTokenUrl(code);
//        JSONObject jsonObject = HttpUtil.post(authTokenUrl, new JSONObject());
//        if (jsonObject != null) {
//            if (null == jsonObject.get("errcode")) {
//                modelAndView.addObject("access_token", jsonObject.getString("access_token"));
//                modelAndView.addObject("expires_in", jsonObject.getString("expires_in"));
//                modelAndView.addObject("refresh_token", jsonObject.getString("refresh_token"));
//                modelAndView.addObject("openId", jsonObject.getString("openid"));
//                modelAndView.addObject("scope", jsonObject.getString("scope"));
//            } else {
//                throw new PosIllegalArgumentException(jsonObject.toJSONString());
//            }
//        }
//        logger.info("微信授权回调结束！");
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
                    paymentManager.processPaidOrder(sequenceNumber, buyerId, PaymentTypeEnum.ALIPAY
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
}
