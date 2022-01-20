package com.ms.gateway.config;

import com.alibaba.fastjson.JSONObject;

public class Resp {

    public static JSONObject Resp(String code, String msg, Object data){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", data);
        return json;
    }

    public static JSONObject ok(Object data){
        return Resp("100200", "成功", data);
    }

    public static JSONObject fail(String code, String msg){
        return Resp(code, msg, "");
    }

}
