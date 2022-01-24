package com.microservice.starter.strategy;


import com.microservice.starter.model.AuthContext;

public interface StrategyService {

	void pre(AuthContext authContext);

	void after(AuthContext authContext, Throwable throwable);

	boolean match(AuthContext authContext);

}
