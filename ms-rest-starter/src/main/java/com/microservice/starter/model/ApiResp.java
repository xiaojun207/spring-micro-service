package com.microservice.starter.model;

import com.alibaba.fastjson.JSONObject;
import com.microservice.starter.code.CommonCodeConst;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ApiResp {
    String code;
    String msg;
    Object data;

    public ApiResp() {}

    public ApiResp(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString(){
       return JSONObject.toJSON(this).toString();
    }

    public static ApiResp ok(Object data){
        return new ApiResp(CommonCodeConst.SUCCESS, "成功", data);
    }

    public static ApiResp fail(String code, String msg){
        return new ApiResp(code, msg, "");
    }

}
