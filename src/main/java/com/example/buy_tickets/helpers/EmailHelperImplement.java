package com.example.buy_tickets.helpers;

import org.springframework.stereotype.Component;

@Component
public class EmailHelperImplement implements EmailHelper {

    @Override
    public void sendPurchaseConfirmation(String userEmail, Long ticketId) {
        // TODO: integrate real email provider (SES, SendGrid, etc.)
        System.out.println("Sending email to " + userEmail + " for ticket " + ticketId);
    }
}