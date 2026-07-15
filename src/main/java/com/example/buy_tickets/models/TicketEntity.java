package com.example.buy_tickets.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class TicketEntity {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    // @Column(nullable = false)
    // private String status;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @Column(name = "reserved_until")
    private LocalDateTime reservedUntil;

    public TicketEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
    public enum TicketStatus {
        AVAILABLE,
        RESERVED,
        SOLD
    }
    public LocalDateTime getReservedUntil() {
        return reservedUntil;
    }
    public void setReservedUntil(LocalDateTime reservedUntil) {
        this.reservedUntil = reservedUntil;
    }  
}
