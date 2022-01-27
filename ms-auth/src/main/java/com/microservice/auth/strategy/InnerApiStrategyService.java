package com.microservice.auth.strategy;

import com.microservice.auth.config.InnerApi;
import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.exception.AppException;
import com.microservice.starter.model.AuthContext;
import com.microservice.starter.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service("InnerApiStrategyService")
public class InnerApiStrategyService implements StrategyService {

    @Autowired
    InnerApi innerApi;

    @Override
    public void pre(AuthContext authContext) {
        String innerApiToken = authContext.getInnerApiToken();
        if (null == innerApiToken) {
            throw new AppException(CommonCodeConst.NO_PERMISSION);
        }
        if (!innerApi.CheckInnerApiToken(innerApiToken)){
            throw new AppException(CommonCodeConst.TOKEN_INVALID);
        }
    }

    @Override
    public void after(AuthContext authContext, Throwable throwable) {}

    @Override
    public boolean match(AuthContext authContext) {
        if (authContext == null) {
            return false;
        }
        String innerApiToken = authContext.getInnerApiToken();
        if (StringUtils.isBlank(innerApiToken)) {
            return false;
        }
        return true;
    }
}
