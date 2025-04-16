package com.appcenter.ticketing.service;

import com.appcenter.ticketing.domain.Reservation;
import com.appcenter.ticketing.domain.Ticket;
import com.appcenter.ticketing.repository.ReservationRepository;
import com.appcenter.ticketing.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OverReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public void reserveTicket(Long userId){
        long count  = reservationRepository.count();

        if(count > 100){
            return;
        }
        Ticket ticket = ticketRepository.save(Ticket.of("초과예매 티켓"));
        reservationRepository.save(Reservation.of(userId,ticket));
    }

}
