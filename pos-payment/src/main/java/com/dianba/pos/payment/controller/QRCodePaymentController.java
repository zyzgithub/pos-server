//package com.dianba.pos.payment.controller;
//
//import com.dianba.pos.order.po.LifeOrder;
//import com.dianba.pos.order.service.LifeOrderManager;
//import com.dianba.pos.passport.po.Passport;
//import com.dianba.pos.passport.service.PassportManager;
//import com.dianba.pos.payment.config.PaymentURLConstant;
//import com.dianba.pos.payment.util.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//@Controller
//@RequestMapping(PaymentURLConstant.QRCODE_ORDER)
//public class QRCodePaymentController {
//
//    private static Logger logger = LogManager.getLogger(QRCodePaymentController.class);
//
//    @Autowired
//    private PassportManager passportManager;
//    @Autowired
//    private LifeOrderManager lifeOrderManager;
//
//    /**
//     * 扫码支付创建订单
//     */
//    @RequestMapping(value = "/createOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    private String createOrder(HttpServletRequest request) {
//        logger.info("扫码创建订单！");
//        Long passportId = 1920561L;
//        Long orderId = 14612687L;
//        String sequenceNumber = "90820170621235450156576933";
//        qcCodeOrderWXPay(orderId, passportId, request);
//        return "";
//    }
//
//    private Map<String, String> qcCodeOrderWXPay(Long orderId, Long passportId, HttpServletRequest request) {
//        Map<String, String> map = new HashMap<String, String>();
//        Passport passport = passportManager.findById(passportId);
//        LifeOrder lifeOrder = lifeOrderManager.getLifeOrder(orderId);
//        //生成微信预付订单
//        BigDecimal amount = BigDecimal.ONE;
//        String title = "1号生活715超市--" + passport.getShowName();
//        String openId = "o3gAGuCwpgafJdg1OQcproBLTZe8";
//        String xml = PayService.createQRCodeJSPackage(title, amount + "", lifeOrder.getSequenceNumber()
//                , openId, IPUtil.getRemoteIp(request), ConfigUtil.QRCode_NOTIFY_URL);
//        xml = "<xml>\n" +
//                "   <appid>wx2421b1c4370ec43b</appid>\n" +
//                "   <attach>支付测试</attach>\n" +
//                "   <body>APP支付测试</body>\n" +
//                "   <mch_id>10000100</mch_id>\n" +
//                "   <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>\n" +
//                "   <notify_url>http://wxpay.wxutil.com/pub_v2/pay/notify.v2.php</notify_url>\n" +
//                "   <out_trade_no>1415659990</out_trade_no>\n" +
//                "   <spbill_create_ip>14.23.150.211</spbill_create_ip>\n" +
//                "   <total_fee>1</total_fee>\n" +
//                "   <trade_type>APP</trade_type>\n" +
//                "   <sign>0CB01533B8C1EF103065174F50BCA001</sign>\n" +
//                "</xml>";
//        logger.info("预付订单提交数据" + xml);
//        String result = CommonUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL, "POST", xml);
//        logger.info("请求预付订单返回数据" + result);
//        try {
//            Map<String, String> resultMap = XMLUtil.doXMLParse(result);
//            //判断prepay_id是否生成
//            if (resultMap.get("return_code").equals("FAIL")) {
//                map.put("state", "fail");
//                map.put("reason", resultMap.get("return_msg"));
//                logger.error("微信支付失败，错误码：" + "------错误信息：" + resultMap.get("return_msg"));
//                return map;
//            } else {
//                if (resultMap.get("result_code").equals("FAIL")) {
//                    String errCode = resultMap.get("err_code");
//                    String errMsg = resultMap.get("err_code_des");
//                    logger.error("微信支付失败，错误码：" + errCode + "------错误信息：" + errMsg);
//                    map.put("state", "fail");
//                    map.put("reason", errMsg);
//                    return map;
//                }
//            }
//
//            logger.info("微信预付订单prepay_id" + resultMap.get("prepay_id"));
//            //封装用户支付订单
//            SortedMap<String, String> params = new TreeMap<String, String>();
//            params.put("appId", ConfigUtil.APPID_KFZ);
//            params.put("timeStamp", System.currentTimeMillis() + "");
//            params.put("nonceStr", PayCommonUtil.createNoncestr());
//            params.put("package", "prepay_id=" + resultMap.get("prepay_id"));
//            params.put("signType", ConfigUtil.SIGN_TYPE);
//
//            // paySign的生成规则和Sign的生成规则一致
//            String paySign = PayCommonUtil.createSign("UTF-8", params, ConfigUtil.API_KEY);
//            params.put("paySign", paySign);
//            // 这里用packageValue是预防package是关键字在js获取值出错
//            params.put("packageValue", "prepay_id=" + resultMap.get("prepay_id"));
//
//            logger.info("生成微信预付订单" + lifeOrder.getSequenceNumber() + "|" + passport.getShowName());
//            params.put("orderId", orderId.toString());
//            params.put("state", "success");
//            params.put("payType", "wx");
//            params.put("saleType", "2");
//            params.put("preScore", amount + "");
//            params.put("qcCodeMoney", amount + "");
//            params.put("merchantId", passport.getId() + "");
//            return params;
//        } catch (Exception e) {
//            e.printStackTrace();
//            map.put("state", "fail");
//            map.put("reason", "订单支付失败");
//        }
//        return map;
//    }
//
////    /**
////     * 扫码支付-微信支付回调
////     *
////     * @param request
////     * @param response
////     * @throws Exception
////     */
////    @RequestMapping("/wxnotify.do")
////    public void wechatPayCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
////        logger.info("-------------扫码支付-微信支付回调--------------");
////        InputStream inStream = request.getInputStream();
////        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
////        byte[] buffer = new byte[1024];
////        int len = 0;
////        while ((len = inStream.read(buffer)) != -1) {
////            outSteam.write(buffer, 0, len);
////        }
////        //获取微信调用我们notify_url的返回信息
////        String result = new String(outSteam.toByteArray(), "utf-8");
////        logger.info("微信支付回调内容:\n{}", result);
////        if (StringUtils.isEmpty(result)) {
////            logger.error("微信支付回调内容为空!!!!");
////            respToWeixin(response, "FAIL", "");
////            return;
////        }
////        Map<String, String> map = XMLUtil.doXMLParse(result);
////        outSteam.close();
////        inStream.close();
////
////        String code = map.get("result_code");
////        logger.info("支付状态:{}", code);
////        if ("SUCCESS".equals(code)) {
////            //获取支付ID 等价订单号
////            String payId = map.get("out_trade_no");
////            logger.info("payId:{}", payId);
////            //获取微信支付交易流水号
////            String outTraceId = map.get("transaction_id");
////            logger.info("outTraceId:{}", outTraceId);
////
////            //修改订单的状态
////            OrderEntity order = orderService.findUniqueByProperty(OrderEntity.class, "payId", payId);
////            if (null != order) {
////                String orderPayState = order.getPayState();
////                String orderOutTraceId = order.getOutTraceId();
////                logger.info("payId:{}, payState:{}", payId, orderPayState);
////                if (OrderStateEnum.UNPAY.getOrderStateEn().equals(orderPayState)) {
////                    order.setPayType(PayEnum.weixinpay.getEn());
////                    order.setOnlineMoney(Integer.valueOf(map.get("total_fee")) / 100.0);
////                    order.setPayTime(DateUtils.getSeconds());
////                    order.setOutTraceId(outTraceId);
////                    payService.updateEntitie(order);
////
////                    payService.orderPayCallback(order);
////
////                    respToWeixin(response, "SUCCESS", "OK");
////                } else {
////                    if (outTraceId.equals(orderOutTraceId)) {
////                        // 告诉微信服务器，我收到信息了，不要再重复回调了
////                        logger.warn("--------------重复通知--------------该订单{}已支付！状态为{}", payId, orderPayState);
////                    } else {
////                        logger.error("该订单{}已支付！状态为{}，但是交易流水号:{}与订单
//// 的交易流水号:{}不一致！", payId, orderPayState, outTraceId, orderOutTraceId);
////                    }
////                    respToWeixin(response, "SUCCESS", "OK");
////                    return;
////                }
////            } else {
////                logger.error("微信支付回调失败，未找到该订单{}", payId);
////                respToWeixin(response, "SUCCESS", "OK");
////                return;
////            }
////        } else {
////            logger.error("支付状态返回异常！！！");
////            respToWeixin(response, "FAIL", "");
////            return;
////        }
////    }
////
////    /**
////     * 扫码支付成功页面跳转
////     *
////     * @param merchantId
////     * @param model
////     * @param request
////     * @param response
////     * @return
////     */
////    @RequestMapping(value = "/qrCodeSuccess", method = RequestMethod.GET)
////    public String qrCodeSuccess(@RequestParam(value = "qcCodeMoney")
//// String qcCodeMoney, Model model, HttpServletRequest request, HttpServletResponse response) {
//////        UserInfo u = getUserInfo(request);
//////        Integer userId = u.getUserId();
//////        logger.info("扫码支付userId==" + userId + "扫码商家成功qcCodeMoney===" + qcCodeMoney);
////        model.addAttribute("qcCodeMoney", qcCodeMoney);
////        return "takeout/qrcodeSuccess";
////    }
//
//    private static void respToWeixin(HttpServletResponse response, String code, String msg) throws IOException {
//        response.getWriter().write("<xml><return_code><![CDATA[" + code
//                + "]]></return_code><return_msg><![CDATA[" + msg
//                + "]]></return_msg></xml>");
//    }
//
//
//}
