package com.microservice.auth.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class FeignConfig {
    @Autowired
    InnerApi innerApi;

    @Bean
    Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Decoder decoder() {
        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }

    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters
                (new MappingJackson2HttpMessageConverter());
        return () -> httpMessageConverters;
    }

    @Bean
    public RequestInterceptor getInnerAuthRequestInterceptor() {
        return template -> {
            String innerApiToken = innerApi.getInnerApiToken();
            template.header("authorization", "inner-api-token" + "=" + innerApiToken);
        };
    }
}
