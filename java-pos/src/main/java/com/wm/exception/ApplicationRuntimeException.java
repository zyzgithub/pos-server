package com.wm.exception;

/**
 * Created by mjorcen on 16/8/9.
 */
public class ApplicationRuntimeException extends RuntimeException {
    private String msg;
    private Integer code;

    public ApplicationRuntimeException(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
