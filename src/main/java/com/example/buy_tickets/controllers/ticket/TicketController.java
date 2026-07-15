package com.example.buy_tickets.controllers.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.buy_tickets.services.TicketService;
import com.example.buy_tickets.dto.request.BuyTicketRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/${API_VERSION:v1}/ticket")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buy")
    public String buyTicket(@Valid @RequestBody BuyTicketRequest request) {
        return ticketService.buy(request.getTicketId(), request.getUserId());
    }
}
