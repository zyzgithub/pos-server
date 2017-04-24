package com.wxpay.service;

import com.wxpay.util.CommonUtil;

public class Example {
	 public static void main(String args[]) {
		try {
			WxPayHelper wxPayHelper = new WxPayHelper();
			//先设置基本信息
			wxPayHelper.SetAppId("wxf8b4f85f3a794e77");//商户注册具有支付权限的公众号成功后即可获得;
			wxPayHelper.SetAppKey("2Wozy2aksie1puXUBpWD8oZxiD1DfQuEaiC7KcRATv1Ino3mdopKaPGQQ7TtkNySuAmCaDCrw4xhPY5qKTBl7Fzm0RgR3c0WaVYIXZARsxzHV2x7iwPPzOz94dnwPWSn");//公众号支付请求中用于加密的密钥 Key，可验证商户唯一身份
			wxPayHelper.SetPartnerKey("8934e7d15453e97507ef794cf7b0519d");//财付通商户权限密钥 Key。
			wxPayHelper.SetSignType("sha1");//按照文档中所示填入，目前仅支持 SHA1；
			//设置请求package信息
			wxPayHelper.SetParameter("bank_type", "WX");//固定为"WX"；
			wxPayHelper.SetParameter("body", "test");//商品描述;
			wxPayHelper.SetParameter("partner", "1900000109");//注册时分配的财付通商户号 partnerId;
			wxPayHelper.SetParameter("out_trade_no", CommonUtil.CreateNoncestr());//商户系统内部的订单号，32 个字符内、可包含字母，确保在商户系统唯一；
			wxPayHelper.SetParameter("total_fee", "1");//订单总金额，单位为分
			wxPayHelper.SetParameter("fee_type", "1");//取值：1（人民币） ，暂只支持 1；
			wxPayHelper.SetParameter("notify_url", "http://www.baidu.com");//notify_url在支付完成后，接收微信通知支付结果的 URL,需给绝对路径 ,255字符内,格式如:http://wap.tenpay.com/tenpay.asp;
			wxPayHelper.SetParameter("spbill_create_ip", "127.0.0.1");//指用户浏览器端 IP， 不是商户服务器 IP， 格式为IPV4;
			wxPayHelper.SetParameter("input_charset", "GBK");//取值范围："GBK"、"UTF-8"，默讣："GBK"

			System.out.println("生成app支付package:");
			System.out.println(wxPayHelper.CreateAppPackage("test"));
			System.out.println("生成jsapi支付package:");
			System.out.println(wxPayHelper.CreateBizPackage());
			System.out.println("生成原生支付url:");
			System.out.println(wxPayHelper.CreateNativeUrl("abc"));
			System.out.println("生成原生支付package:");
			System.out.println(wxPayHelper.CreateNativePackage("0", "ok"));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
