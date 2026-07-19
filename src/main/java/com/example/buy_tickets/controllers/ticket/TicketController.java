package com.example.buy_tickets.controllers.ticket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.buy_tickets.dto.request.BuyTicketRequest;
import com.example.buy_tickets.dto.response.UserTicketResponse;
import com.example.buy_tickets.services.TicketService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import com.example.buy_tickets.dto.response.TicketListResponse;
import com.example.buy_tickets.error.ErrorHandler;
import com.example.buy_tickets.models.TicketEntity;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping({"/api/${API_VERSION:v1}/ticket", "/api/${API_VERSION:v1}/tickets"})
@Tag(name = "Tickets", description = "Ticket operations")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/list")
        @Operation(summary = "List tickets", description = "Returns the list of available tickets and associated event metadata.")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket list returned",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketListResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "500", description = "Unexpected error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
        })
    public List<TicketListResponse> list() {
        return ticketService.findAllByStatus(TicketEntity.TicketStatus.AVAILABLE);
    }

    @GetMapping("/user/{userId}")
        @Operation(summary = "List user tickets", description = "Returns all tickets assigned to the informed user.")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User tickets returned",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserTicketResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                content = @Content(schema = @Schema(implementation = ErrorHandler.ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = Map.class)))
        })
    public List<UserTicketResponse> listUserTickets(@PathVariable @Positive(message = "userId must be a positive number") Long userId) {
        return ticketService.findAllByUserId(userId);
    }

    @PostMapping("/buy")
        @Operation(summary = "Buy a ticket", description = "Purchases or reserves a ticket for the informed user.")
        @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase successful",
                content = @Content(schema = @Schema(implementation = String.class))),
                @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorHandler.ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "500", description = "Unexpected error",
                    content = @Content(schema = @Schema(implementation = Map.class)))
        })
    public String buyTicket(@Valid @RequestBody BuyTicketRequest request) {
        return ticketService.buy(
            request.getTicketId(), 
            request.getUserId(),
            request.getUserEmail(),
            request.getUserWhatsapp()
        );
    }
}
