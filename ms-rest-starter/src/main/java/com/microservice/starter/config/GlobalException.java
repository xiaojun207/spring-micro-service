package com.microservice.starter.config;

import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.model.ApiResp;
import com.microservice.starter.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    /**
     * AppException 异常处理
     */
    @ExceptionHandler(AppException.class)
    public ApiResp appException(AppException e) {
        return ApiResp.fail(e.getCode(), e.getMessage());
    }

    /**
     * NoHandlerFoundException
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResp appException(NoHandlerFoundException e) {
        return ApiResp.fail(CommonCodeConst.INVALID_REQUEST, e.getMessage());
    }


    /**
     * 全局异常处理
     */
    @ExceptionHandler
    public ApiResp handleException(Exception e) {
//        log.info("handleException.name:" + e.getClass().getName());
        log.error("handleException:", e);
        return ApiResp.fail(CommonCodeConst.SERVICE_ERROR, e.getMessage());
    }

}
