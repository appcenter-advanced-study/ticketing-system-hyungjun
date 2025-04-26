package com.appcenter.wnt.service.strategy.couponstock;


import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.response.CouponReservationDetailResponse;
import com.appcenter.wnt.repository.CouponRepository;
import com.appcenter.wnt.repository.CouponReservationRepository;
import com.appcenter.wnt.repository.UserRepository;
import com.appcenter.wnt.service.executor.NamedLockExecutor;
import com.appcenter.wnt.service.processor.CouponStockCommandProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("named")
public class NamedLockCouponReservationStrategy implements CouponReservationStrategy {
    private final CouponRepository couponRepository;
    private final CouponReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final NamedLockExecutor executor;
    private final CouponStockCommandProcessor processor;

    @Override
    public CouponReservationDetailResponse reserve(Long userId, Long couponId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다."));

        reservationRepository.findByUserIdAndCouponId(userId, couponId)
                .ifPresent(res -> {
                    throw new RuntimeException("이미 존재하는 쿠폰입니다.");
                });

        // 네임드 락을 사용한 재고 감소
        return executor.executeWithLock(couponId.toString(), () -> {
            // 내부에서 @Transactional
            return processor.tryReserve(user, coupon);
        });
    }
}
