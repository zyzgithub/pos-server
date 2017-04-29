package com.dianba.pos.common.exception.core;

public abstract interface ApplicationException {
    public abstract ExceptionContent getContent();

    public abstract Integer getCode();

    public abstract String getMsg();

    public abstract Object getTag();
}
