package com.example.buy_tickets.dto.response;

public record AuthResponse(String accessToken, Long userId, String username, String email, String whatsapp) {
    public AuthResponse {
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("accessToken must not be blank");
        }
    }
}