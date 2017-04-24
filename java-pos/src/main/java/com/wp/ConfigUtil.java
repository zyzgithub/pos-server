package com.wp;

import com.base.config.EnvConfig;


public class ConfigUtil {

    // 微付通支付-退款接口
    public final static String PAY_REFUND_URL_WFT = EnvConfig.base.DOMAIN_PAY + "/pay/pay/weixin/giveUpABill.action";

    // 珀金动态二维码支付
    public final static String PAY_REFUND_URL_DPT = EnvConfig.base.DOMAIN_PAY + "/pay/pay/ptPay/wxDPrepay.action";

    //点吧外卖服务号的APPID
    public final static String APPID = EnvConfig.wechat.APPID;
    //点吧外卖服务号的应用密码
    public final static String APP_SECRECT = EnvConfig.wechat.APP_SECRECT;
    //点吧外卖服务号的商户号
    public final static String MCH_ID = EnvConfig.wechat.MCH_ID;

    //点吧外卖服务号的APPID
    public final static String APPID_KFZ = EnvConfig.wechat.APPID_KFZ;
    //点吧外卖服务号的应用密码
    public final static String APP_SECRECT_KFZ = EnvConfig.wechat.APP_SECRECT_KFZ;
    //点吧外卖服务号的商户号
    public final static String MCH_ID_KFZ = EnvConfig.wechat.MCH_ID_KFZ;

    //API密钥，在公众平台上设置好api证书好，自己设置的密码
    public final static String API_KEY = EnvConfig.wechat.API_KEY;
    
	//威富通微信公众号广告url
    public final static String WFT_ADVURL = EnvConfig.wechat.WFT_ADVURL;
 
    //威富通微信公众号广告渠道ID
    public final static String WFT_DISTRIBUTIONID = EnvConfig.wechat.WFT_DISTRIBUTIONID;

    
    
    //快递员APP微信开发平台应用id
    public final static String KDY_APP_ID = "wx897009dbd783cdf6";
    //快递员APP商户号
    public final static String KDY_MCH_ID = "1236613202";
    //快递员APPAPI密钥
    public final static String KDY_API_KEY = "0085abcdefghijklmnopqrstuvwxyz00";

    //商家APP微信开发平台应用id
    public final static String MERCHANT_APP_ID = "wxe1be9a82ebe8dab0";
  //商家APP商户号
    public final static String MERCHANT_MCH_ID = "1306108301";
    //商家APPAPI密钥
    public final static String MERCHANT_API_KEY = "0085abcdefghijklmnopqrstuvwxyz00";

    //商家APP微信开发平台应用id - appStore
    public final static String APP_STORE_MERCHANT_APP_ID = "wx0998bcb4ca6ec794";
    //商家APP商户号
    public final static String APP_STORE_MERCHANT_MCH_ID = "1362885202";
    //商家APPAPI密钥
    public final static String APP_STORE_MERCHANT_API_KEY = "64c9d7c08f8f11e6be385cf938a05dd2";
    
    
    //////////////////////////////            为了解决账期问题新开的支付账号              ////////////////////////////////////////
    //商家APP微信开发平台应用id
    public final static String MERCHANT_APP_ID_FIR = "wx31044be9b94adb2a";
    //商家APP商户号
    public final static String MERCHANT_MCH_ID_FIR = "1404597702";
    //商家APPAPI密钥
    public final static String MERCHANT_API_KEY_FIR = "NSMeriAx7xWZiASvAvuSuZpLrYxPd23p";

    
    
    //签名加密方式
    public final static String SIGN_TYPE = "MD5";
    //微信支付统一接口的回调action,支付成功后，会自动回调，不能带问号,在此方法在 OrderPayAlipayController中
    public final static String NOTIFY_URL = EnvConfig.base.DOMAIN + "/takeOutController/wxnotify.do";
    //微信支付付成功支后跳转的地址，手动跳转
    public final static String SUCCESS_URL = EnvConfig.base.DOMAIN + "/takeOutController.do?payresult";

    /**
     * 1.5wap界面地址，需要进行微信重定向地址来访问,否则直接访问拿不到微信用户的openId
     **/
    //wap首页(微信授权跳转)
    public final static String INDEX_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?merchantList";
    //店铺列表
    public final static String MERCHANTLIST_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?merchantListAfterAuth";
    //店铺菜单列表
    public final static String MENULIST_URI = EnvConfig.base.DOMAIN + "/takeOutController/menu/%s.do";

    public final static String MENULIST_URI_AUTH = EnvConfig.base.DOMAIN + "/takeOutController/menu.do?merchantId=%s";
    //订单列表
    public final static String ORDER_LIST_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?orderListAfterAuth";
    //订单列表(微信授权跳转)
    public final static String ORDER_LIST_URL = EnvConfig.base.DOMAIN + "/takeOutController/orderlist.do";
    //订单详情页面(其中%s为订单id 的占位符)
    public final static String ORDER_DETAIL_URL = EnvConfig.base.DOMAIN + "/takeOutController/%sorderdetail.do";
    //我的个人信息
    public final static String SELF_INFO_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?selfInfoAfterAuth";
    //我的优惠券
    public final static String MYCARD_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?myCardAfterAuth";

    public final static String MYCOUPONS_URI = EnvConfig.base.DOMAIN + "/coupons/mylist.do";
    //菜品详情
    public final static String MENU_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?menuAfterAuth&menuId=MENU_ID";
    //进入信步游戏之前授权跳转地址
    public final static String GAME_URI = EnvConfig.base.DOMAIN + "/takeOutController/gameAfterAuth.do?gameSecret=GAME_SECRET&gameOpenId=GAME_OPENID";
    //绑定手机号地址
    public final static String SIGNMOBILE_URI = EnvConfig.base.DOMAIN + "/takeOutController/signmobileAfter.do";
    //我的
    public final static String MINE_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?mineAfterAuth";

    public final static String MINE_URL = EnvConfig.base.DOMAIN + "/takeOutController.do?mine";
    //我的帐号信息
    public final static String MINE_ACCOUNT_INFO_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?mineAccountInfoAfterAuth";
    //我的收藏信息
    public final static String MINE_FAVORITES_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?mineFavoritesAfterAuth";
    //我的-绑定手机号
    public final static String MINE_MOBILE_BIND_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?mineMobileBindAfterAuth";
    //我的-地址列表
    public final static String MINE_ADDREESSES_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?mineAddressesAfterAuth";
    //我的-待付款
    public final static String MINE_WAITING_PAY_URI = EnvConfig.base.DOMAIN + "/takeOutController/mineWaitingPayAfterAut.do";
    //我的-待收货
    public final static String MINE_WAITING_RECEIPT_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?mineWaitingReceiptAfterAuth";
    //我的-待评价
    public final static String MINE_WAITING_EVALUATES_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?mineWaitingEvaluatesAfterAuth";
    //我的-退款单
    public final static String MINE_REFUNDS_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?mineRefundsAfterAuth";
    //我的-充值
    public final static String MINE_RECHARGE_URI = EnvConfig.base.DOMAIN + "/takeOutController.do?mineRechargeAfterAuth";
    //微信支付充值回调action,支付成功后，会自动回调，不能带问号,在此方法在 WeixinPayController中
    public final static String RECHARGE_NOTIFY_URL = EnvConfig.base.DOMAIN + "/takeOutController/weixinPay/rechargeNotify.do";
    //分享红包
    public final static String SHARE_COUPONS_URL = EnvConfig.base.DOMAIN + "/coupons/share.do?score=";
    // 扫码支付页面
    public final static String QRCode_URL = EnvConfig.base.DOMAIN + "/weixin/store/qrCode.do?merchantId=";
    // 扫码支付回调地址
    public final static String QRCode_NOTIFY_URL = EnvConfig.base.DOMAIN + "/weixin/store/wxnotify.do";
    
    // 先锋-代付成功回调地址
    public final static String UCF_NOTICE_URL = EnvConfig.base.DOMAIN + "/takeOutController/ucf/withdrawNotice.do";

    /**
     * 1.8 项目接口
     **/
    // 1.8项目首页
    public final static String WEIXIN_HOME = EnvConfig.base.DOMAIN18 + "/weixin/home";
    // 订单详情
    public final static String ORDER_DETAIL = EnvConfig.base.DOMAIN18 + "/weixin/store/orderDetail?orderId=ORDER_ID";
    public final static String EATIN_ORDER_DETAIL = EnvConfig.base.DOMAIN18 + "/weixin/store/eatInOrderDetail?orderId=%s";
    // 扫码支付页面
    public final static String QRCODE_URL = EnvConfig.base.DOMAIN18 + "/weixin/store/qrCode?merchantId=MERCHANT_ID";
    // 菜品页
    public final static String MENU_HOME = EnvConfig.base.DOMAIN18 + "/weixin/store/menuTypeList?merchantId=MERCHANT_ID&menuId=MENU_ID";
    // 商家首页
    public final static String MERCHANT_HOME = EnvConfig.base.DOMAIN18 + "/weixin/store/menuTypeList?merchantId=MERCHANT_ID";
    // 第三方-天生玩家
    public final static String THIRD_TSWJ = EnvConfig.base.DOMAIN18 + "/open/order/to_pay/tswj?order_id=";
    // 第三方-订单详情
    public final static String THIRD_ORDER_DETAIL = EnvConfig.base.DOMAIN18 + "/open/order/detail/THIRD_PLAT/ORDER_ID";
    // 快递员扫码推广二维码
    public final static String COURIER_SCAN_CAMPAIGN_RECHARGE_URL = EnvConfig.base.DOMAIN18 + "/weixin/store/recharge?wellDiscountType=P&courierId=";

    //1）被扫支付API(商家收款)
    public static final String PLATFORM_BARCODE_PAY_URL = EnvConfig.base.USER_APP + "/userdomain/scanOrder/barcodePay";

    /**
     * 微信基础接口地址
     */
    //获取code接口
    public final static String OAUTH2_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
    //oauth2授权接口(GET)
    public final static String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //刷新access_token接口（GET）
    public final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
    // 菜单创建接口（POST）
    public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    // 菜单查询（GET）
    public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    // 菜单删除（GET）
    public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public final static String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    /**
     * 微信支付接口地址
     */
    //微信支付统一接口(POST)
    public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信退款接口(POST)
    public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //订单查询接口(POST)
    public final static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    //关闭订单接口(POST)
    public final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    //退款查询接口(POST)
    public final static String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    //对账单接口(POST)
    public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    //短链接转换接口(POST)
    public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
    //接口调用上报接口(POST)
    public final static String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";


    public static boolean isTest = EnvConfig.base.ISTEST;
    public static boolean isTestThird = EnvConfig.base.ISTEST_THIRD;
    //isDebug为true时即模拟的登录的userId
    public static Integer testUserId = Integer.parseInt(EnvConfig.base.TEST_USERID);

    public static String TOKEN_DES_KEY = "yihaowaimai_123456_!@#$%^";//加密token key

    //	//1）被扫支付API(商家收款)
//	public static final String PLATFORM_BARCODE_PAY_URL = EnvConfig.base.USER_APP + "/userdomain/scanOrder/barcodePay";
    //2）被扫支付查询API
    public static final String PLATFORM_BARCODE_QUERY_URL = EnvConfig.base.USER_APP + "/userdomain/scanOrder/barcodePayState?barcodeNumber=";
    //3) 被扫支付API(超市POS)
    public static final String POS_PLATFORM_BARCODE_PAY_URL = EnvConfig.base.USER_APP + "/userdomain/scanOrder/supermarketBarcodePay";

}
