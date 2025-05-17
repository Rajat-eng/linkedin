package com.codingshuttle.linkedin.posts_service.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class UserContextFilter extends OncePerRequestFilter {

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String roles = request.getHeader("X-User-Roles"); // Example: "USER,ADMIN"
        log.info("UserContextFilter: userId = {}, roles = {}", userId, roles);
        if (userId != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (roles != null) {
                String[] roleArray = roles.split(",");
                for (String role : roleArray) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.trim()));
                }
            }

            UserDetails userDetails = new User(userId, "", authorities); // Password is not used in JWT

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            log.info("userDetails: auth = {}, authorities = {}", auth, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}