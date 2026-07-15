package com.example.buy_tickets.services;

import org.springframework.stereotype.Service;
import com.example.buy_tickets.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.buy_tickets.models.TicketEntity;
import lombok.extern.slf4j.Slf4j;
import jakarta.transaction.Transactional;



@Service
@Slf4j
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    private TicketEntity reservedTicket;
    
    @Transactional
    public String buy(Long ticketId, Long userId) {

        reservedTicket = isTicketAvailable(ticketId);

        if (reservedTicket != null) {
            return "CALL the SQS";

        }
        return "Ticket not available";
    }

    
    @Transactional
    protected TicketEntity isTicketAvailable(Long ticketId) {
        reservedTicket = ticketRepository.findByIdToReserve(ticketId).orElse(null);
        return reservedTicket;
    }
}
