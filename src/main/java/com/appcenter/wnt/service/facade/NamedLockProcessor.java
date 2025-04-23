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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockProcessor {

    private final CouponStockRepository couponStockRepository;
    private final CouponReservationRepository reservationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CouponReserveDetailResponse decreaseStockAndSaveReservation(User user, Coupon coupon) {
        CouponStock stock = couponStockRepository.findByCoupon(coupon)
                .orElseThrow(() -> new RuntimeException("재고 없음"));
        stock.decreaseQuantity();

        CouponReservation couponReservation = reservationRepository.save(CouponReservation.of(user.getId(), coupon));
        return CouponReserveDetailResponse.of(coupon.getId(),couponReservation.getId(),user.getId(), coupon.getType().name(), stock.getQuantity());
    }
}
