package com.example.buy_tickets.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BuyTicketRequest {

    @NotNull(message = "Ticket ID is required")
    @Positive(message = "Ticket ID must be a positive number")
    @Schema(description = "Ticket identifier", example = "1")
    private Long ticketId;

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    @Schema(description = "Authenticated user identifier", example = "12")
    private Long userId;

    @NotNull(message = "User Email is required")
    @Schema(description = "Buyer e-mail", example = "john.doe@example.com")
    private String userEmail;

    @Schema(description = "Buyer WhatsApp contact", example = "+5511999999999")
    private String userWhatsapp;

    // getters and setters
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserWhatsapp() { return userWhatsapp; }
    public void setUserWhatsapp(String userWhatsapp) { 
        this.userWhatsapp = userWhatsapp;
    }
}
