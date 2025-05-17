package com.codingshuttle.linkedin.posts_service.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class FeinClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String userId =  ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
               
        if(userId != null) {
            requestTemplate.header("X-User-Id", userId);
        }
    }
}
