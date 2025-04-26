package com.appcenter.wnt.service.nailreservation;

import com.appcenter.wnt.domain.Store;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.domain.enums.NailCategory;
import com.appcenter.wnt.domain.enums.NailReservationTime;
import com.appcenter.wnt.dto.request.NailReservationRequest;
import com.appcenter.wnt.repository.NailReservationRepository;
import com.appcenter.wnt.repository.StoreRepository;
import com.appcenter.wnt.repository.UserRepository;
import com.appcenter.wnt.service.strategy.nailreservation.NailReservationStrategyManager;
import com.appcenter.wnt.service.strategy.type.LockType;
import lombok.extern.slf4j.Slf4j;
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

@SpringBootTest
@Slf4j
public class RedisLockNailReservationTest {
    @Autowired
    private NailReservationStrategyManager nailReserveStrategyManager;

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

    /**
     * 레디스 락을 걸어서 이중 예매를 해결
     */
    @Test
    public void 네일예약_5명_동시요청_테스트() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        LockType lockType = LockType.REDIS;
        for (User user : users) {
            executorService.submit(() -> {
                try {
                    NailReservationRequest request = new NailReservationRequest(
                            user.getId(),
                            store.getId(),
                            category,
                            date,
                            time
                    );

                    nailReserveStrategyManager.reserve(lockType, request);
                } catch (RuntimeException e) {
                    System.out.println("예약 실패: " + e.getMessage());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 실제 저장된 예약 개수 확인
        long count = reservationRepository.count();

        log.info("=== 저장된 예약 수: {} ===",count);
        assertEquals(1L, count, "단 1건의 예약만 저장되어야 합니다.");
    }
}
