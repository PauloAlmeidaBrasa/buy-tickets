package com.example.buy_tickets.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ticket list item")
public class TicketListResponse {
    @Schema(description = "Ticket identifier", example = "1")
    private Long id;
    @Schema(description = "Event identifier", example = "10")
    private Long eventId;
    @Schema(description = "Current ticket status", example = "AVAILABLE")
    private String status;
    @Schema(description = "Reservation expiration in ISO-8601", example = "2026-07-18T15:30:00Z")
    private String reservedUntil;
    @Schema(description = "Event name", example = "Rock in Rio")
    private String eventName;
    @Schema(description = "Event address", example = "Av. Salvador Allende, 6555 - Rio de Janeiro")
    private String eventAddress;
    @Schema(description = "Event date", example = "2026-09-20T20:00:00Z")
    private String eventDate;

    public TicketListResponse(Long id, Long eventId, String status, String reservedUntil, String eventName, String eventAddress, String eventDate) {
        this.id = id;
        this.eventId = eventId;
        this.status = status;
        this.reservedUntil = reservedUntil;
        this.eventName = eventName;
        this.eventAddress = eventAddress;
        this.eventDate = eventDate;
    }

    public Long getId() { return id; }
    public Long getEventId() { return eventId; }
    public String getStatus() { return status; }
    public String getReservedUntil() { return reservedUntil; }
    public String getEventName() { return eventName; }
    public String getEventAddress() { return eventAddress; }
    public String getEventDate() { return eventDate; }
}