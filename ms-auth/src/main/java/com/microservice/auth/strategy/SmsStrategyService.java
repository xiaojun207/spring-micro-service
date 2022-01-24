package com.microservice.auth.strategy;

import com.microservice.starter.model.AuthContext;
import com.microservice.starter.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("SmsStrategyService")
public class SmsStrategyService implements StrategyService {

    @Override
    public void pre(AuthContext authContext) {
        log.info("SmsStrategyService.pre");
    }

    @Override
    public void after(AuthContext authContext, Throwable throwable) {
        log.info("SmsStrategyService.after");
    }

    @Override
    public boolean match(AuthContext authContext) {
        log.info("SmsStrategyService.match");
        return true;
    }
}
