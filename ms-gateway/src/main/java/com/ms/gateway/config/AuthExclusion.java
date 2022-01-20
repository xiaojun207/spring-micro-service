package com.ms.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Component
@ConfigurationProperties("auth.exclusion")
public class AuthExclusion {

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public boolean isExclusionUrl(String path){
        List<String> exclusions = this.getUrls();
        if (exclusions.size() == 0){
            return false;
        }
        return exclusions.stream().anyMatch( action -> antPathMatcher.match(action , path));
    }
}
