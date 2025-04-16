package com.appcenter.ticketing.repository;

import com.appcenter.ticketing.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
