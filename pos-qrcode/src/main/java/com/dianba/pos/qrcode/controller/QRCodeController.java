package com.dianba.pos.qrcode.controller;

import com.alibaba.fastjson.JSONObject;
import com.dianba.pos.base.config.AppConfig;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.common.util.HttpUtil;
import com.dianba.pos.order.service.LifeOrderManager;
import com.dianba.pos.order.service.QROrderManager;
import com.dianba.pos.payment.config.WechatConfig;
import com.dianba.pos.qrcode.config.QRCodeURLConstant;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private QROrderManager qrOrderManager;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private WechatConfig wechatConfig;

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
        String url = "http://apptest.0085.com/pos/qrcode/manager/qr_order/10000/6323";
//        String url = "http://192.168.1.115:8080/pos/qrcode/manager/qr_order/10000/6323";

        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(url, BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, qrcodeFormat, response.getOutputStream());
    }

    /**
     * 扫码跳转页面-（判定扫码设备，以及微信鉴权）
     */
    @RequestMapping(value = "qr_order/{type}/{code}", method = RequestMethod.GET)
    public ModelAndView payQROrder(@PathVariable("type") String type, @PathVariable("code") String code)
            throws Exception {
        ModelAndView modelAndView = new ModelAndView("auth_code");
        //TODO 记录state访问uuid,安全校验,查出商家ID
        Long passportId = 100348L;
        UUID uuid = UUID.randomUUID();
        String authCodeUrl = wechatConfig.getAuthCodeUrl(appConfig.getPosWechatCallBackHost()
                + QRCodeURLConstant.WECHAT_CALLBACK_URL + passportId, uuid.toString());
        modelAndView.addObject("url", authCodeUrl);
        modelAndView.addObject("passportId", passportId);
        return modelAndView;
    }

    /**
     * 授权回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "to_pay/{passportId}", method = RequestMethod.GET)
    public ModelAndView toPay(HttpServletRequest request, HttpServletResponse response
            , @PathVariable(name = "passportId") Long passportId) {
        ModelAndView modelAndView = new ModelAndView("pay");
        modelAndView.addObject("passportId", passportId);
        //TODO 校验state是否正确,安全校验
        String code = request.getParameter("code");
        if (code != null) {
            logger.info("微信授权回调开始！");
            String state = request.getParameter("state");
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
        }
        return modelAndView;
    }
}
