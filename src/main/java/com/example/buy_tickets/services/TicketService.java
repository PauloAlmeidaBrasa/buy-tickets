package com.example.buy_tickets.services;

import com.example.buy_tickets.models.TicketEntity;
import java.util.List;
import com.example.buy_tickets.dto.response.TicketListResponse;


public interface TicketService {
    TicketEntity isTicketAvailable(Long ticketId);
    String buy(Long ticketId, Long userId, String userEmail, String userWhatsapp);
    List<TicketListResponse> listTickets();
}
