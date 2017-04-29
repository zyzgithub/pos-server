package com.dianba.pos.common.exception.lang;

import com.dianba.pos.common.exception.core.AopAble;
import com.dianba.pos.common.exception.core.ResponseContent;
import com.dianba.pos.common.exception.util.LoggerWapper;
import com.dianba.pos.common.exception.util.ThreadContent;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

public class AbstractApiController
        implements AopAble {
    protected LoggerWapper log = new LoggerWapper(getClass());

    public ResponseContent getResponseContent() {
        ResponseContent content = ThreadContent.getResponseContent();
        if (content == null) {
            content = new ResponseContent();
            ThreadContent.setResponseContent(content);
        }
        return content;
    }

    public ResponseContent addResponseBody(Object obj) {
        ResponseContent content = getResponseContent();
        content.addResponseBody(obj);
        return content;
    }

    public ResponseContent addAllResponse(Map<String, Object> obj) {
        ResponseContent content = getResponseContent();
        content.addAllResponse(obj);
        return content;
    }

    public ResponseContent addResponse(String key, Object obj) {
        ResponseContent content = getResponseContent();
        content.getResponse().put(key, obj);
        return content;
    }

    public Double getDouble(String key, Double defalutValue) {
        String value = getParamValue(key);
        if (StringUtils.isEmpty(value)) {
            return defalutValue;
        }
        try {
            return Double.valueOf(Double.parseDouble(value));
        } catch (Exception e) {
            Assert.checkArgumentIsTrue(false, "参数:" + key + "必须是 double 类型!");
        }
        return Double.valueOf(0.0D);
    }

    public Integer getInt(String key, Integer defalutValue) {
        String value = getParamValue(key);
        if (StringUtils.isEmpty(value)) {
            return defalutValue;
        }
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            Assert.checkArgumentIsTrue(false, "参数:" + key + "必须是 Integer 类型!");
        }
        return Integer.valueOf(0);
    }

    public Integer getPageNumber() {
        return getInt("pageNumber", Integer.valueOf(1));
    }

    public Integer getMaxPageSize() {
        return getInt("maxPageSize", Integer.valueOf(20));
    }

    public Long getLong(String key, Long defalutValue) {
        String value = getParamValue(key);
        if (StringUtils.isEmpty(value)) {
            return defalutValue;
        }
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            Assert.checkArgumentIsTrue(false, "参数:" + key + "必须是 Integer 类型!");
        }
        return Long.valueOf(0L);
    }

    public String getText(String key, String defalutValue) {
        String value = getParamValue(key);
        if (StringUtils.isEmpty(value)) {
            return defalutValue;
        }
        return value;
    }

    public Timestamp getTimestamp(String key, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String value = getParamValue(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        try {
            return new Timestamp(dateFormat.parse(value).getTime());
        } catch (Exception e) {
            Assert.checkArgumentIsTrue(false, "参数:" + key + "必须是 Date 类型!");
        }
        return null;
    }

    public double mustDouble(String key) {
        Double value = getDouble(key, null);
        Assert.checkArgumentNotNull(value, "参数:" + key + "不能为空!");
        return value.doubleValue();
    }

    public int mustInt(String key) {
        Integer value = getInt(key, null);
        Assert.checkArgumentNotNull(value, "参数:" + key + "不能为空!");
        return value.intValue();
    }

    public long mustLong(String key) {
        Long value = getLong(key, null);
        Assert.checkArgumentNotNull(value, "参数:" + key + "不能为空!");
        return value.longValue();
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

    public Timestamp mustTimestamp(String key, String pattern) {
        Timestamp time = getTimestamp(key, pattern);
        Assert.checkArgumentNotNull(time, "参数:" + key + "不能为空!");
        return time;
    }

    public Timestamp mustTimestamp(String key) {
        Timestamp time = getTimestamp(key, "yyyy-MM-dd HH:mm:ss");
        Assert.checkArgumentNotNull(time, "参数:" + key + "不能为空!");
        return time;
    }

    public String getParamValue(String key) {
        String value = getHttpServletRequest().getParameter(key);
        if (!StringUtils.isEmpty(value)) {
            value = value.trim();
        }
        return value;
    }

    public HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return request;
    }
}
