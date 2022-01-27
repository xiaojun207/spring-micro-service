package com.microservice.auth.strategy;

import com.microservice.auth.repository.IUriRepository;
import com.microservice.auth.service.PermissionService;
import com.microservice.auth.utils.TokenHelper;
import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.exception.AppException;
import com.microservice.starter.model.AuthContext;
import com.microservice.starter.model.IUser;
import com.microservice.starter.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Slf4j
@Service("AdminStrategyService")
public class AdminStrategyService implements StrategyService {

    @Autowired
    IUriRepository uriRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpSession session;
    @Autowired
    PermissionService permissionService;

    @Override
    public void pre(AuthContext authContext) {
        String token = authContext.getToken();
        if (null == token) {
            throw new AppException(CommonCodeConst.NO_LOGIN);
        }

        Long uid = 0L;
        try {
            uid = TokenHelper.getTokenUid(token);
        } catch (Exception e) {
            throw new AppException(CommonCodeConst.TOKEN_INVALID);
        }
    }

    @Override
    public void after(AuthContext authContext, Throwable throwable) {}

    @Override
    public boolean match(AuthContext authContext) {
        if (authContext == null) {
            log.info("AdminStrategyService.match,authContext:" + authContext);
            return false;
        }
        String token = authContext.getToken();
        if (StringUtils.isBlank(token)) {
            log.info("AdminStrategyService.match,token:" + authContext.getToken());
            return false;
        }
        return true;
    }
}
