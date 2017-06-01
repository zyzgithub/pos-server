package com.dianba.pos.payment.pojo;

import java.io.Serializable;

public class BarcodePayResponse implements Serializable {

    public static final String WX_SUCCESS = "SUCCESS";
    public static final String WX_FAIL = "FAIL";

    public static final String WX_TRADE_STATE_ERROR = "PAYERROR";

    public static final String WX_EC_AUTHCODEEXPIRE = "AUTHCODEEXPIRE";            //请扫描微信支付被扫条码/二维码
    public static final String WX_EC_USERPAYING = "USERPAYING";                    //需要用户输入支付密码

    public static final int SUCCESS_CODE = 0;

    public static final BarcodePayResponse SUCCESS = new BarcodePayResponse(0, "支付成功");
    public static final BarcodePayResponse FAILURE = new BarcodePayResponse(1, "支付失败");
    private int code;
    private String msg;
    private PlatformBarcodePayResponse response;

    public static BarcodePayResponse createSuccessResult() {
        BarcodePayResponse barcodePayResponse = new BarcodePayResponse();
        barcodePayResponse.setCode(0);
        barcodePayResponse.setMsg("支付成功！");
        return barcodePayResponse;
    }

    public BarcodePayResponse() {

    }

    public BarcodePayResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean isSuccess() {
        if (code == 0) {
            return true;
        }
        return false;
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
