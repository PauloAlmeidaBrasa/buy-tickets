package com.example.buy_tickets.dto.response;

public class TicketListResponse {
    private Long id;
    private Long eventId;
    private String status;
    private String reservedUntil;
    private String eventName;
    private String eventAddress;
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