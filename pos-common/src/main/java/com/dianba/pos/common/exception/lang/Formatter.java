package com.dianba.pos.common.exception.lang;

import java.lang.reflect.Method;

public interface Formatter<T> {
    boolean isFormatter(Class<?> paramClass, Method paramMethod);

    Object doFailed(T paramT, Method paramMethod, Throwable paramThrowable)
            throws Throwable;

    Object doSuccessfully(T paramT, Method paramMethod, Object paramObject)
            throws Exception;
}
