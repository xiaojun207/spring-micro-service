package com.microservice.starter.model;

import com.microservice.starter.code.CommonCodeConst;
import com.microservice.starter.exception.AppException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * authorization: token=12334423134234,captcha-code=123455,captcha-key=123222aaa,sms-code=1223442;sms-key=12345
 */
@Slf4j
@Data
public class AuthContext {
    public static final String AUTH_HEADER = "authorization"; // Cookie or authorization
    public static final String FILED_SEPARATOR = ",";
    public static final String VALUE_SEPARATOR = "=";
    public static final String USER_SESSION = "userSession";
    //
    private static final String KEY_TOKEN = "token";
    private static final String KEY_CAPTCHA_CODE = "captcha-code";
    private static final String KEY_CAPTCHA_KEY = "captcha-key";
    private static final String KEY_SMS_CODE = "sms-code";
    private static final String KEY_SMS_KEY = "sms-key";
    private static final String KEY_USER_ACCOUNT = "user-account";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_INNER_API_TOKEN = "inner-api-token";

    //
    String authorization;
    HttpServletRequest request;
    HashMap<String, String> fields = new HashMap<>();

    public static AuthContext build(HttpServletRequest request) {
        AuthContext res = new AuthContext();
        res.request = request;
        res.authorization = request.getHeader(AUTH_HEADER);
        res.parseAuthorization();
        return res;
    }

    public static AuthContext build(String authorization) {
        AuthContext res = new AuthContext();
        res.authorization = authorization;
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
                log.info("f:" + f + ", k:" + kv[0].trim());
                if(kv.length == 2){
                    this.fields.put(kv[0].trim(), URLDecoder.decode(kv[1].trim(), "UTF-8"));
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new AppException(CommonCodeConst.FIELD_ERROR, e);
        }
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

    public String getUserAccount(){
        return this.fields.getOrDefault(KEY_USER_ACCOUNT, "");
    }

    public String getMobile(){
        return this.fields.getOrDefault(KEY_MOBILE, "");
    }

    public String getInnerApiToken() {
        return this.fields.getOrDefault(KEY_INNER_API_TOKEN, "");
    }

    public Long getUid() {
        IUser user = getUserSession();
        if (user == null)
            return null;
        return user.getUid();
    }

    public IUser getUserSession() {
        Object user = request.getSession().getAttribute(USER_SESSION);
        if(user == null){
            return null;
        }
        return (IUser) user;
    }
}
