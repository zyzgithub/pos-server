package com.dianba.pos.base.exception;

public class PosRuntimeException extends RuntimeException{

    public PosRuntimeException() {
    }

    public PosRuntimeException(String message) {
        super(message);
    }

    public PosRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PosRuntimeException(Throwable cause) {
        super(cause);
    }

    public PosRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
