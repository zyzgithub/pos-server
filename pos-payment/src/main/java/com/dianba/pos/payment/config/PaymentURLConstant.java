package com.dianba.pos.payment.config;

public class PaymentURLConstant {

    private static final String BASE_URL = "payment/";

    public static final String PAYMENT_ORDER = BASE_URL + "order/";

    public static final String CREDIT_LOAN = BASE_URL + "credit_loan/";

    public static final String QRCODE_ORDER = BASE_URL + "qr_code";

    public static final String WAP = BASE_URL + "wap/";

    public static final String REWARD = BASE_URL + "reward/";

    //支付宝跳转、微信授权回调地址
    public static final String WAP_CALLBACK_URL = WAP + "to_pay/";

    //支付宝同步返回地址
    public static final String WAP_ALIPAY_RETURN_URL = WAP + "aliPayReturnUrl";

    //支付宝异步通知地址
    public static final String WAP_ALIPAY_NOTIFY_URL = WAP + "aliPayNotifyUrl";

    //微信支付回调地址
    public static final String WAP_WETCHAT_PAY_CALL_BACK_URL = WAP + "wechatNotifyUrl";
}
