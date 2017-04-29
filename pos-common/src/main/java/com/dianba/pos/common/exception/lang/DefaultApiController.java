package com.dianba.pos.common.exception.lang;

import com.dianba.pos.common.exception.core.AopAble;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class DefaultApiController
        implements AopAble {
    public double mustDouble(String key) {
        String value = mustText(key);
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            Assert.checkArgumentIsTrue(false, "参数:" + key + "必须是 double 类型!");
        }
        return 0.0D;
    }

    public int mustInt(String key) {
        String value = mustText(key);
        try {
            return Double.valueOf(value).intValue();
        } catch (Exception e) {
            Assert.checkArgumentIsTrue(false, "参数:" + key + "必须是 int 类型!");
        }
        return 0;
    }

    public long mustLong(String key) {
        String value = mustText(key);
        try {
            return Double.valueOf(value).longValue();
        } catch (Exception e) {
            Assert.checkArgumentIsTrue(false, "参数:" + key + "必须是 long 类型!");
        }
        return 0L;
    }

    public String mustText(String key) {
        String value = getParamValue(key);
        Assert.checkArgumentNotBlank(value, "参数:" + key + "不能为空!");
        return value;
    }

    public String[] mustText(String... keys) {
        if (keys == null) {
            return new String[0];
        }
        String[] values = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            values[i] = mustText(keys[i]);
        }
        return values;
    }

    public String getParamValue(String key) {
        return getHttpServletRequest().getParameter(key);
    }

    public HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return request;
    }
}
