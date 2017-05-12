package com.dianba.pos.common.exception.core;

public interface ApplicationException {
    ExceptionContent getContent();

    Integer getCode();

    String getMsg();

    Object getTag();
}
