package com.microservice.auth.service;

import com.microservice.auth.dto.UserDto;
import com.microservice.starter.model.AuthContext;

public interface LoginService {

    String login(UserDto user);
    void logout(AuthContext authContext);

    Long getUidByToken(String token);


}
