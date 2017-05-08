package com.dianba.pos.order.exception;

public class BusinessException extends Exception{

    private int errCode;
    public BusinessException(){

    }

    public BusinessException(int errCode, String msg){
        super(msg);
        this.errCode = errCode;
    }

    public BusinessException(int errCode, String msg, Throwable cause){
        super(msg, cause);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
