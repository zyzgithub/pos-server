package com.dianba.pos.qrcode.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.payment.util.MD5Util;
import com.dianba.pos.qrcode.config.QRCodeURLConstant;
import com.dianba.pos.qrcode.config.QRWeChatConfig;
import com.dianba.pos.qrcode.config.WeChatURLConstant;
import com.dianba.pos.qrcode.po.PosQRCode;
import com.dianba.pos.qrcode.service.PosQRCodeManager;
import com.dianba.pos.qrcode.util.QRCodeUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.UUID;

@Api("商家二维码管理器")
@Controller
@RequestMapping(QRCodeURLConstant.MANAGER)
public class QRCodeController {

    private static Logger logger = LogManager.getLogger(QRCodeController.class);

    @Autowired
    private PosQRCodeManager posQRCodeManager;
    @Autowired
    private LifeOrderManager lifeOrderManager;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private QRWeChatConfig qrWeChatConfig;

    @ApiOperation("通过商家ID下载商家二维码")
    @ApiImplicitParam(name = "passportId", value = "商家ID", required = true)
    @RequestMapping(value = "get_qrcode", method = {RequestMethod.GET})
    public void getQRCodeByPassportId(HttpServletRequest request, HttpServletResponse response
            , Long passportId) throws Exception {
        PosQRCode posQRCode = posQRCodeManager.getQRCodeByMerchantId(passportId);
        QRCodeUtil.putQRCodeInOutPutStrem(posQRCode.getCode(), response);
    }

    @ApiOperation("通过CODE下载商家二维码")
    @RequestMapping(value = "get_qrcode/{code}", method = {RequestMethod.GET})
    public void getQRCodeByCode(HttpServletRequest request, HttpServletResponse response
            , @PathVariable("code") String code) throws Exception {
        PosQRCode posQRCode = posQRCodeManager.getQRCodeByCode(code);
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + code + ".png");
        QRCodeUtil.putQRCodeInOutPutStrem(posQRCode.getCode(), response);
    }

    @RequestMapping(value = "show_qrcode/{code}", method = {RequestMethod.GET})
    public void showQRCodeByCode(HttpServletRequest request, HttpServletResponse response
            , @PathVariable("code") String code) throws Exception {
//        PosQRCode posQRCode = posQRCodeManager.getQRCodeByCode(code);
//        QRCodeUtil.putQRCodeInOutPutStrem(posQRCode.getCode(), response);
        int qrcodeWidth = 300;
        int qrcodeHeight = 300;
        String qrcodeFormat = "png";
        HashMap<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode("http://192.168.3.13:8080/pos/qrcode/manager/qr_order/10000/6323"
                        , BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, qrcodeFormat, response.getOutputStream());
    }

    /**
     * 扫码跳转页面-（判定扫码设备，以及微信鉴权）
     */
    @RequestMapping(value = "qr_order/{type}/{code}", method = RequestMethod.GET)
    public String payQROrder(Model model, @PathVariable("type") String type, @PathVariable("code") String code)
            throws Exception {
        //TODO 记录state访问uuid,安全校验
        UUID uuid = UUID.randomUUID();
        String authCodeUrl = WeChatURLConstant.getAuthCodeUrl(qrWeChatConfig.getAppId()
                , appConfig.getPosWechatCallBackHost() + QRCodeURLConstant.WECHAT_CALLBACK_URL
                , uuid.toString());
        model.addAttribute("url", authCodeUrl);
        return "auth_code";
    }

    /**
     * 授权回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("to_pay")
    public String toPay(HttpServletRequest request, HttpServletResponse response
            , Model model) {
        //TODO 校验state是否正确,安全校验
        String code = request.getParameter("code");
        if (code != null) {
            logger.info("微信授权回调开始！");
            String state = request.getParameter("state");
            logger.info(code + " " + state);
            String authTokenUrl = WeChatURLConstant.getAuthTokenUrl(qrWeChatConfig.getAppId()
                    , qrWeChatConfig.getSecrectKey(), code);
            JSONObject jsonObject = HttpUtil.post(authTokenUrl, new JSONObject());
            if (jsonObject != null) {
                if (null == jsonObject.get("errcode")) {
                    model.addAttribute("access_token", jsonObject.getString("access_token"));
                    model.addAttribute("expires_in", jsonObject.getString("expires_in"));
                    model.addAttribute("refresh_token", jsonObject.getString("refresh_token"));
                    model.addAttribute("openid", jsonObject.getString("openid"));
                    model.addAttribute("scope", jsonObject.getString("scope"));
                } else {
                    throw new PosIllegalArgumentException(jsonObject.toJSONString());
                }
            }
            logger.info("微信授权回调结束！");
        }
        return "pay";
    }

    /**
     * 扫码下单支付
     *
     * @return
     */
    @RequestMapping(value = "pay_order", method = RequestMethod.GET)
    public void payQROrder(HttpServletRequest request
            , String scope) {

    }

    private static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        String appSecret = MD5Util.md5("dianba0085tan0085");
        String code = "081WAZo022TM1Y07z9r02r4Ro02WAZoK";
        String authTokenUrl = WeChatURLConstant.getAuthTokenUrl("wxdcc86bc0e6e451a6"
                , appSecret, code);
//        c6a10ab01fa29ef6f55e26c6d3f750ba
//        710d0f94e79c0999c5b497ef240d7d4b
//        0085abcdefghijklmnopqrstuvwxyz00
        JSONObject jsonObject = HttpUtil.post(authTokenUrl, new JSONObject());
        if (jsonObject != null) {
            for (String key : jsonObject.keySet()) {
                System.out.println("key:" + key + "  vlaue:" + jsonObject.get(key));
            }
        }
    }
}
