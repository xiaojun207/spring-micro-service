package com.microservice.auth.strategy;

import com.microservice.auth.code.AuthCodeConst;
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
 */
@Service("CaptchaCodeStrategy")
@Slf4j
public class CaptchaCodeStrategy implements StrategyService {

	@Autowired
	private CaptchaCodeService captchaCodeService;

	@Override
	public void pre(AuthContext authContext) {
		String code = authContext.getCaptchaCode();
		String captchaKey = authContext.getCaptchaKey();

		String captchaCode = captchaCodeService.getCode(captchaKey);

		boolean isValid = code.equalsIgnoreCase(captchaCode);
		captchaCodeService.delCode(authContext.getCaptchaKey());

		if (!isValid) {
			throw new AppException(AuthCodeConst.CAPTCHA_CODE_ERROR, "图形验证码错误");
		}
	}

	@Override
	public boolean match(AuthContext authContext) {
		return StringUtils.isNotBlank(authContext.getCaptchaKey());
	}

	@Override
	public void after(AuthContext authContext, Throwable throwable) {

	}

}
