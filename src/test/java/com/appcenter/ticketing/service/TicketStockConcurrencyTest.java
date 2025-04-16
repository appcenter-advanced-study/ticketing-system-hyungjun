package com.appcenter.ticketing.service;

import com.appcenter.ticketing.domain.Ticket;
import com.appcenter.ticketing.domain.TicketStock;
import com.appcenter.ticketing.repository.ReservationRepository;
import com.appcenter.ticketing.repository.TicketRepository;
import com.appcenter.ticketing.repository.TicketStockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TicketStockConcurrencyTest {
    @Autowired
    private StockConcurrencyService reservationService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketStockRepository ticketStockRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = ticketRepository.saveAndFlush(Ticket.of("문제의 티켓"));
        ticketStockRepository.saveAndFlush(TicketStock.of(100L, ticket));
    }

    @AfterEach
    void afterEach() {
        ticketStockRepository.deleteAll();
        reservationRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    /**
     * 티켓 예매 100건은 성공했지만 티켓 재고 필드가 꼬인 상황 -> 정합성 깨짐
     */
    @Test
    public void 티켓_100개_재고_감소_테스트() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(long i =1; i <= threadCount; i++) {
            final long userId = i;
            executorService.submit(()->{
                try{
                    reservationService.reserveTicket(ticket.getId(), userId);
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        TicketStock ticketStock = ticketStockRepository.findByTicket(ticket).orElseThrow(()-> new RuntimeException("TicketStock Not Found"));

        // 모든 예매가 수행 되었으면 티켓 재고는 0개가 남아야 한다.
        assertEquals(0L, ticketStock.getQuantity());
    }
}