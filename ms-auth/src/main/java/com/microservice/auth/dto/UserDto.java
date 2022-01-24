package com.microservice.auth.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class UserDto {
    private Long uid;
    private String nickName;
    private String username;
    private String mobile;
    private String email;
    private String password;
    private String salt;
    private String ddOpenId;
    private String wxOpenId;
    private String avatar;
}
