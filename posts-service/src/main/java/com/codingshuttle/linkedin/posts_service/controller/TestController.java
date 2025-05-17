package com.codingshuttle.linkedin.posts_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class TestController {
    @Secured("ROLE_ADMIN")
    @GetMapping("/test")
    public ResponseEntity<String> test(HttpServletRequest request) {

        //  request.getHeaderNames().asIterator().forEachRemaining(
        //     header -> System.out.println("🔹 Header [" + header + "] = " + request.getHeader(header)));
        return ResponseEntity.ok("Test endpoint is working!");
    }
}
