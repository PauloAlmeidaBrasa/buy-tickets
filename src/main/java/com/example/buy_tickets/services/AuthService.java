package com.example.buy_tickets.services;

import com.example.buy_tickets.dto.response.AuthResponse;
import com.example.buy_tickets.dto.request.LoginRequest;
import com.example.buy_tickets.dto.request.RegisterRequest;
import com.example.buy_tickets.models.UserEntity;
import com.example.buy_tickets.repositories.UserRepository;
import com.example.buy_tickets.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public AuthResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtHelper.createToken(user.getUsername());
        return new AuthResponse(token, "13123132");
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtHelper.createToken(user.getUsername());
        return new AuthResponse(token, "13123132");
    }
}