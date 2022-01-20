package com.microservice.starter.exception;

import lombok.Data;

@Data
public class AppException extends RuntimeException{
    private String code;
    private String[] format;
    private Object data;


    public AppException(String code) {
        super();
        this.code = code;
    }

    public AppException(String code, Exception e) {
        super(e.getMessage(), e);
        this.code = code;
    }

    public AppException(String code, String message, String... format) {
        super(message);
        this.code = code;
        this.format = format;
    }

    public AppException(String code, String message, String data, String... format) {
        super(message);
        this.code = code;
        this.format = format;
        this.data = data;
    }

    @Override
    public String toString() {
        return "[code:" + this.code + ", msg:" + super.getMessage() + "]";
    }
}
