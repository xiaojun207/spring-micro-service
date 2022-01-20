package com.microservice.starter.model;

import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.exception.AppException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.NativeWebRequest;

@Slf4j
@Data
public class AuthContext {
    private static final String AUTH_HEADER = "authorization"; // Cookie or authorization

    String authorization;
    Integer uid;

    public static AuthContext build(NativeWebRequest webRequest) {
        String authorization = webRequest.getHeader(AUTH_HEADER);
        // log.info("AuthContext.build,authorization:" + authorization);
        if (authorization == null || authorization.isBlank()) {
            throw new AppException(CommonCodeConst.NO_LOGIN, "未登录");
        }

        AuthContext res = new AuthContext();
        res.authorization = authorization;
        res.parseAuthorization();
        return res;
    }

    private void parseAuthorization(){
        // 解析authorization 获取uid等信息
        this.uid = 1001;
    }

}
