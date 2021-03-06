package com.microservice.auth.strategy;

import com.microservice.auth.entity.User;
import com.microservice.auth.repository.IUriRepository;
import com.microservice.auth.service.TokenService;
import com.microservice.auth.service.UserService;
import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.exception.AppException;
import com.microservice.starter.model.AuthContext;
import com.microservice.starter.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


@Slf4j
@Service("AuthStrategyService")
public class AuthStrategyService implements StrategyService {

    @Autowired
    IUriRepository uriRepository;

    @Autowired
    TokenService tokenService;
    @Autowired
    UserService userService;

    @Autowired
    HttpSession session;

    @Override
    public void pre(AuthContext authContext) {
        String token = authContext.getToken();
        if (null == token) {
            throw new AppException(CommonCodeConst.NO_LOGIN);
        }

        try {
            Long uid = tokenService.getUidByToken(token);
            if (uid == 0 ){
                // 如果用户uid==null or uid == 0 ，则token失效
                throw new AppException(CommonCodeConst.TOKEN_INVALID);
            }
            if(authContext.getUserSession() == null){
                User user = userService.findById(uid);
                session.setAttribute(AuthContext.USER_SESSION, user);
            }
        } catch (Exception e) {
            throw new AppException(CommonCodeConst.TOKEN_INVALID, e);
        }
    }

    @Override
    public void after(AuthContext authContext, Throwable throwable) {
//        log.info("AuthStrategyService.after");
    }

    @Override
    public boolean match(AuthContext authContext) {
        if (authContext == null) {
            log.info("AuthStrategyService.match,authContext:" + authContext);
            return false;
        }
        String token = authContext.getToken();
        if (StringUtils.isBlank(token)) {
            log.info("AuthStrategyService.match,token:" + authContext.getToken());
            return false;
        }
        return true;
    }
}
