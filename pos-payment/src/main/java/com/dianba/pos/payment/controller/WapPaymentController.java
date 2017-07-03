package com.dianba.pos.payment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.base.exception.PosAccessDeniedException;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.order.service.QROrderManager;
import com.dianba.pos.payment.config.AlipayConfig;
import com.dianba.pos.payment.config.PaymentURLConstant;
import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.payment.pojo.BizContent;
import com.dianba.pos.payment.service.WapPaymentManager;
import com.dianba.pos.payment.util.IPUtil;
import com.dianba.pos.payment.util.MD5Util;
import com.dianba.pos.payment.util.OrderInfoUtil;
import com.dianba.pos.payment.xmlbean.WechatReturnXml;
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
import java.io.IOException;
import java.io.PrintWriter;
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
    private AppConfig appConfig;

    /**
     * 扫码跳转页面-（判定扫码设备，以及微信鉴权）
     */
    @RequestMapping(value = "qr_scan/{code}", method = RequestMethod.GET)
    public ModelAndView qrScan(@PathVariable("code") String code)
            throws Exception {
        ModelAndView modelAndView = new ModelAndView("auth_code");
        PosQRCode posQRCode = posQRCodeManager.getQRCodeByCode(code);
        String authCodeUrl = wechatConfig.getAuthCodeUrl(appConfig.getPosCallBackHost()
                + PaymentURLConstant.WAP_CALLBACK_URL + posQRCode.getMerchantId());
        logger.info("二维码访问地址：" + authCodeUrl);
        modelAndView.addObject("url", authCodeUrl);
        modelAndView.addObject("passportId", posQRCode.getMerchantId());
        modelAndView.addObject("pay_url", PaymentURLConstant.WAP_CALLBACK_URL);
        return modelAndView;
    }

    /**
     * 支付宝跳转/微信授权回调
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


    @RequestMapping("alipay/{sequenceNumber}")
    public void aliPay(HttpServletRequest request, HttpServletResponse response
            , @PathVariable(name = "sequenceNumber") String sequenceNumber) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do"
                , alipayConfig.getAppid(), alipayConfig.getRsaRrivateKey(), "json", AlipayConfig.CHARSET
                , alipayConfig.getAlipayPublicKey());
//        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
//        alipayRequest.setReturnUrl("http://apptest.0085.com/pos/qrcode/manager/show_qrcode/EWM00000051");
//        alipayRequest.setNotifyUrl("http://apptest.0085.com/pos/qrcode/manager/show_qrcode/EWM00000051");
//        alipayRequest.setBizContent("{" +
//                "    \"out_trade_no\":\"98765456787654332\"," +
//                "    \"total_amount\":0.01," +
//                "    \"subject\":\"Iphone6 16G\"," +
//                "    \"seller_id\":\"1212121\"," +
//                "    \"product_code\":\"QUICK_WAP_PAY\"" +
//                "  }");//填充业务参数
//        String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
//        response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
//        response.getWriter().write(form);//直接将完整的表单html输出到页面
//        response.getWriter().flush();
        String body = "我是测试数据";
        String subject = "Javen 测试";
        String totalAmount = "0.01";
        String passbackParams = "1";

        BizContent content = new BizContent();
        content.setBody(body);
        content.setOutTradeNo(OrderInfoUtil.getOutTradeNo());
        content.setPassbackParams(passbackParams);
        content.setSubject(subject);
        content.setTotalAmount(totalAmount);
        content.setProductCode("QUICK_WAP_PAY");
        try {
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
            //在公共参数中设置回跳和通知地址
            alipayRequest.setReturnUrl(appConfig.getPosCallBackHost() + PaymentURLConstant.WAP_ALIPAY_RETURN_URL);
            alipayRequest.setNotifyUrl(appConfig.getPosCallBackHost() + PaymentURLConstant.WAP_ALIPAY_NOTIFY_URL);
            //参数参考 https://doc.open.alipay.com/doc2/detail.htm?treeId=203&articleId=105463&docType=1#s0
            System.out.println(JSON.toJSONString(content));
            alipayRequest.setBizContent(JSON.toJSONString(content));//填充业务参数
            String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("aliPayReturnUrl")
    public void aliPayReturnUrl(HttpServletRequest request, HttpServletResponse response) {
        String text;
        try {
            // 获取支付宝GET过来反馈信息
            Map<String, String> map = request.getParameterMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            boolean verifyResult = AlipaySignature.rsaCheckV1(map, alipayConfig.getAlipayPublicKey()
                    , AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
            if (verifyResult) {// 验证成功
                // TODO 请在这里加上商户的业务逻辑程序代码
                System.out.println("return_url 验证成功");
                text = "success";
            } else {
                System.out.println("return_url 验证失败");
                // TODO
                text = "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            text = "failure";
        }
        PrintWriter writer = null;
        try {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setContentType("text/plain");
            response.setCharacterEncoding(AlipayConfig.CHARSET);
            writer = response.getWriter();
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }

        }
    }

    @RequestMapping("aliPayNotifyUrl")
    public void aliPayNotifyUrl(HttpServletRequest request, HttpServletResponse response) {
        String text = "";
        try {
            Map<String, String> paramsMap = request.getParameterMap();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                System.out.println(entry.getKey() + "--->" + entry.getValue());
            }
            System.out.println("alipayPulicKey>" + alipayConfig.getAlipayPublicKey());
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, alipayConfig.getAlipayPublicKey()
                    , AlipayConfig.CHARSET); //调用SDK验证签名
            if (signVerified) {
                // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校
                // 验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
                text = "success";
            } else {
                // TODO 验签失败则记录异常日志，并在response中返回failure.
                text = "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        PrintWriter writer = null;
        try {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setContentType("text/plain");
            response.setCharacterEncoding(AlipayConfig.CHARSET);
            writer = response.getWriter();
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }

        }
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
        return modelAndView;
    }

    /**
     * 微信JS支付回调
     *
     * @param request
     * @param sequenceNumber
     * @return
     */
    @RequestMapping("wechatNotifyUrl/{sequenceNumber}")
    public WechatReturnXml notifyUrl(HttpServletRequest request
            , @PathVariable(name = "sequenceNumber") String sequenceNumber) {
        logger.info("开始接收微信支付回调消息：" + sequenceNumber);
        for (Object key : request.getParameterMap().keySet()) {
            logger.info("key=" + key + " value=" + request.getParameter(key.toString()));
        }
        logger.info("结束接收！");
        return WechatReturnXml.createSuccessReturn();
    }
}
