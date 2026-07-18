package com.example.buy_tickets.helpers;

public interface WhatsAppHelper {
    void sendPurchaseConfirmation(String userWhatsapp, Long ticketId);
}