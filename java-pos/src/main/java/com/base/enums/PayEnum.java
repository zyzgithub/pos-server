package com.base.enums;

public enum PayEnum {

	unionpay("unionpay", "银联"), 
	tenpay("tenpay", "财付通"), 
	weixinpay("weixinpay", "微信支付"),
	wft_pay("wft_pay", "微付通支付"), 
	ucf_pay("xf_pay", "先锋支付"), 
	balance("balance", "余额支付"), 
	alipay("alipay", "支付宝支付"), 
	merchantpay("merchantpay","商家会员支付"),
	supermarkt_cash("supermarkt_cash", "现金支付"),
	supermarkt_alibarcode("supermarkt_alibarcode", "超市支付宝支付"),
	supermarkt_wxbarcode("supermarkt_wxbarcode", "超市微信支付"),
	merchant_banlance_pay("merchant_banlance_pay", "商家余额支付"),
	merchant_account_banlance_pay("merchant_account_banlance_pay", "商家账户余额支付");

	private String en;
	private String cn;

	private PayEnum(String en, String cn) {
		this.en = en;
		this.cn = cn;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public static String getCn(String en) {
		PayEnum pay = valueOf(en);
		if(null != pay)
			return pay.getCn();
		return "";
	}
}
