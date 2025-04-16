package com.appcenter.ticketing.service;

import com.appcenter.ticketing.repository.ReservationRepository;
import com.appcenter.ticketing.repository.TicketRepository;
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
public class TicketOverReservationTest {

    @Autowired
    private OverReservationService reservationService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void afterEach() {
        reservationRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    /**
     * 티켓 예매 요청 1000건 중 100개의 티켓에 대해서만 예매를 허용해야하는데 100개 이상의 쿠폰이 생성 및 예매됌 -> 초과예매 발생
     */
    @Test
    public void 요청1000건_중_100개만_허용_테스트() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(long i =1; i <= threadCount; i++) {
            long userId = i;
            executorService.submit(()->{
                try{
                    reservationService.reserveTicket(userId);
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        long count = reservationRepository.count();

        // 티켓 예매는 총 100건만 허용 해야한다.
        assertEquals(100L, count);
    }
}
