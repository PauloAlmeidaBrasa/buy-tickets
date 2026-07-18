package com.example.buy_tickets.controllers.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.buy_tickets.dto.request.BuyTicketRequest;
import com.example.buy_tickets.services.TicketService;
import jakarta.validation.Valid;
import com.example.buy_tickets.models.TicketEntity;
import java.util.List;
import com.example.buy_tickets.dto.response.TicketListResponse;

@RestController
@RequestMapping("/api/${API_VERSION:v1}/ticket")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/list")
    public List<TicketListResponse> list() {
        return ticketService.listTickets();
    }

    @PostMapping("/buy")
    public String buyTicket(@Valid @RequestBody BuyTicketRequest request) {
        return ticketService.buy(request.getTicketId(), request.getUserId(),request.getUserEmail());
    }
}
