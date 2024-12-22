package com.qihui.sun.permission;

import cn.dev33.satoken.util.SaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    // 全局异常拦截
    @ExceptionHandler
    public SaResult handlerException(Exception e) {
        logger.info("全局异常拦截：{}", e.getMessage());
        return SaResult.error(e.getMessage());
    }
}

