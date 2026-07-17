package com.example.buy_tickets.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.buy_tickets.dto.request.LoginRequest;
import com.example.buy_tickets.services.AuthService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/${API_VERSION:v1}/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        return "13";
    }
}
