package com.dianba.pos.base.exception;

public class PosAccessDeniedException extends Exception {

    public PosAccessDeniedException() {
    }

    public PosAccessDeniedException(String message) {
        super(message);
    }

    public PosAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PosAccessDeniedException(Throwable cause) {
        super(cause);
    }

    public PosAccessDeniedException(String message, Throwable cause
            , boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
