package com.codingshuttle.linkedin.posts_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.codingshuttle.linkedin.posts_service.filters.UserContextFilter;

import lombok.RequiredArgsConstructor;



@Configuration
@EnableWebSecurity // Enables Spring Security configuration
@EnableMethodSecurity(securedEnabled = true) // Enables @PreAuthorize annotations
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserContextFilter userContextFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF is needed for sessions, not JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // Public APIs
                        .anyRequest().authenticated())
                .addFilterBefore(userContextFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}