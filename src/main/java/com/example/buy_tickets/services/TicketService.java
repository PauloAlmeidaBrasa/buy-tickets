package com.example.buy_tickets.services;

import com.example.buy_tickets.models.TicketEntity;

public interface TicketService {
    TicketEntity isTicketAvailable(Long ticketId);
    String buy(Long ticketId, Long userId);
}
