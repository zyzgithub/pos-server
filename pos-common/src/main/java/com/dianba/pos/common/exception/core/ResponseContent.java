package com.dianba.pos.common.exception.core;

import java.util.HashMap;
import java.util.Map;

public class ResponseContent {
    protected Integer code = Integer.valueOf(AssertCore.G0000.getCode());
    protected String msg = AssertCore.G0000.getMsg();
    protected Map<String, Object> response = new HashMap();

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getResponse() {
        return this.response;
    }

    public void addResponseBody(Object obj) {
        this.response.put("body", obj);
    }

    public void addResponse(String key, Object obj) {
        this.response.put(key, obj);
    }

    public void addAllResponse(Map<String, Object> map) {
        this.response.putAll(map);
    }

    public String toString() {
        return "ResponseContent [code=" + this.code + ", msg=" + this.msg + ", response=" + this.response + "]";
    }
}
