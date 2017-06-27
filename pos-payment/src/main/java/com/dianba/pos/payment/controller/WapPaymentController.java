package com.dianba.pos.payment.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.dianba.pos.payment.config.AlipayConfig;
import com.dianba.pos.payment.config.PaymentURLConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(PaymentURLConstant.WAP)
public class WapPaymentController {

    @Autowired
    private AlipayConfig alipayConfig;

    @RequestMapping("pay")
    public void payAndGenerateOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String orderNum = "09876543456789";
        // 订单名称，必填
        String subject = "sdsa";
        // 付款金额，必填
        String totalAmount = "0.01";
        // 商品描述，可空
        String body = "xxx";
        // 超时时间 可空
        String timeoutExpress = "2m";
        // 销售产品码 必填
        String productCode = "QUICK_WAP_PAY";
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(alipayConfig.getUrl(), alipayConfig.getAppid()
                , alipayConfig.getRsaRrivateKey(), AlipayConfig.FORMAT
                , AlipayConfig.CHARSET, alipayConfig.getAlipayPublicKey(), AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(orderNum);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setBody(body);
        model.setTimeoutExpress(timeoutExpress);
        model.setProductCode(productCode);
        alipayRequest.setBizModel(model);
        // 设置异步通知地址
        alipayRequest.setNotifyUrl(alipayConfig.getNotifyUrl());
        // 设置同步地址
        alipayRequest.setReturnUrl(alipayConfig.getReturnUrl());

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = client.pageExecute(alipayRequest).getBody();
            response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
