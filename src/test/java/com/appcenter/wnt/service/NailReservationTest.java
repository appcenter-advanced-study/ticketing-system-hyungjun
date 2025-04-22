package com.appcenter.wnt.service;

import com.appcenter.wnt.domain.Store;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.domain.enums.NailCategory;
import com.appcenter.wnt.domain.enums.NailReservationTime;
import com.appcenter.wnt.dto.request.NailReserveRequest;
import com.appcenter.wnt.repository.NailReservationRepository;
import com.appcenter.wnt.repository.StoreRepository;
import com.appcenter.wnt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class NailReservationTest {

    @Autowired
    private  NailReservationService nailReservationService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NailReservationRepository reservationRepository;

    private Store store;
    private List<User> users;

    private final LocalDate date = LocalDate.of(2025, 4, 23);
    private final NailReservationTime time = NailReservationTime.SIX_PM;
    private final NailCategory category = NailCategory.GEL_REMOVAL;

    @BeforeEach
    void setUp() {
        // 유저 5명 저장
        users = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> userRepository.saveAndFlush(User.of("user" + i)))
                .collect(Collectors.toList());

        // 가게 1개 저장
        store = storeRepository.saveAndFlush(Store.of(users.get(0), "나의 네일샵"));

    }

    @AfterEach
    void afterEach() {
        reservationRepository.deleteAll();
        storeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void 네일예약_중복요청_예외_테스트() {
        // 첫 번째 사용자 예약 (성공)
        NailReserveRequest firstRequest = new NailReserveRequest(
                users.get(0).getId(),
                store.getId(),
                category,
                date,
                time
        );
        nailReservationService.reserveNail(firstRequest); // 성공

        // 두 번째 사용자 예약 (실패 예상)
        NailReserveRequest secondRequest = new NailReserveRequest(
                users.get(1).getId(),
                store.getId(),
                category,
                date,
                time
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            nailReservationService.reserveNail(secondRequest);
        });

        assertEquals("이미 예약이 존재합니다.", exception.getMessage());
    }

    /**
     * 동일 가게 동일 시간대에 예매는 하나밖에 되지 않아야 한다 -> 이중예매 발생
     */
    @Test
    public void 네일예약_5명_동시요청_테스트() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (User user : users) {
            executorService.submit(() -> {
                try {
                    NailReserveRequest request = new NailReserveRequest(
                            user.getId(),
                            store.getId(),
                            category,
                            date,
                            time
                    );
                    nailReservationService.reserveNail(request);
                } catch (RuntimeException e) {
                    System.out.println("예약 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 실제 저장된 예약 개수 확인
        long count = reservationRepository.count();
        System.out.println("=== 저장된 예약 수: " + count + " ===");

        assertEquals(1L, count, "단 1건의 예약만 저장되어야 합니다.");
    }
}