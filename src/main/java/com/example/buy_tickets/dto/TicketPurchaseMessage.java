package com.example.buy_tickets.dto;

public class TicketPurchaseMessage {

    private Long ticketId;
    private Long userId;
    private String userEmail;
    private String userWhatsapp;

    public TicketPurchaseMessage() {
    }

    public TicketPurchaseMessage(Long ticketId, Long userId, String userEmail, String userWhatsapp) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userWhatsapp = userWhatsapp;
    }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserWhatsapp() { return userWhatsapp; }
    public void setUserWhatsapp(String userWhatsapp) { this.userWhatsapp = userWhatsapp; }
}