package com.microservice.auth.controller;

import com.microservice.auth.code.AuthCodeConst;
import com.microservice.auth.service.CaptchaCodeService;
import com.microservice.auth.strategy.InnerApiStrategyService;
import com.microservice.starter.annotation.Strategy;
import com.microservice.starter.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

	@Autowired
	private CaptchaCodeService captchaCodeService;

	@RequestMapping(value = "/code", method = RequestMethod.GET)
	@ResponseBody
	public void getCode(@RequestParam("randomKey") String randomKey, HttpServletResponse resp) {
		ServletOutputStream sos = null;
		try {
			BufferedImage bi = captchaCodeService.createCode(randomKey);

		 	resp.setDateHeader("Expires", 0);
	        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
	        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
	        resp.setHeader("Pragma", "no-cache");
	        resp.setContentType("image/jpeg");
	        sos = resp.getOutputStream();
	        ImageIO.write(bi, "jpg", sos);
	        sos.flush();
		} catch (Exception e) {
			log.error("图形验证码异常", e);
		} finally {
			if (sos != null) {
				try {
					sos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 验证图形验证吗
	 * 仅内部可调用
	 * @param code
	 * @param randomKey
	 */
	@Strategy(strategyClass = {InnerApiStrategyService.class})
	@RequestMapping(value = "/validate", method = RequestMethod.GET)
	public void validCaptchaCode(@RequestParam("code") String code, @RequestParam("randomKey") String randomKey) {
		String captchaCode = captchaCodeService.getCode(randomKey);
		boolean isValid = StringUtils.equalsIgnoreCase(code, captchaCode);
		if (!isValid) {
			throw new AppException(AuthCodeConst.CAPTCHA_CODE_ERROR, "图形验证码错误");
		}
	}
}
