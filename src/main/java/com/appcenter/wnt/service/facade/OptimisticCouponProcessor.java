package com.appcenter.wnt.service.facade;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.CouponReservation;
import com.appcenter.wnt.domain.CouponStock;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.response.CouponReserveDetailResponse;
import com.appcenter.wnt.repository.CouponReservationRepository;
import com.appcenter.wnt.repository.CouponStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OptimisticCouponProcessor {
    private final CouponStockRepository couponStockRepository;
    private final CouponReservationRepository reservationRepository;

    // 트랜잭션 안에서 예외가 발생하면 스프링은 그 트랜잭션은 rollback-only 상태로 바꿉니다.
    // 그 후에는 해당 트랜잭션 안에서 어떤 db작업도 불가능합니다.
    // -> 재시도 로직이 의미가 없어집니다.
    // 다른 클래스로 메서드를 빼서 낙관적 락 재시도 시 매번 새로운 트랜잭션을 열수 있다.
    @Transactional
    public CouponReserveDetailResponse tryReserve(User user, Coupon coupon) {
        // 낙관적 락 적용
        CouponStock couponStock = couponStockRepository.findByIdWithOptimisticLock(coupon.getId())
                .orElseThrow(() -> new RuntimeException("재고가 존재하지 않습니다."));

        couponStock.decreaseQuantity();

        CouponReservation reservation = reservationRepository.save(
                CouponReservation.of(user.getId(), coupon)
        );

        return CouponReserveDetailResponse.of(
                coupon.getId(), user.getId(), reservation.getId(), coupon.getType().name(), couponStock.getQuantity()
        );
    }
}
