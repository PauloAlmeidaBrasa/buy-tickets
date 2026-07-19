package com.example.buy_tickets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.buy_tickets.controllers.ticket.TicketController;
import com.example.buy_tickets.dto.request.BuyTicketRequest;
import com.example.buy_tickets.dto.response.UserTicketResponse;
import com.example.buy_tickets.models.TicketEntity;
import com.example.buy_tickets.services.TicketService;
import java.util.List;
import com.example.buy_tickets.dto.response.TicketListResponse;

class TicketControllerTest {

    @Test
    void shouldBuyTicketAndReturnHelloWorld() {
        TicketController controller = new TicketController(new StubTicketService());

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

    @Test
    void shouldReturnUserTicketsFromService() {
        RecordingTicketService service = new RecordingTicketService();
        service.userTickets = List.of(new UserTicketResponse(3L, "Rock in Rio", "2026-09-20T20:00:00"));
        TicketController controller = new TicketController(service);

        List<UserTicketResponse> response = controller.listUserTickets(15L);

        assertEquals(1, response.size());
        assertEquals(15L, service.lookupUserId);
        assertEquals("Rock in Rio", response.getFirst().eventName());
    }

    private BuyTicketRequest buildRequest(Long ticketId, Long userId) {
        BuyTicketRequest request = new BuyTicketRequest();
        request.setTicketId(ticketId);
        request.setUserId(userId);
        return request;
    }

    private static class StubTicketService implements TicketService {
        @Override
        public TicketEntity isTicketAvailable(Long ticketId) {
            return null;
        }

        @Override
        public String buy(Long ticketId, Long userId, String userEmail, String userWhatsapp) {
            return "hello world";
        }
        @Override
        public List<TicketListResponse> findAllByStatus(TicketEntity.TicketStatus status) {
            return List.of();
        }

        @Override
        public List<UserTicketResponse> findAllByUserId(Long userId) {
            return List.of();
        }
    }

    private static class RecordingTicketService implements TicketService {
        private Long ticketId;
        private Long userId;
        private Long lookupUserId;
        private List<UserTicketResponse> userTickets = List.of();

        @Override
        public TicketEntity isTicketAvailable(Long ticketId) {
            return null;
        }

        @Override
        public String buy(Long ticketId, Long userId, String userEmail, String userWhatsapp) {
            this.ticketId = ticketId;
            this.userId = userId;
            return "hello world";
        }
        @Override
        public List<TicketListResponse> findAllByStatus(TicketEntity.TicketStatus status) {
            return List.of();
        }

        @Override
        public List<UserTicketResponse> findAllByUserId(Long userId) {
            this.lookupUserId = userId;
            return userTickets;
        }
    }
}
