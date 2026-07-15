package com.example.buy_tickets.controllers.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.buy_tickets.services.TicketService;
import com.example.buy_tickets.validators.BuyTicketValidator;

@RestController
@RequestMapping("/api/${API_VERSION:v1}/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final BuyTicketValidator buyTicketValidator;

    @Autowired
    public TicketController(TicketService ticketService, BuyTicketValidator buyTicketValidator) {
        this.ticketService = ticketService;
        this.buyTicketValidator = buyTicketValidator;
    }

    @GetMapping("/buy")
    public String buyTicket(@RequestParam("ticket_id") Long ticketId,
                            @RequestParam("user_id") Long userId) {
        buyTicketValidator.validate(ticketId, userId);
        return ticketService.buy();
    }
}
