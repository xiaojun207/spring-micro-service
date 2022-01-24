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

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class StrategyAdvice {

    @Autowired
    BeanFactory beanfactory;

    @Pointcut(value = "@annotation(com.microservice.starter.annotation.Strategy)")
    private void pointcut() {}

    @Around(value = "pointcut()")
    public Object beforeController(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) (pjp.getSignature())).getMethod();
        Strategy strategyContext = method.getAnnotation(Strategy.class);
        String[] strategyServices = strategyContext.strategyService();
        Object[] args = pjp.getArgs();

        AuthContext authContext = null;
        for (Object obj : args) {
            if (null == obj) {
                continue;
            }
            if (AuthContext.class.isAssignableFrom(obj.getClass())) {
                authContext = (AuthContext) obj;
                break;
            }
        }

        for (String strategy : strategyServices) {
			StrategyService authStrategy = (StrategyService) beanfactory.getBean(strategy);
			if (!authStrategy.match(authContext)) {
				throw new AppException(CommonCodeConst.NO_PERMISSION);
			} else {
				authStrategy.pre(authContext);
			}
        }

		Throwable throwable = null;
		Object o = null;
        try {
            o = pjp.proceed();
        } catch (Throwable e) {
            throwable = e;
        }

        for (String strategy : strategyServices) {
			StrategyService authStrategy = (StrategyService) beanfactory.getBean(strategy);
			if (!authStrategy.match(authContext)) {
				throw new AppException(CommonCodeConst.NO_PERMISSION);
			} else {
				authStrategy.after(authContext, throwable);
			}
        }

        if (throwable != null) {
            throw throwable;
        }
        return o;
    }

}
