package com.qihui.sun.config;

import cn.dev33.satoken.exception.*;
import cn.dev33.satoken.util.SaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(NotLoginException.class)
    public SaResult handlerException(NotLoginException e) {
        logger.info(e.getMessage(), e);
        return SaResult.error(e.getMessage());
    }

    @ExceptionHandler(NotPermissionException.class)
    public SaResult handlerException(NotPermissionException e) {
        logger.info(e.getMessage(), e);
        return SaResult.error("缺少权限：" + e.getPermission());
    }

    @ExceptionHandler(NotRoleException.class)
    public SaResult handlerException(NotRoleException e) {
        logger.info(e.getMessage(), e);
        return SaResult.error("缺少角色：" + e.getRole());
    }

    @ExceptionHandler(NotSafeException.class)
    public SaResult handlerException(NotSafeException e) {
        logger.info(e.getMessage(), e);
        return SaResult.error("二级认证校验失败：" + e.getService());
    }

    @ExceptionHandler(NotHttpBasicAuthException.class)
    public SaResult handlerException(NotHttpBasicAuthException e) {
        logger.info(e.getMessage(), e);
        return SaResult.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public SaResult handlerException(Exception e) {
        logger.info(e.getMessage(), e);
        return SaResult.error(e.getMessage());
    }
}

