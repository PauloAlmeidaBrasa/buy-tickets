package com.example.buy_tickets.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.buy_tickets.models.TicketEntity;
import com.example.buy_tickets.repositories.TicketRepository;
import com.example.buy_tickets.services.TicketService;
import java.util.List;
import com.example.buy_tickets.dto.response.TicketListResponse;
import java.time.LocalDateTime;


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
    public String buy(
        Long ticketId, 
        Long userId, 
        String userEmail,
        String userWhatsapp) {


        TicketEntity ticket =
                ticketRepository.findByIdToReserve(ticketId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ticket not found"));

        if (ticket.getStatus()
                != TicketEntity.TicketStatus.AVAILABLE) {

            return "Ticket not available";
        }

        ticket.setStatus(
                TicketEntity.TicketStatus.RESERVED);

        ticket.setReservedUntil(
                LocalDateTime.now()
                        .plusMinutes(10));

        ticketRepository.save(ticket);

        sqsPublisher.publish(
                ticketId,
                userId,
                userEmail,
                userWhatsapp);

        return "Ticket reserved successfully";
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
                ticket.getReservedUntil() != null ? ticket.getReservedUntil().toString() : null,
                ticket.getEvent().getName(),
                ticket.getEvent().getEventAddress(),
                ticket.getEvent().getEventDate().toString()
        ))
        .collect(java.util.stream.Collectors.toList());
    }
}
