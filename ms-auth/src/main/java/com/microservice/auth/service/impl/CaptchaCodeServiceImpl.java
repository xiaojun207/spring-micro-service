package com.microservice.auth.service.impl;

import com.google.code.kaptcha.Producer;
import com.microservice.auth.service.CaptchaCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CaptchaCodeServiceImpl implements CaptchaCodeService {
	// 验证码有效期，1分钟
	public static final long CAPTCHA_EXPIRE_TIME = 60 * 1;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private Producer captchaProducer;

	@Override
	public BufferedImage createCode(String captchaKey) {
		String code = captchaProducer.createText();
		String codeKey = "captcha:code:" + captchaKey;
		redisTemplate.boundValueOps(codeKey).set(code, CAPTCHA_EXPIRE_TIME, TimeUnit.SECONDS);
		return captchaProducer.createImage(code);
	}

	@Override
	public String getCode(String captchaKey) {
		if (StringUtils.isBlank(captchaKey)) {
			return "";
		}
		String codeKey = "captcha:code:" + captchaKey;
		String code = redisTemplate.boundValueOps(codeKey).get();
		log.info("获取到的captcha.codeKey:{},code:{}", codeKey, code);
		return code;
	}

	@Override
	public void delCode(String captchaKey) {
		String codeKey = "captcha:code:" + captchaKey;
		redisTemplate.delete(codeKey);
	}

}
