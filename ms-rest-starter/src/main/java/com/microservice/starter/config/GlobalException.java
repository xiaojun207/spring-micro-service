package com.microservice.starter.config;

import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.model.ApiResp;
import com.microservice.starter.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Locale;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    @Autowired
    MsgFactory msgFactory;

    public ApiResp getApiResp(String code, String[] args, Object data) {
        return getApiResp(code, args, data, LocaleContextHolder.getLocale());
    }
    public ApiResp getApiResp(String code, String[] args, Object data, Locale locale) {
        ApiResp apiResp = new ApiResp();
        apiResp.setCode(code);
        String msg = null;
        try {
            msg = msgFactory.get(code, args, locale);
        } catch (NoSuchMessageException e) {
            log.error("NoSuchMessageException,code:{},local:{},e:{}", code, locale.toString(), e.getMessage());
        } catch (Exception e) {
            log.error("根据code码发现返回值异常,code:{},local:{},e:{}", code, locale.toString(), e.getMessage());
        }
        apiResp.setMsg(msg);
        apiResp.setData(data);
        return apiResp;
    }

    /**
     * AppException 异常处理
     */
    @ExceptionHandler(AppException.class)
    public ApiResp appException(AppException e) {
        log.error("appException:", e);
//        return ApiResp.fail(e.getCode(), e.getMessage());
        return getApiResp(e.getCode(), e.getFormat(), e.getData());
    }

    /**
     * NoHandlerFoundException
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResp appException(NoHandlerFoundException e) {
//        return ApiResp.fail(CommonCodeConst.INVALID_REQUEST, e.getMessage());
        return getApiResp(CommonCodeConst.INVALID_REQUEST, null, null);
    }


    /**
     * 全局异常处理
     */
    @ExceptionHandler
    public ApiResp handleException(Exception e) {
//        log.info("handleException.name:" + e.getClass().getName());
        log.error("handleException:", e);
//        return ApiResp.fail(CommonCodeConst.SERVICE_ERROR, e.getMessage());
        return getApiResp(CommonCodeConst.SERVICE_ERROR, null, null);
    }

}
