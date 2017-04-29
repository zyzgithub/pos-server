package com.dianba.pos.common.exception.util;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

public class GenericsUtils {
    public static Method getMethodPermission(Class<?> clazz, Method joinMethod)
            throws Exception {
        Method method = clazz.getMethod(joinMethod.getName(), joinMethod.getParameterTypes());
        if (method.isAnnotationPresent(RequestMapping.class)) {
            return method;
        }
        return null;
    }
}
