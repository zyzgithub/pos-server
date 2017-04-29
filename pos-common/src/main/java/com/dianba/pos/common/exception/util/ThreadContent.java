package com.dianba.pos.common.exception.util;

import com.dianba.pos.common.exception.core.ResponseContent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ThreadContent {
    private static final ThreadLocal<ResponseContent> RESPONSE_CONTENTS = new ThreadLocal();
    private static final ThreadLocal<HttpServletRequest> HTTP_SERVLET_REQUESTS = new ThreadLocal();
    private static final ThreadLocal<HttpServletResponse> HTTP_SERVLET_RESPONSES = new ThreadLocal();
    private static final ThreadLocal<String> SERIAL_VERSION_MSG = new ThreadLocal();
    private static final Log LOG = LogFactory.getLog(ThreadContent.class);

    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) HTTP_SERVLET_REQUESTS.get();
    }

    public static void setHttpServletRequest(HttpServletRequest request) {
        HTTP_SERVLET_REQUESTS.set(request);
    }

    public static HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) HTTP_SERVLET_RESPONSES.get();
    }

    public static void setHttpServletResponse(HttpServletResponse response) {
        HTTP_SERVLET_RESPONSES.set(response);
    }

    public static ResponseContent getResponseContent() {
        return (ResponseContent) RESPONSE_CONTENTS.get();
    }

    public static void setResponseContent(ResponseContent content) {
        RESPONSE_CONTENTS.set(content);
    }

    public static ResponseContent setResponse(HashMap<String, Object> map) {
        ResponseContent content = (ResponseContent) RESPONSE_CONTENTS.get();
        content.addAllResponse(map);
        return content;
    }

    public static void freed() {
        RESPONSE_CONTENTS.remove();
        HTTP_SERVLET_RESPONSES.remove();
        HTTP_SERVLET_REQUESTS.remove();
        SERIAL_VERSION_MSG.remove();
    }

    public static void init(HttpServletRequest request, HttpServletResponse response, String encoding)
            throws UnsupportedEncodingException {
        setHttpServletRequest(request);
        setHttpServletResponse(response);
        request.setCharacterEncoding(encoding);
        response.setContentType("application/json; charset=" + encoding);
        response.setCharacterEncoding(encoding);
        String serialVersionMsg = request.getHeader("serialVersionMsg");
        SERIAL_VERSION_MSG.set(serialVersionMsg);
        ResponseContent content = new ResponseContent();
        setResponseContent(content);
    }

    public static String getSerialVersionMsg() {
        return (String) SERIAL_VERSION_MSG.get();
    }

    public static void println(Object obj) {
        try {
            HttpServletResponse response = (HttpServletResponse) HTTP_SERVLET_RESPONSES.get();
            if (response == null) {
                LOG.info("HttpServletResponse is null ");
                return;
            }
            response.getWriter().println(obj);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            LOG.warn(e);
        }
    }
}
