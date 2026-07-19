package com.example.buy_tickets.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User ticket summary")
public record UserTicketResponse(
        @Schema(description = "Ticket identifier", example = "1")
        Long ticketId,
        @Schema(description = "Event name", example = "Rock in Rio")
        String eventName,
        @Schema(description = "Event date in ISO-8601", example = "2026-09-20T20:00:00")
        String eventDate
) {
}