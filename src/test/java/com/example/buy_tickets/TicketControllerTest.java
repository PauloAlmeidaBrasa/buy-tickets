package com.example.buy_tickets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.buy_tickets.controllers.ticket.TicketController;
import com.example.buy_tickets.dto.request.BuyTicketRequest;
import com.example.buy_tickets.services.TicketService;

class TicketControllerTest {

    @Test
    void shouldBuyTicketAndReturnHelloWorld() {
        TicketController controller = new TicketController(new TicketService());

        String response = controller.buyTicket(buildRequest(1L, 7L));

        assertEquals("hello world", response);
    }

    @Test
    void shouldPassValidatedIdsToService() {
        RecordingTicketService service = new RecordingTicketService();
        TicketController controller = new TicketController(service);

        String response = controller.buyTicket(buildRequest(2L, 9L));

        assertEquals("hello world", response);
        assertEquals(2L, service.ticketId);
        assertEquals(9L, service.userId);
    }

    private BuyTicketRequest buildRequest(Long ticketId, Long userId) {
        BuyTicketRequest request = new BuyTicketRequest();
        request.setTicketId(ticketId);
        request.setUserId(userId);
        return request;
    }

    private static class RecordingTicketService extends TicketService {
        private Long ticketId;
        private Long userId;

        @Override
        public String buy(Long ticketId, Long userId) {
            this.ticketId = ticketId;
            this.userId = userId;
            return "hello world";
        }
    }
}
