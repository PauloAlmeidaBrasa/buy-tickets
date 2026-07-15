package com.example.buy_tickets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.buy_tickets.models.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
