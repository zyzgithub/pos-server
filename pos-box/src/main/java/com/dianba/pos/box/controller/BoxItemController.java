package com.dianba.pos.box.controller;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.box.config.BoxURLConstant;
import com.dianba.pos.box.service.BoxItemLabelManager;
import com.dianba.pos.qrcode.service.PosQRCodeManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(BoxURLConstant.ITEM)
public class BoxItemController {

    @Autowired
    private BoxItemLabelManager boxItemLabelManager;
    @Autowired
    private PosQRCodeManager posQRCodeManager;

    @ResponseBody
    @RequestMapping("getItemsByRFID")
    public BasicResult getItemsByRFID(HttpServletResponse response, Long passportId, String rfids) {
        if (StringUtils.isEmpty(rfids)) {
            throw new PosIllegalArgumentException("RFIDS不能为空");
        }
        String[] rfidKeys = rfids.split(",");
        List<String> rfidList = Arrays.asList(rfidKeys);
        try {
            String qrcodeFormat = "png";
            HashMap<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode("http://www.baidu.com"
                    , BarcodeFormat.QR_CODE, 300, 300, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, qrcodeFormat, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boxItemLabelManager.getItemsByRFID(passportId, rfidList);
    }
}
