package com.microservice.auth.strategy;

import com.microservice.auth.service.CaptchaCodeService;
import com.microservice.starter.exception.AppException;
import com.microservice.starter.model.AuthContext;
import com.microservice.starter.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 校验图形验证码策略方法
 *
 * @author liuze
 *
 */
@Service("CaptchaCodeStrategy")
@Slf4j
public class CaptchaCodeStrategy implements StrategyService {

	@Autowired
	private CaptchaCodeService captchaCodeService;

	@Override
	public void pre(AuthContext authContext) {
		String code = authContext.getCaptchaCode();
		String captchaNo = authContext.getCaptchaNo();

		String captchaCode = captchaCodeService.getCode(captchaNo);

		boolean isValid = code.equalsIgnoreCase(captchaCode);
		captchaCodeService.delCode(authContext.getCaptchaNo());

		if (!isValid) {
			throw new AppException("100301", "图形验证码错误");
		}
	}

	@Override
	public boolean match(AuthContext authContext) {
		return StringUtils.isNotBlank(authContext.getCaptchaNo());
	}

	@Override
	public void after(AuthContext authContext, Throwable throwable) {

	}

}
