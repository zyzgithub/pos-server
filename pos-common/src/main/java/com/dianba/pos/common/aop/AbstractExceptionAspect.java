package com.dianba.pos.common.aop;

import com.dianba.pos.common.exception.lang.Formatter;
import com.dianba.pos.common.exception.util.PrintlnException;
import com.dianba.pos.common.exception.util.VoidFormatResultUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class AbstractExceptionAspect {
    protected List<Formatter<?>> formatters = new LinkedList();

    public AbstractExceptionAspect(Formatter... formatters) {
        if (formatters != null) {
            Formatter[] arrayOfFormatter;
            int j = (arrayOfFormatter = formatters).length;
            for (int i = 0; i < j; i++) {
                Formatter<Object> formatter = arrayOfFormatter[i];
                this.formatters.add(formatter);
            }
        } else {
            this.formatters.add(new VoidFormatResultUtil());
        }
    }

    public Object process(ProceedingJoinPoint point)
            throws Throwable {
        Object[] args = point.getArgs();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Object returnValue = null;
        Formatter<Object> formatter = getFormatter(point.getTarget().getClass(), method);
        try {
            returnValue = point.proceed(args);
            if (formatter != null) {
                returnValue = formatter.doSuccessfully(point.getTarget(), method, returnValue);
            }
        } catch (Throwable e) {
            if (formatter == null) {
                PrintlnException.println(e);
                throw e;
            }
            returnValue = formatter.doFailed(point.getTarget(), method, e);
        }
        return returnValue;
    }

    private Formatter<Object> getFormatter(Class<? extends Object> clazz, Method method) {
        for (Formatter formatter : this.formatters) {
            if (formatter.isFormatter(clazz, method)) {
                return formatter;
            }
        }
        return null;
    }
}
