package com.appcenter.ticketing.repository;

import com.appcenter.ticketing.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
