package com.microservice.starter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
public class MsgFactory {

	private ResourceBundleMessageSource messageResource;

	@PostConstruct
	public void init() {
		if (null == messageResource) {
			messageResource = new ResourceBundleMessageSource();
			messageResource.addBasenames("SysCode", "AuthCode");
		}
	}

	public ResourceBundleMessageSource getMessageResource() {
		return messageResource;
	}

	public String get(String code, Object[] args, Locale locale) {
		if (locale == Locale.ENGLISH){
			locale = Locale.US;
		}

		if (locale == Locale.CHINESE){
			locale = Locale.CHINA;
		}

		return messageResource.getMessage(code, args, locale);
	}

}
