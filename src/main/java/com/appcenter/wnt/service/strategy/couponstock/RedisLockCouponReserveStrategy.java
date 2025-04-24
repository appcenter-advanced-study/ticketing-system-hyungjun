package com.appcenter.wnt.service.strategy.couponstock;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.response.CouponReserveDetailResponse;
import com.appcenter.wnt.repository.CouponRepository;
import com.appcenter.wnt.repository.CouponReservationRepository;
import com.appcenter.wnt.repository.RedisLockRepository;
import com.appcenter.wnt.repository.UserRepository;
import com.appcenter.wnt.service.processor.CouponStockCommandProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("redis")
public class RedisLockCouponReserveStrategy implements CouponReserveStrategy {
    private final CouponRepository couponRepository;
    private final CouponReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RedisLockRepository redisLockRepository;
    private final CouponStockCommandProcessor couponStockCommandProcessor;

    @Override
    public CouponReserveDetailResponse reserveCoupon(Long userId, Long couponId) throws InterruptedException {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(()-> new RuntimeException("쿠폰이 존재하지 않습니다."));
        reservationRepository.findByUserIdAndCouponId(userId,couponId).ifPresent(cr ->{
            throw new RuntimeException("이미 존재하는 쿠폰입니다.");
        });

        return tryReserve(user,coupon);
    }

    private CouponReserveDetailResponse tryReserve(User user, Coupon coupon) throws InterruptedException {
        while(!redisLockRepository.lock(coupon.getId().toString())){
            log.info("락 획득 완료1");
            Thread.sleep(100);
        }
        try {
            return couponStockCommandProcessor.decreaseStockAndSaveReservation(user,coupon);
        }finally {
            redisLockRepository.unlock(coupon.getId().toString());
        }
    }
}
