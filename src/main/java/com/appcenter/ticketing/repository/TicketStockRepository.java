package com.appcenter.ticketing.repository;

import com.appcenter.ticketing.domain.Ticket;
import com.appcenter.ticketing.domain.TicketStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketStockRepository extends JpaRepository<TicketStock, Long> {
    Optional<TicketStock> findByTicket(Ticket ticket);
}
