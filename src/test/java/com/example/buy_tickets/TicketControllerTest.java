package com.example.buy_tickets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.buy_tickets.controllers.ticket.TicketController;
import com.example.buy_tickets.services.TicketService;
import com.example.buy_tickets.validators.BuyTicketValidator;

class TicketControllerTest {

    @Test
    void shouldBuyTicketAndReturnHelloWorld() {
        TicketController controller = new TicketController(new TicketService(), new BuyTicketValidator());

        String response = controller.buyTicket(1L, 7L);

        assertEquals("hello world", response);
    }
}
