package com.dianba.pos.common.exception.lang;

import java.lang.reflect.Method;

public abstract interface Formatter<T> {
    public abstract boolean isFormatter(Class<?> paramClass, Method paramMethod);

    public abstract Object doFailed(T paramT, Method paramMethod, Throwable paramThrowable)
            throws Throwable;

    public abstract Object doSuccessfully(T paramT, Method paramMethod, Object paramObject)
            throws Exception;
}
