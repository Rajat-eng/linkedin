package com.codingshuttle.linkedin.user_service.controller;


import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/")
public class HealthCheck {
    @GetMapping("")
    public ResponseEntity<String> checkHeaEntity() {
        return ResponseEntity.ok("Hi From user service");
    }
    
}
