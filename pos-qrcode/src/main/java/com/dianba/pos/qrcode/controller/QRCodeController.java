package com.dianba.pos.qrcode.controller;

import com.dianba.pos.qrcode.config.QRCodeURLConstant;
import com.dianba.pos.qrcode.po.PosQRCode;
import com.dianba.pos.qrcode.service.PosQRCodeManager;
import com.dianba.pos.qrcode.util.QRCodeUtil;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api("商家二维码管理器")
@Controller
@RequestMapping(QRCodeURLConstant.MANAGER)
public class QRCodeController {

    private static Logger logger = LogManager.getLogger(QRCodeController.class);

    @Autowired
    private PosQRCodeManager posQRCodeManager;

    @ApiOperation("通过商家ID下载商家二维码")
    @ApiImplicitParam(name = "passportId", value = "商家ID", required = true)
    @ResponseBody
    @RequestMapping(value = "get_qrcode", method = {RequestMethod.GET})
    public void getQRCodeByPassportId(HttpServletRequest request, HttpServletResponse response
            , Long passportId) throws Exception {
        PosQRCode posQRCode = posQRCodeManager.getQRCodeByMerchantId(passportId);
        QRCodeUtil.putQRCodeInOutPutStrem(posQRCode.getCode(), response);
    }

    @ApiOperation("通过CODE下载商家二维码")
    @ResponseBody
    @RequestMapping(value = "get_qrcode/{code}", method = {RequestMethod.GET})
    public void getQRCodeByCode(HttpServletRequest request, HttpServletResponse response
            , @PathVariable("code") String code) throws Exception {
        PosQRCode posQRCode = posQRCodeManager.getQRCodeByCode(code);
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + code + ".png");
        QRCodeUtil.putQRCodeInOutPutStrem(posQRCode.getCode(), response);
    }

    /**
     * 扫码下单页面
     *
     * @param request
     * @param code
     * @param params
     * @return
     */
    @RequestMapping(value = "qr_order/{type}/{code}", method = RequestMethod.GET)
    public String buildQROrder(HttpServletRequest request
            , @PathVariable("type") String code, @PathVariable("code") String params) {
        logger.info("二维码下单：code:" + code + ",params:" + params);
//        PosQRCode posQRCode = posQRCodeManager.getQRCodeByCode(params);
//        System.out.println(posQRCode.toString());
        //根据code获取商家信息
        return "pay";
    }
}
