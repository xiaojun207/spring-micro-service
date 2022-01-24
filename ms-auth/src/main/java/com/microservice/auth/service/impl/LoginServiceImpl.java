package com.microservice.auth.service.impl;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.microservice.auth.code.AuthCodeConst;
import com.microservice.auth.dto.UserDto;
import com.microservice.auth.entity.User;
import com.microservice.auth.repository.IUserRepository;
import com.microservice.auth.service.LoginService;
import com.microservice.auth.service.RsaService;
import com.microservice.auth.utils.Sha256;
import com.microservice.auth.utils.TokenHelper;
import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.exception.AppException;
import com.microservice.starter.model.AuthContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    RsaService rsaService;

    @Autowired
    IUserRepository userRepository;

    @Override
    public String login(UserDto in, AuthContext authContext) {
        try {
            String password = rsaService.decrypt(in.getPassword());
            // 密码原文
            in.setPassword(password);
        } catch (Exception e) {
            log.info("login.in.password:" + in.getPassword(), e);
        }

        User user = userRepository.findUserByMobile(in.getUsername());
        if (user == null) {
            throw new AppException(AuthCodeConst.USER_PASSWORD_ERROR, "用户名密码错误");
        }

        String mdxPassword = Sha256.getSHA256Str(user.getSalt() + in.getPassword());
        log.info("mdxPassword:" + mdxPassword);
        log.info("user.getPassword:" + user.getPassword());
        if (mdxPassword.equals(user.getPassword())) {
            String token = TokenHelper.getToken(user.getUid(), 60 * 60 * 24);
            authContext.setUserSession(user);
            return token;
        }
        throw new AppException(AuthCodeConst.USER_PASSWORD_ERROR, "用户名密码错误");
    }

    @Override
    public Long getUidByToken(String token) {
        try {
            return TokenHelper.getTokenUid(token);
        } catch (TokenExpiredException e) {
            throw new AppException(CommonCodeConst.NO_LOGIN, "token已过期");
        } catch (Exception e) {
            throw new AppException(CommonCodeConst.NO_LOGIN, "token无效");
        }
    }

    public static void main(String[] args) {
        String mdxPassword = Sha256.getSHA256Str("8faad06e547abb2d48544527a018657c376f4393bc6eeabd179ba06a7f80c5c2" + "123456");
        System.out.println(mdxPassword);
    }

}
