package com.wm.service.impl.pay;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BarcodePayResponse implements Serializable{
	
	public final static String WX_SUCCESS = "SUCCESS";
	public final static String WX_FAIL = "FAIL";
	
	public final static String WX_TRADE_STATE_ERROR = "PAYERROR";
	
	public final static String WX_EC_AUTHCODEEXPIRE = "AUTHCODEEXPIRE";			//请扫描微信支付被扫条码/二维码
	public final static String WX_EC_USERPAYING = "USERPAYING";					//需要用户输入支付密码
	
	public static int SUCCESS_CODE = 0;
	
	public static final BarcodePayResponse SUCCESS = new BarcodePayResponse(0, "支付成功");
	public static final BarcodePayResponse FAILURE = new BarcodePayResponse(1, "支付失败");
	private int code;
	private String msg;
	private PlatformBarcodePayResponse response;
	
	public BarcodePayResponse(){
	}
	
	public BarcodePayResponse(int code, String msg){
		this.code = code;
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public PlatformBarcodePayResponse getResponse() {
		return response;
	}

	public void setResponse(PlatformBarcodePayResponse response) {
		this.response = response;
	}
}
