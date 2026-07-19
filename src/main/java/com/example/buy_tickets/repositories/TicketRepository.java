package com.example.buy_tickets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import com.example.buy_tickets.models.TicketEntity;
import java.util.Optional;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TicketEntity t WHERE t.id = :ticketId")
    Optional<TicketEntity> findByIdToReserve(@Param("ticketId") Long ticketId);
    List<TicketEntity> findAllByStatus(TicketEntity.TicketStatus status);
    @Query("SELECT t FROM TicketEntity t WHERE t.user.id = :userId")
    List<TicketEntity> findAllByUserId(@Param("userId") Long userId);






}
