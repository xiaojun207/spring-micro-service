package com.microservice.starter.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Strategy {
	/**
	 *
	 * @return
	 */
	String[] strategyService() default {};

}
