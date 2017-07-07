package com.dianba.pos.qrcode.controller;

import com.dianba.pos.qrcode.config.QRCodeURLConstant;
import com.dianba.pos.qrcode.service.PosQRCodeManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api("商家二维码管理器")
@Controller
@RequestMapping(QRCodeURLConstant.MANAGER)
public class QRCodeController {

    private static Logger logger = LogManager.getLogger(QRCodeController.class);

    @Autowired
    private PosQRCodeManager posQRCodeManager;

    @ApiOperation("通过CODE下载商家二维码")
    @RequestMapping(value = "get_qrcode/{code}", method = {RequestMethod.GET})
    public void getQRCodeByCode(HttpServletRequest request, HttpServletResponse response
            , @PathVariable("code") String code) throws Exception {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + code + ".png");
        posQRCodeManager.showQRCodeByCode(code, 300, 300, response);
    }

    @ApiOperation("通过CODE展示商家二维码")
    @RequestMapping(value = "show_qrcode/{code}", method = {RequestMethod.GET})
    public void showQRCodeByCode(HttpServletRequest request, HttpServletResponse response
            , @PathVariable("code") String code) throws Exception {
        posQRCodeManager.showQRCodeByCode(code, 300, 300, response);
    }

    @ApiOperation("通过passportId展示商家二维码")
    @RequestMapping(value = "show_qrcode", method = {RequestMethod.GET, RequestMethod.POST})
    public void showQRCodeByPassportId(HttpServletRequest request, HttpServletResponse response
            , Long passportId) throws Exception {
        posQRCodeManager.showQRCodeByPassportId(passportId, 300, 300, response);
    }
}
