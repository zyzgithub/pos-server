package com.dianba.pos.box.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class BoxPaymentController {

    /**
     * 扫码跳转页面-（判定扫码设备，以及微信鉴权）
     */
    @RequestMapping(value = "qr_scan/{code}", method = RequestMethod.GET)
    public ModelAndView qrScan(@PathVariable("code") String code) throws Exception {
        ModelAndView modelAndView = new ModelAndView("auth_code");
//        PosQRCode posQRCode = posQRCodeManager.getQRCodeByCode(code);
//        String authCodeUrl = wechatConfig.getAuthCodeUrl(appConfig.getPosCallBackHost()
//                + PaymentURLConstant.WAP_CALLBACK_URL + posQRCode.getMerchantId());
//        logger.info("扫码牌号：" + posQRCode.getCode() + "扫码对应商家：" + posQRCode.getMerchantId());
//        modelAndView.addObject("url", authCodeUrl);
//        modelAndView.addObject("passportId", posQRCode.getMerchantId());
//        modelAndView.addObject("pay_url", PaymentURLConstant.WAP_CALLBACK_URL);
        return modelAndView;
    }
}
