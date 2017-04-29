package com.dianba.pos.common.exception.util;

import com.alibaba.fastjson.JSON;
import com.dianba.pos.common.exception.core.ApplicationException;
import com.dianba.pos.common.exception.core.ExceptionContent;
import com.dianba.pos.common.exception.lang.DefaultApiController;
import com.dianba.pos.common.exception.lang.Formatter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class DefaultFormatResultUtil
        implements Formatter<DefaultApiController> {
    protected final Log log = LogFactory.getLog(getClass());

    private static Object getResult(Class<?> returnType, Object returnValue) {
        Object result = returnValue;
        if ((returnValue == null) || (((returnValue instanceof String)) &&
                (StringUtils.isEmpty(returnValue.toString())))) {
            result = new ExceptionContent();
        }
        if (String.class == returnType) {
            result = JSON.toJSONString(result);
        }
        return result;
    }

    public boolean isFormatter(Class<?> clazz, Method joinMethod) {
        try {
            if (!DefaultApiController.class.isAssignableFrom(clazz)) {
                return false;
            }
            Method method = GenericsUtils.getMethodPermission(clazz, joinMethod);
            return method != null;
        } catch (Exception e) {
        }
        return false;
    }

    public Object doSuccessfully(DefaultApiController obj, Method joinMethod, Object returnValue)
            throws Exception {
        return getResult(joinMethod.getReturnType(), returnValue);
    }

    public Object doFailed(DefaultApiController obj, Method joinMethod, Throwable e)
            throws Throwable {
        Object returnValue = null;
        if ((e instanceof ApplicationException)) {
            PrintlnException.println(e);
            this.log.debug(e);
            ApplicationException appexc = (ApplicationException) e;
            ExceptionContent content = appexc.getContent();
            returnValue = getResult(joinMethod.getReturnType(), content);
        } else {
            PrintlnException.println(e);
            this.log.error(e);
            returnValue = getResult(joinMethod.getReturnType(), new ExceptionContent());
        }
        return returnValue;
    }
}
