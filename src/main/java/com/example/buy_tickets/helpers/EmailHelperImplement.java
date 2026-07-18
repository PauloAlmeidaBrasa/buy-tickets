package com.example.buy_tickets.helpers.impl;

import org.springframework.stereotype.Component;
import com.example.buy_tickets.helpers.EmailHelper;

@Component
public class EmailHelperImplement implements EmailHelper {

    @Override
    public void sendPurchaseConfirmation(String userEmail, Long ticketId) {
        // TODO: integrate real email provider (SES, SendGrid, etc.)
        System.out.println("Sending email to " + userEmail + " for ticket " + ticketId);
    }
}