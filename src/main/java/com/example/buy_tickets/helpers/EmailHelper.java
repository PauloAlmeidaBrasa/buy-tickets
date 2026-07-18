package com.example.buy_tickets.helpers;

public interface EmailHelper {
    void sendPurchaseConfirmation(String userEmail, Long ticketId);
}