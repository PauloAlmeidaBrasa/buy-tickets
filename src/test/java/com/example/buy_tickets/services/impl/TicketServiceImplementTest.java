package com.example.buy_tickets.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.buy_tickets.dto.response.UserTicketResponse;
import com.example.buy_tickets.models.EventEntity;
import com.example.buy_tickets.models.TicketEntity;
import com.example.buy_tickets.repositories.TicketRepository;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplementTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SqsPublisherServiceImplement sqsPublisher;

    @InjectMocks
    private TicketServiceImplement ticketService;

    @Test
    void shouldSendSqsMessageAfterReservingTicket() {
        TicketEntity ticket = new TicketEntity();
        ticket.setId(1L);
        ticket.setStatus(TicketEntity.TicketStatus.AVAILABLE);

        when(ticketRepository.findByIdToReserve(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        String response = ticketService.buy(1L, 7L, "user@example.com", "5511999999999");

        assertEquals("Ticket reserved successfully", response);
        assertEquals(TicketEntity.TicketStatus.RESERVED, ticket.getStatus());
        assertEquals(LocalDateTime.class, ticket.getReservedUntil().getClass());

        verify(sqsPublisher).publish(1L, 7L, "user@example.com", "5511999999999");
    }

    @Test
    void shouldReturnUserTicketsMappedToSummaryResponse() {
        EventEntity event = new EventEntity();
        event.setName("Rock in Rio");
        event.setEventDate(LocalDateTime.of(2026, 9, 20, 20, 0));

        TicketEntity ticket = new TicketEntity();
        ticket.setId(5L);
        ticket.setEvent(event);

        when(ticketRepository.findAllByUserId(12L)).thenReturn(List.of(ticket));

        List<UserTicketResponse> response = ticketService.findAllByUserId(12L);

        assertEquals(1, response.size());
        assertEquals(5L, response.getFirst().ticketId());
        assertEquals("Rock in Rio", response.getFirst().eventName());
        assertEquals("2026-09-20T20:00", response.getFirst().eventDate());
    }
}
