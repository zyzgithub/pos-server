package com.dianba.pos.base.handler;

import com.dianba.pos.base.BasicResult;
import com.dianba.pos.base.exception.PosAccessDeniedException;
import com.dianba.pos.base.exception.PosIllegalArgumentException;
import com.dianba.pos.base.exception.PosNullPointerException;
import com.dianba.pos.base.exception.PosRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Configuration
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    private static final Class[] EXCEPTIONS = new Class[]{PosNullPointerException.class
            , PosRuntimeException.class
            , PosIllegalArgumentException.class
            , PosAccessDeniedException.class};

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public BasicResult exceptionHandler(Exception e, HttpServletResponse response) {
        boolean isCustomException = false;
        for (Class cla : EXCEPTIONS) {
            if (e.getClass().getName().equals(cla.getName())) {
                isCustomException = true;
                break;
            }
        }
        if (!isCustomException) {
            e.printStackTrace();
            return BasicResult.createFailResult("系统错误！");
        }
        logger.error(e);
        return BasicResult.createFailResult(e.getMessage());
    }
}
