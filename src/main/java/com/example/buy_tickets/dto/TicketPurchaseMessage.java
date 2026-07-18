package com.example.buy_tickets.dto;

public record TicketPurchaseMessage(
    Long ticketId,
    Long userId,
    String userEmail,
    String userWhatsapp
) {
}