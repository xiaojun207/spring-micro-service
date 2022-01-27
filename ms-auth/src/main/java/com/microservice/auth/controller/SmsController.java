package com.microservice.auth.controller;

import com.microservice.auth.code.AuthCodeConst;
import com.microservice.auth.service.SmsService;
import com.microservice.starter.exception.AppException;
import com.microservice.starter.model.AuthContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "/code", method = RequestMethod.GET)
    @ResponseBody
    public void getCode(AuthContext authContext) {
        if (StringUtils.isNotBlank(authContext.getMobile())) {
            smsService.sendCode(authContext.getMobile());
        } else if (authContext.getUid() > 0) {
            smsService.sendCode(authContext.getUid());
        }
    }

}
