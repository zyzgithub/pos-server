package com.dianba.pos.base;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

public class BasicResult {

    private String code;
    private String msg;
    private JSONObject response;

    public boolean isSuccess() {
        if (!StringUtils.isEmpty(code) && code.trim().equals("0")) {
            return true;
        }
        return false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getResponse() {
        return response;
    }

    public void setResponse(JSONObject response) {
        this.response = response;
    }

    public static BasicResult createSuccessResult() {
        BasicResult basicResult = new BasicResult();
        basicResult.setCode("0");
        basicResult.setMsg("请求成功！");
        return basicResult;
    }

    public static BasicResult createFailResult() {
        BasicResult basicResult = new BasicResult();
        basicResult.setCode("1");
        basicResult.setMsg("请求失败！");
        return basicResult;
    }
}
