package com.example.buy_tickets.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.buy_tickets.config.AwsSqsHelper;
import com.example.buy_tickets.models.TicketEntity;
import com.example.buy_tickets.repositories.TicketRepository;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplementTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private AwsSqsHelper awsSqsHelper;

    @InjectMocks
    private TicketServiceImplement ticketService;

    @Test
    void shouldSendSqsMessageAfterReservingTicket() {
        TicketEntity ticket = new TicketEntity();
        ticket.setId(1L);
        ticket.setStatus(TicketEntity.TicketStatus.AVAILABLE);

        when(ticketRepository.findByIdToReserve(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        String response = ticketService.buy(1L, 7L);

        assertEquals("Ticket reserved successfully", response);
        assertEquals(TicketEntity.TicketStatus.RESERVED, ticket.getStatus());
        assertEquals(LocalDateTime.class, ticket.getReservedUntil().getClass());

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(awsSqsHelper).sendReservationMessage(messageCaptor.capture());
        assertEquals(true, messageCaptor.getValue().contains("ticketId=1"));
        assertEquals(true, messageCaptor.getValue().contains("userId=7"));
    }
}
