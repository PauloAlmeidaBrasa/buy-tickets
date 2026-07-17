package com.example.buy_tickets.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.buy_tickets.services.impl.SqsPublisherServiceImplement;
import com.example.buy_tickets.models.TicketEntity;
import com.example.buy_tickets.repositories.TicketRepository;
import com.example.buy_tickets.services.TicketService;
import java.util.List;
import com.example.buy_tickets.dto.response.TicketListResponse;


@Service
public class TicketServiceImplement implements TicketService {

    private final TicketRepository ticketRepository;
    private final SqsPublisherServiceImplement sqsPublisher;

    public TicketServiceImplement(TicketRepository ticketRepository, SqsPublisherServiceImplement sqsPublisher) {
        this.ticketRepository = ticketRepository;
        this.sqsPublisher = sqsPublisher;
    }

    @Override
    @Transactional
    public String buy(Long ticketId, Long userId) {
        TicketEntity reservedTicket = isTicketAvailable(ticketId);

        if (reservedTicket != null) {
            reservedTicket.setStatus(TicketEntity.TicketStatus.RESERVED);
            reservedTicket.setReservedUntil(java.time.LocalDateTime.now().plusMinutes(10));
            ticketRepository.save(reservedTicket);

            String messageBody = String.format("ticketId=%s,userId=%s", ticketId, userId);
            sqsPublisher.publish(ticketId, userId);
            return "Ticket reserved successfully";
        }

        return "Ticket not available";
    }

    @Override
    public TicketEntity isTicketAvailable(Long ticketId) {
        return ticketRepository.findByIdToReserve(ticketId).orElse(null);
    }
    @Override
    public List<TicketListResponse> listTickets() {
        return ticketRepository.findAll().stream()
        .map(ticket -> new TicketListResponse(
                ticket.getId(),
                ticket.getEvent().getId(),
                ticket.getStatus().name(),
                ticket.getReservedUntil() != null ? ticket.getReservedUntil().toString() : null
        ))
        .collect(java.util.stream.Collectors.toList());
    }
}
