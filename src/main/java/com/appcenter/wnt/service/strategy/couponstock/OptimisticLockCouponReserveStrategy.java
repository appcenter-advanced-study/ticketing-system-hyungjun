package com.appcenter.wnt.service.strategy.couponstock;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.response.CouponReserveDetailResponse;
import com.appcenter.wnt.repository.CouponRepository;
import com.appcenter.wnt.repository.CouponReservationRepository;
import com.appcenter.wnt.repository.UserRepository;
import com.appcenter.wnt.service.processor.OptimisticCouponProcessor;
import com.appcenter.wnt.service.processor.OptimisticLockRetryExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Qualifier("optimistic")
public class OptimisticLockCouponReserveStrategy implements CouponReserveStrategy {
    private final CouponRepository couponRepository;
    private final CouponReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final OptimisticLockRetryExecutor retryExecutor;
    private final OptimisticCouponProcessor processor;


    @Override
    public CouponReserveDetailResponse reserveCoupon(Long userId, Long couponId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(()-> new RuntimeException("쿠폰이 존재하지 않습니다."));
        reservationRepository.findByUserIdAndCouponId(userId,couponId).ifPresent(cr ->{
            throw new RuntimeException("이미 예매한 쿠폰입니다.");
        });

        // CouponStock 부분 재시도 실행 실행
        return retryExecutor.execute(() -> processor.tryReserve(user, coupon));
    }
}
