package com.dianba.pos.box.config;

public class BoxURLConstant {

    private static final String BASE_URL = "service/";

    public static final String ITEM = BASE_URL + "item/";

    public static final String ORDER = BASE_URL + "order/";

    public static final String PAYMENT = BASE_URL + "payment/";

    public static final String ACCESS = BASE_URL + "access/";

    public static final String ACCOUNT = BASE_URL + "account/";

    public static final String DOOR = BASE_URL + "door/";

    public static final String MAINTENANCE = BASE_URL + "maintenance/";


    //支付宝跳转、微信授权回调地址
    public static final String CALLBACK_URL = PAYMENT + "to_pay/";
    //支付宝跳转、微信授权回调地址
    public static final String ACCOUNT_CALLBACK_URL = ACCOUNT + "authorization/";
    //支付宝同步返回地址
    public static final String ALIPAY_RETURN_URL = PAYMENT + "aliPayReturnUrl";
    //支付宝异步通知地址
    public static final String ALIPAY_NOTIFY_URL = PAYMENT + "aliPayNotifyUrl";
    //微信支付回调地址
    public static final String WETCHAT_PAY_CALL_BACK_URL = PAYMENT + "wechatNotifyUrl";
}
