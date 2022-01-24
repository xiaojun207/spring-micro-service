package com.microservice.auth.strategy;

import com.microservice.starter.model.AuthContext;
import com.microservice.starter.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service("AuthStrategyService")
public class AuthStrategyService implements StrategyService {
    @Override
    public void pre(AuthContext authContext) {
        log.info("AuthStrategyService.pre");
    }

    @Override
    public void after(AuthContext authContext, Throwable throwable) {
        log.info("AuthStrategyService.after");
    }

    @Override
    public boolean match(AuthContext authContext) {
        log.info("AuthStrategyService.match");
        return true;
    }
}
