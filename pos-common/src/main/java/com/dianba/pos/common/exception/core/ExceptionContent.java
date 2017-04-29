package com.dianba.pos.common.exception.core;

public class ExceptionContent
        extends ResponseContent {
    public ExceptionContent() {
        this.code = Integer.valueOf(AssertCore.G1000.getCode());
        this.msg = AssertCore.G1000.getMsg();
    }

    public static ExceptionContent init(ApplicationException applicationException) {
        ExceptionContent content = new ExceptionContent();

        content.setCode(applicationException.getCode());
        content.setMsg(applicationException.getMsg());
        if (applicationException.getTag() != null) {
            content.addResponse("codeProperties", applicationException.getTag());
        }
        return content;
    }

    public String toString() {
        return super.toString();
    }
}
