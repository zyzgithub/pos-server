package com.dianba.pos.base.handler;

import com.dianba.pos.base.BasicResult;
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

    private static final Class[] EXCEPTIONS
            = new Class[]{PosNullPointerException.class, PosRuntimeException.class};

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public BasicResult exceptionHandler(RuntimeException e, HttpServletResponse response) {
        logger.error(e);
        boolean isCustomException = false;
        for (Class cla : EXCEPTIONS) {
            if (e.getClass().getName().equals(cla.getName())) {
                isCustomException = true;
                break;
            }
        }
        if (!isCustomException) {
            e.printStackTrace();
        }
        return BasicResult.createFailResult(e.getMessage());
    }
}
