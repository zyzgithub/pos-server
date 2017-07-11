package com.dianba.pos.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.order.po.LifeOrder;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.order.service.QROrderManager;
import com.dianba.pos.payment.config.AlipayConfig;
import com.dianba.pos.payment.pojo.BizContent;
import com.dianba.pos.payment.service.WapPaymentManager;
import com.dianba.pos.payment.service.WeChatPayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class DefaultWapPaymentManager implements WapPaymentManager {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private AlipayConfig alipayConfig;
    @Autowired
    private QROrderManager qrOrderManager;
    @Autowired
    private WeChatPayManager weChatPayManager;
    @Autowired
    private LifeOrderManager lifeOrderManager;

    @Override
    public BasicResult wechatPay(String sequenceNumber, String spBillCreateIP) throws Exception {
        LifeOrder lifeOrder = lifeOrderManager.getLifeOrder(sequenceNumber, false);
        if (lifeOrder != null) {
            return weChatPayManager.jsPayment(lifeOrder, lifeOrder.getReceiptUserId(), "WEP", spBillCreateIP);
        }
        return BasicResult.createFailResult("订单不存在！");
    }

    @Override
    public void aliPayByOutHtml(HttpServletResponse response, String sequenceNumber
            , String returnUrl, String notifyUrl) throws Exception {
        LifeOrder lifeOrder = lifeOrderManager.getLifeOrder(sequenceNumber, true);
        if (lifeOrder != null) {
            AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getUrl()
                    , alipayConfig.getAppid(), alipayConfig.getRsaRrivateKey(), "json", AlipayConfig.CHARSET
                    , alipayConfig.getAlipayPublicKey());
            String body = "1号生活715超市--" + lifeOrder.getShippingNickName();
            String passbackParams = "1";
            BizContent content = new BizContent();
            content.setBody(body);
            content.setOutTradeNo(sequenceNumber);
            content.setPassbackParams(passbackParams);
            content.setSubject(body);
            content.setTotalAmount(lifeOrder.getTotalPrice() + "");
            content.setProductCode("QUICK_WAP_PAY");
            try {
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
                //在公共参数中设置回跳和通知地址
                alipayRequest.setReturnUrl(returnUrl);
                alipayRequest.setNotifyUrl(notifyUrl);
                //参数参考 https://doc.open.alipay.com/doc2/detail.htm?treeId=203&articleId=105463&docType=1#s0
                alipayRequest.setBizContent(JSON.toJSONString(content));//填充业务参数
                String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
                response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
                response.getWriter().write(form);//直接将完整的表单html输出到页面
                response.getWriter().flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new PosNullPointerException("订单不存在!");
        }
    }
}
