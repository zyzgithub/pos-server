package com.dianba.pos.common.exception.core;

import org.springframework.util.StringUtils;

public abstract class AbstractApplicationRuntimeException
        extends RuntimeException
        implements ApplicationException {
    private static final long serialVersionUID = -4762773641307470201L;
    protected Integer code;
    protected String msg;
    protected Object tag;

    public AbstractApplicationRuntimeException(AssertCore coreEnum, String msg) {
        this(Integer.valueOf(coreEnum.getCode()), StringUtils.isEmpty(msg) ? coreEnum.getMsg() : msg, null);
    }

    protected AbstractApplicationRuntimeException(Integer code, String msg) {
        this(code, msg, null);
    }

    protected AbstractApplicationRuntimeException(Integer code, String msg, Object tag) {
        this.code = code;
        this.tag = tag;
        this.msg = msg;
    }

    public ExceptionContent getContent() {
        return ExceptionContent.init(this);
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public Object getTag() {
        return this.tag;
    }
}
