package com.example.buy_tickets.dto.response;

public class TicketListResponse {
    private Long id;
    private Long eventId;
    private String status;
    private String reservedUntil;

    public TicketListResponse(Long id, Long eventId, String status, String reservedUntil) {
        this.id = id;
        this.eventId = eventId;
        this.status = status;
        this.reservedUntil = reservedUntil;
    }

    public Long getId() { return id; }
    public Long getEventId() { return eventId; }
    public String getStatus() { return status; }
    public String getReservedUntil() { return reservedUntil; }
}