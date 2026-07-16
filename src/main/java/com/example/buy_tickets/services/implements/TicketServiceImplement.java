package com.example.buy_tickets.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.buy_tickets.models.TicketEntity;
import com.example.buy_tickets.repositories.TicketRepository;
import com.example.buy_tickets.services.TicketService;

@Service
public class TicketServiceImplement implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImplement(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public String buy(Long ticketId, Long userId) {
        TicketEntity reservedTicket = isTicketAvailable(ticketId);

        if (reservedTicket != null) {
            reservedTicket.setStatus(TicketEntity.TicketStatus.RESERVED);
            reservedTicket.setReservedUntil(java.time.LocalDateTime.now().plusMinutes(10));
            ticketRepository.save(reservedTicket);
            return "CALL the SQS";
        }

        return "Ticket not available";
    }

    @Override
    public TicketEntity isTicketAvailable(Long ticketId) {
        return ticketRepository.findByIdToReserve(ticketId).orElse(null);
    }
}
