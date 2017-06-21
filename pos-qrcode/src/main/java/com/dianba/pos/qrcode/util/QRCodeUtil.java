package com.dianba.pos.qrcode.util;

import com.dianba.pos.qrcode.controller.QRCodeController;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;

public class QRCodeUtil {

    public static final String QRCODE_DIR = "static/qrcode/";

    public static String createQRCodePic(String code) throws Exception {
        String path = QRCodeController.class.getClassLoader().getResource("").getPath();
        File file = new File(path + QRCODE_DIR + code + ".png");
        if (!file.exists()) {
            file.mkdirs();
            int qrcodeWidth = 300;
            int qrcodeHeight = 300;

            String qrcodeFormat = "png";
            HashMap<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode("http://localhost:8080/pos"
                            + "/qrcode/manager/qr_order/10000/" + code
                    , BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
            MatrixToImageWriter.writeToPath(bitMatrix, qrcodeFormat, file.toPath());
        }
        return "qrcode/" + code + ".png";
    }

    public static void putQRCodeInOutPutStrem(String code, HttpServletResponse response) throws Exception {
        int qrcodeWidth = 300;
        int qrcodeHeight = 300;
        String qrcodeFormat = "png";
        HashMap<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode("http://localhost:8080/pos"
                        + "/qrcode/manager/qr_order/10000/" + code
                , BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, qrcodeFormat, response.getOutputStream());
    }
}
