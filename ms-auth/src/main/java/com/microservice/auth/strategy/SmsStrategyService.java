package com.microservice.auth.strategy;

import com.microservice.auth.code.AuthCodeConst;
import com.microservice.auth.service.SmsService;
import com.microservice.starter.exception.AppException;
import com.microservice.starter.model.AuthContext;
import com.microservice.starter.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("SmsStrategyService")
public class SmsStrategyService implements StrategyService {

    @Autowired
    SmsService smsService;

    @Override
    public void pre(AuthContext authContext) {
        boolean checkFlag = false;
        if (authContext.getUserSession() != null) {
            checkFlag = smsService.checkCode(authContext.getUid(), authContext.getSmsCode());
        } else {
            checkFlag = smsService.checkCode(authContext.getMobile(), authContext.getSmsCode());
        }
        if (!checkFlag) {
            throw new AppException(AuthCodeConst.SMS_CODE_ERROR, "短信验证码错误");
        }
    }

    @Override
    public void after(AuthContext authContext, Throwable throwable) {
        if (throwable == null) {
            if (authContext.getUserSession() != null) {
                smsService.delete(authContext.getUid());
            } else {
                smsService.delete(authContext.getMobile());
            }
        }
    }

    @Override
    public boolean match(AuthContext authContext) {
        if (StringUtils.isBlank(authContext.getSmsCode())) {
            throw new AppException(AuthCodeConst.SMS_CODE_EMPTY, "短信验证码不能为空");
        }

        if (StringUtils.isBlank(authContext.getMobile()) && (null == authContext.getUserSession())) {
            throw new AppException(AuthCodeConst.SMS_MOBILE_EMPTY, "手机号码不能为空");
            // 电话号码和uid，两这必须满足其一
        }
        return true;
    }
}
