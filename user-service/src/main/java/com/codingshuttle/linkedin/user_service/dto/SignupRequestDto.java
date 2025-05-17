package com.codingshuttle.linkedin.user_service.dto;

import java.util.Set;

import com.codingshuttle.linkedin.user_service.entity.enums.Role;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String name;
    private String email;
    private String password;
    private Set<Role> roles;
}
