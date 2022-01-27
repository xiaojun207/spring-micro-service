package com.microservice.starter.advice;

import com.microservice.starter.annotation.Strategy;
import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.exception.AppException;
import com.microservice.starter.model.AuthContext;
import com.microservice.starter.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@Aspect
@Slf4j
public class StrategyAdvice {

    @Autowired
    HttpServletRequest request;

    @Autowired
    BeanFactory beanfactory;

    @Pointcut(value = "@annotation(com.microservice.starter.annotation.Strategy)")
    private void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object beforeController(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) (pjp.getSignature())).getMethod();
        Strategy strategyContext = method.getAnnotation(Strategy.class);
        Set<StrategyService> serviceSet = getStrategyService(strategyContext);

        // 1.获取AuthContext
        AuthContext authContext = getAuthContext(pjp);

        // 2.proceed before
        for (StrategyService authStrategy : serviceSet) {
            if (!authStrategy.match(authContext)) {
                throw new AppException(CommonCodeConst.NO_PERMISSION, "没有访问权限.");
            } else {
                authStrategy.pre(authContext);
            }
        }

        // 3.proceed
        Throwable throwable = null;
        Object o = null;
        try {
            o = pjp.proceed();
        } catch (Throwable e) {
            throwable = e;
        }

        // 4.proceed after
        for (StrategyService authStrategy : serviceSet) {
            if (!authStrategy.match(authContext)) {
                throw new AppException(CommonCodeConst.NO_PERMISSION, "没有访问权限!");
            } else {
                authStrategy.after(authContext, throwable);
            }
        }

        if (throwable != null) {
            throw throwable;
        }
        return o;
    }

    private AuthContext getAuthContext(ProceedingJoinPoint pjp){
        for (Object obj :  pjp.getArgs()) {
            if (obj != null && AuthContext.class.isAssignableFrom(obj.getClass())) {
                return (AuthContext) obj;
            }
        }
        return AuthContext.build(request);
    }

    private Set<StrategyService> getStrategyService(Strategy strategyContext){
        Set<StrategyService> serviceSet = new LinkedHashSet<>();
        for (Class<? extends StrategyService> strategyClass : strategyContext.strategyClass()) {
            StrategyService authStrategy = beanfactory.getBean(strategyClass);
            serviceSet.add(authStrategy);
        }

        for (String strategy : strategyContext.strategyService()) {
            StrategyService authStrategy = (StrategyService) beanfactory.getBean(strategy);
            serviceSet.add(authStrategy);
        }
        return serviceSet;
    }

}
