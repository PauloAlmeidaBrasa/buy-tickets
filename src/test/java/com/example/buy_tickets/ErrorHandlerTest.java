package com.example.buy_tickets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.buy_tickets.controllers.ticket.TicketController;
import com.example.buy_tickets.error.ErrorHandler;
import com.example.buy_tickets.models.TicketEntity;
import com.example.buy_tickets.services.TicketService;
import java.util.List;
import com.example.buy_tickets.dto.response.TicketListResponse;
import com.example.buy_tickets.dto.response.UserTicketResponse;

class ErrorHandlerTest {

    @Test
    void shouldReturnBadRequestForInvalidBuyTicketRequest() throws Exception {
        TicketController controller = new TicketController(new StubTicketService());
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ErrorHandler())
                .build();

        String requestBody = """
                {
                  "ticketId": 0,
                  "userId": 0
                }
                """;

        mockMvc.perform(post("/api/v1/ticket/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.errors[*].field", org.hamcrest.Matchers.hasItems("ticketId", "userId", "userEmail")));
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
}
