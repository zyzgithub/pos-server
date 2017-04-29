package com.dianba.pos.common.exception.util;

import com.dianba.pos.common.exception.core.ApplicationException;
import com.dianba.pos.common.exception.core.ExceptionContent;
import com.dianba.pos.common.exception.lang.AbstractApiController;
import com.dianba.pos.common.exception.lang.Formatter;
import com.dianba.pos.common.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

public class VoidFormatResultUtil
        implements Formatter<AbstractApiController> {
    protected final Log log = LogFactory.getLog(getClass());

    private static Object getResult(AbstractApiController obj, Class<?> returnType, Object returnValue) {
        ThreadContent.println(JsonUtil.toJsonString(returnValue));

        return returnValue;
    }

    private static boolean isVoid(Class<?> returnType) {
        return "void".equalsIgnoreCase(returnType.getName());
    }

    public boolean isFormatter(Class<?> clazz, Method joinMethod) {
        try {
            if (!AbstractApiController.class.isAssignableFrom(clazz)) {
                return false;
            }
            Method method = GenericsUtils.getMethodPermission(clazz, joinMethod);
            return (method != null) && (isVoid(joinMethod.getReturnType()));
        } catch (Exception e) {
        }
        return false;
    }

    public Object doSuccessfully(AbstractApiController obj, Method joinMethod, Object returnValue)
            throws Exception {
        returnValue = obj.getResponseContent();

        return getResult(obj, joinMethod.getReturnType(), returnValue);
    }

    public Object doFailed(AbstractApiController obj, Method joinMethod, Throwable e)
            throws Throwable {
        Object returnValue = null;
        if ((e instanceof ApplicationException)) {
            PrintlnException.println(e);
            this.log.debug(e);
            ApplicationException appexc = (ApplicationException) e;
            ExceptionContent content = appexc.getContent();
            returnValue = getResult(obj, joinMethod.getReturnType(), content);
        } else {
            PrintlnException.println(e);
            this.log.error(e);
            returnValue = getResult(obj, joinMethod.getReturnType(), new ExceptionContent());
        }
        return returnValue;
    }
}
