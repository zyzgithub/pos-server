package com.dianba.pos.common.exception.lang;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public class ExceptionFilter
        implements Filter {
    private String encoding = null;

    public void destroy() {
        this.encoding = null;
    }

    /* Error */
    public void doFilter(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response, javax.servlet.FilterChain chain)
            throws java.io.IOException, ServletException {
        // Byte code:
        //   0: aload_0
        //   1: invokespecial 28	com.dianba.pos.common.exception/lang/ExceptionFilter:getEncoding	()Ljava/lang/String;
        //   4: astore 4
        //   6: aload 4
        //   8: ifnonnull +7 -> 15
        //   11: ldc 32
        //   13: astore 4
        //   15: aload_1
        //   16: checkcast 34	javax/servlet/http/HttpServletRequest
        //   19: aload_2
        //   20: checkcast 36	javax/servlet/http/HttpServletResponse
        //   23: aload 4
        //   25: invokestatic 38	com.dianba.pos.common.exception/util/ThreadContent:init	(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;Ljava/lang/String;)V
        //   28: aload_3
        //   29: aload_1
        //   30: aload_2
        //   31: invokeinterface 44 3 0
        //   36: goto +16 -> 52
        //   39: astore 5
        //   41: aload 5
        //   43: athrow
        //   44: astore 6
        //   46: invokestatic 49	com.dianba.pos.common.exception/util/ThreadContent:freed	()V
        //   49: aload 6
        //   51: athrow
        //   52: invokestatic 49	com.dianba.pos.common.exception/util/ThreadContent:freed	()V
        //   55: return
        // Line number table:
        //   Java source line #26	-> byte code offset #0
        //   Java source line #27	-> byte code offset #6
        //   Java source line #28	-> byte code offset #11
        //   Java source line #31	-> byte code offset #15
        //   Java source line #32	-> byte code offset #28
        //   Java source line #33	-> byte code offset #36
        //   Java source line #34	-> byte code offset #41
        //   Java source line #35	-> byte code offset #44
        //   Java source line #36	-> byte code offset #46
        //   Java source line #37	-> byte code offset #49
        //   Java source line #36	-> byte code offset #52
        //   Java source line #38	-> byte code offset #55
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	56	0	this	ExceptionFilter
        //   0	56	1	request	javax.servlet.ServletRequest
        //   0	56	2	response	javax.servlet.ServletResponse
        //   0	56	3	chain	javax.servlet.FilterChain
        //   4	20	4	encoding	String
        //   39	3	5	e	Throwable
        //   44	6	6	localObject	Object
        // Exception table:
        //   from	to	target	type
        //   15	36	39	java/lang/Throwable
        //   15	44	44	finally
    }

    public void init(FilterConfig filterConfig)
            throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    private String getEncoding() {
        return this.encoding;
    }
}
