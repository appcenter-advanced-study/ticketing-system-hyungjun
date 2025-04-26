package com.appcenter.wnt.service.couponstock;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.CouponStock;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.domain.enums.CouponType;
import com.appcenter.wnt.repository.CouponRepository;
import com.appcenter.wnt.repository.CouponReservationRepository;
import com.appcenter.wnt.repository.CouponStockRepository;
import com.appcenter.wnt.repository.UserRepository;
import com.appcenter.wnt.service.strategy.couponstock.CouponReservationStrategyManager;
import com.appcenter.wnt.service.strategy.type.LockType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Slf4j
public class RedisLockCouponStockTest {
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponReservationRepository reservationRepository;

    @Autowired
    private CouponReservationStrategyManager couponReserveStrategyManager;

    @Autowired
    private CouponStockRepository couponStockRepository;

    @Autowired
    private UserRepository userRepository;

    private Coupon coupon;
    private List<User> users;

    @BeforeEach
    void setUp() {
        coupon = couponRepository.saveAndFlush(Coupon.of(CouponType.THIRTY_PERCENT)); // enum 예시

        couponStockRepository.saveAndFlush(CouponStock.of(100L, coupon));

        users = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> userRepository.saveAndFlush(User.of("user" + i)))
                .collect(Collectors.toList());
    }

    @AfterEach
    void afterEach() {
        reservationRepository.deleteAll();
        couponStockRepository.deleteAll();
        couponRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     ** 쿠폰 예매 100건을 레디스 락으로 동시성 문제 해결
     **/
    @Test
    public void 요청100건_중_재고_테스트() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long couponId = coupon.getId();

        LockType lockType = LockType.REDIS;
        for (User user : users) {
            executorService.submit(() -> {
                try {
                    couponReserveStrategyManager.reserve(lockType, user.getId(), couponId);
                } catch (Exception e) {
                    log.info("쿠폰 예매중 에러 발생!");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        CouponStock couponStock = couponStockRepository.findByCoupon(coupon).orElseThrow(()-> new RuntimeException("쿠폰 재고 존재 x"));

        // 모든 예매가 수행 되었으면 티켓 재고는 0개가 남아야 한다.
        assertEquals(0L, couponStock.getQuantity());
    }
}
