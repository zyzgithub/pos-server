package com.dianba.pos.qrcode.config;

public class QRCodeURLConstant {

    private static final String BASE_URL = "qrcode/";

    public static final String MANAGER = BASE_URL + "manager/";

    //扫码访问地址
    public static final String QRCODE_URL = MANAGER + "qr_scan/";
    //微信授权回调地址
    public static final String WECHAT_CALLBACK_URL = MANAGER + "to_pay/";
}
