package com.appcenter.ticketing.service;

import com.appcenter.ticketing.domain.Reservation;
import com.appcenter.ticketing.domain.Ticket;
import com.appcenter.ticketing.domain.TicketStock;
import com.appcenter.ticketing.repository.ReservationRepository;
import com.appcenter.ticketing.repository.TicketRepository;
import com.appcenter.ticketing.repository.TicketStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockConcurrencyService{

    private final TicketStockRepository ticketStockRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void reserveTicket(Long ticketId, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()-> new RuntimeException("Ticket Not Found"));
        TicketStock ticketStock = ticketStockRepository.findByTicket(ticket).orElseThrow(()-> new RuntimeException("TicketStock Not Found"));
        ticketStock.decreaseQuantity();
        reservationRepository.save(Reservation.of(userId, ticket));
    }


}
