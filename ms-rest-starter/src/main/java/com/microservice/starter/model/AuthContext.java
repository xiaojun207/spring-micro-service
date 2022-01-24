package com.microservice.starter.model;

import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.exception.AppException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * authorization: token=12334423134234;captcha-code=123455;captcha-key=123222aaa;sms-code=1223442;sms-key=12345
 */
@Slf4j
@Data
public class AuthContext {
    private static final String AUTH_HEADER = "authorization"; // Cookie or authorization
    private static final String FILED_SEPARATOR = ";";
    private static final String VALUE_SEPARATOR = "=";
    private static final String USER_SESSION = "userSession";
    //
    private static final String KEY_TOKEN = "token";
    private static final String KEY_CAPTCHA_CODE = "captcha-code";
    private static final String KEY_CAPTCHA_KEY = "captcha-key";
    private static final String KEY_SMS_CODE = "sms-code";
    private static final String KEY_SMS_KEY = "sms-key";

    //
    String authorization;
    NativeWebRequest webRequest;
    HashMap<String, String> fields = new HashMap<>();

    public static AuthContext build(NativeWebRequest webRequest) {
        AuthContext res = new AuthContext();
        res.webRequest = webRequest;
        res.authorization = webRequest.getHeader(AUTH_HEADER);
        res.parseAuthorization();
        return res;
    }

    public void parseAuthorization() {
        // 解析authorization 获取信息
        String authStr = this.authorization;
        try {
            String[] fieldsStr = StringUtils.tokenizeToStringArray(authStr, FILED_SEPARATOR, true, true);
            this.fields = new HashMap<>();
            for (String f : fieldsStr) {
                String[] kv = StringUtils.tokenizeToStringArray(f, VALUE_SEPARATOR, true, true);
                if (kv == null) {
                    continue;
                }
                this.fields.put(kv[0].trim(), URLDecoder.decode(kv[1].trim(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            throw new AppException(CommonCodeConst.FIELD_ERROR, e);
        }
    }

    public IUser getUserSession() {
        Object user = webRequest.getAttribute(USER_SESSION, RequestAttributes.SCOPE_REQUEST);
        if (user == null)
            return null;
        return (IUser) user;
    }

    public void setUserSession(IUser user) {
        webRequest.setAttribute(USER_SESSION, user, RequestAttributes.SCOPE_REQUEST);
    }

    public String getCaptchaCode() {
        return this.fields.getOrDefault(KEY_CAPTCHA_CODE, "");
    }

    public String getCaptchaKey() {
        return this.fields.getOrDefault(KEY_CAPTCHA_KEY, "");
    }

    public String getSmsCode() {
        return this.fields.getOrDefault(KEY_SMS_CODE, "");
    }

    public String getSmsKey() {
        return this.fields.getOrDefault(KEY_SMS_KEY, "");
    }

    public String getToken() {
        return this.fields.getOrDefault(KEY_TOKEN, "");
    }

    public Long getUid() {
        IUser user = this.getUserSession();
        if (user == null) {
            return null;
        }
        return user.getUid();
    }

}
