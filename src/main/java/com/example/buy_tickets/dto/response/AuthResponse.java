package com.example.buy_tickets.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response payload")
public record AuthResponse(
        @Schema(description = "JWT bearer token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,
        @Schema(description = "Authenticated user ID", example = "12")
        Long userId,
        @Schema(description = "Authenticated username", example = "johndoe")
        String username,
        @Schema(description = "Authenticated e-mail", example = "john.doe@example.com")
        String email,
        @Schema(description = "Authenticated user WhatsApp", example = "+5511999999999")
        String whatsapp
) {
    public AuthResponse {
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("accessToken must not be blank");
        }
    }
}