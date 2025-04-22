package com.appcenter.wnt.service;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.CouponStock;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.domain.enums.CouponType;
import com.appcenter.wnt.repository.CouponReservationRepository;
import com.appcenter.wnt.repository.CouponRepository;
import com.appcenter.wnt.repository.CouponStockRepository;
import com.appcenter.wnt.repository.UserRepository;
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
public class CouponReservationTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponReservationRepository reservationRepository;

    @Autowired
    private CouponReservationService couponReservationService;

    @Autowired
    private CouponStockRepository couponStockRepository;

    @Autowired
    private UserRepository userRepository;

    private Coupon coupon;
    private CouponStock couponStock;
    private List<User> users;

    @BeforeEach
    void setUp() {
        coupon = couponRepository.saveAndFlush(Coupon.of(CouponType.THIRTY_PERCENT)); // enum 예시

        couponStock = couponStockRepository.saveAndFlush(CouponStock.of(100L, coupon));

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
     ** 쿠폰 예매 100건은 성공했지만 티켓 재고 필드가 꼬인 상황 -> 정합성 깨짐
     **/
    @Test
    public void 요청1000건_중_100개만_허용_테스트() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Coupon coupon = couponRepository.findAll().getFirst();
        Long couponId = coupon.getId();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            executorService.submit(() -> {
                try {
                    couponReservationService.reserveCoupon(user.getId(), couponId);
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
