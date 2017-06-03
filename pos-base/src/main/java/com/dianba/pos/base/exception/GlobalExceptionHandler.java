package com.dianba.pos.base.exception;

import com.dianba.pos.base.BasicResult;
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

    private static Logger logger= LogManager.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public BasicResult exceptionHandler(RuntimeException e, HttpServletResponse response) {
        logger.error(e);
        return BasicResult.createFailResult(e.getMessage());
    }
}
