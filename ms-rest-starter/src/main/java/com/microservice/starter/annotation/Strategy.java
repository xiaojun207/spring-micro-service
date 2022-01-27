package com.microservice.starter.annotation;

import com.microservice.starter.strategy.StrategyService;

import java.lang.annotation.*;

/**
 * 策略注解，需配置strategyService或strategyClass
 * @author xiao
 * @Field strategyClass : @Strategy(strategyService = {"InnerApiStrategyService"}, strategyClass = {InnerApiStrategyService.class, AuthStrategyService.class})
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Strategy {
	/**
	 *
	 * @return
	 */
	String[] strategyService() default {};

	/**
	 *
	 * @Strategy(strategyService = {"InnerApiStrategyService"}, strategyClass = {InnerApiStrategyService.class, AuthStrategyService.class})
	 * @return
	 */
	Class<? extends StrategyService>[] strategyClass() default{};

}
