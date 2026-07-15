package com.example.buy_tickets.validators;

import org.springframework.stereotype.Component;

@Component
public class BuyTicketValidator {

    public void validate(Long ticketId, Long userId) {
        if (ticketId == null || ticketId <= 0) {
            throw new IllegalArgumentException("ticket_id must be a positive number");
        }

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("user_id must be a positive number");
        }
    }
}
