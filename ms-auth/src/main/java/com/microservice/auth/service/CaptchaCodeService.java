package com.microservice.auth.service;

import java.awt.image.BufferedImage;

public interface CaptchaCodeService {

    BufferedImage createCode(String key);

    String getCode(String key);

    void delCode(String captchaKey);
}
