package com.example.buy_tickets.helpers;

import org.springframework.stereotype.Component;

@Component
public class WhatsAppHelperImplement implements WhatsAppHelper {

    @Override
    public void sendPurchaseConfirmation(String userWhatsapp, Long ticketId) {
        // TODO: integrate real WhatsApp provider (Twilio, Meta Cloud API, etc.)
        if (userWhatsapp == null || userWhatsapp.isBlank()) {
            System.out.println("No WhatsApp number provided for ticket " + ticketId + " — skipping.");
            return;
        }
        System.out.println("Sending WhatsApp message to " + userWhatsapp + " for ticket " + ticketId);
    }
}