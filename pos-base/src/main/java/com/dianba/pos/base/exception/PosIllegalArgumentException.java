package com.dianba.pos.base.exception;

public class PosIllegalArgumentException extends IllegalArgumentException{

    public PosIllegalArgumentException() {
        super();
    }

    public PosIllegalArgumentException(String s) {
        super(s);
    }

    public PosIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PosIllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
