package com.dianba.pos.common.exception.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public class LoggerWapper {
    private final Log log;

    public LoggerWapper(Class<?> clazz) {
        this.log = LogFactory.getLog(clazz);
    }

    public void debug(String message, Throwable e) {
        message = formatMsg(message);
        this.log.debug(message, e);
    }

    public void info(String message) {
        message = formatMsg(message);
        this.log.info(message);
    }

    public void warn(String message) {
        message = formatMsg(message);
        this.log.warn(message);
    }

    public void error(String message) {
        message = formatMsg(message);
        this.log.error(message);
    }

    public void info(String message, Throwable e) {
        message = formatMsg(message);
        this.log.info(message, e);
    }

    public void warn(String message, Throwable e) {
        message = formatMsg(message);
        this.log.warn(message, e);
    }

    public void error(String message, Throwable e) {
        message = formatMsg(message);
        this.log.error(message, e);
    }

    private String formatMsg(String message) {
        if (StringUtils.isEmpty(ThreadContent.getSerialVersionMsg())) {
            message = "serialVersionMsg:" + ThreadContent.getSerialVersionMsg() + message;
        }
        return message;
    }

    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }

    public boolean isErrorEnabled() {
        return this.log.isErrorEnabled();
    }

    public boolean isWarnEnabled() {
        return this.log.isWarnEnabled();
    }

    public boolean isInfoEnabled() {
        return this.log.isInfoEnabled();
    }

    public void info(Object... objects) {
        if (isInfoEnabled()) {
            StringBuilder sb = new StringBuilder();
            Object[] arrayOfObject;
            int j = (arrayOfObject = objects).length;
            for (int i = 0; i < j; i++) {
                Object object = arrayOfObject[i];
                sb.append(object).append(" , ");
            }
            info(sb.toString());
        }
    }
}
