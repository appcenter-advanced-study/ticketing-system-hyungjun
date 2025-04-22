package com.appcenter.wnt.service;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.CouponReservation;
import com.appcenter.wnt.domain.CouponStock;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.domain.enums.CouponType;
import com.appcenter.wnt.dto.response.CouponReserveDetailResponse;
import com.appcenter.wnt.dto.response.CouponReserveResponse;
import com.appcenter.wnt.repository.CouponRepository;
import com.appcenter.wnt.repository.CouponReservationRepository;
import com.appcenter.wnt.repository.CouponStockRepository;
import com.appcenter.wnt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponReservationService {
    private final CouponStockRepository couponStockRepository;
    private final CouponRepository couponRepository;
    private final CouponReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public CouponReserveDetailResponse reserveCoupon(Long userId, Long couponId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(()-> new RuntimeException("쿠폰이 존재하지 않습니다."));
        CouponStock couponStock = couponStockRepository.findByCoupon(coupon).orElseThrow(()-> new RuntimeException("쿠폰 재고가 존재하지 않습니다."));
        reservationRepository.findByUserIdAndCouponId(userId,couponId).ifPresent(cr ->{
            throw new RuntimeException("이미 존재하는 쿠폰입니다.");
        });
        couponStock.decreaseQuantity();
        CouponReservation couponReservation = reservationRepository.save(CouponReservation.of(user.getId(), coupon));

        return CouponReserveDetailResponse.of(coupon.getId(),user.getId(), couponReservation.getId(), coupon.getType().name(), couponStock.getQuantity());
    }

    @Transactional
    public void cancelCouponReservation(Long userId, Long couponId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(()-> new RuntimeException("쿠폰이 존재하지 않습니다."));
        CouponStock couponStock = couponStockRepository.findByCoupon(coupon).orElseThrow(()-> new RuntimeException("쿠폰 재고가 존재하지 않습니다."));
        CouponReservation couponReservation = reservationRepository.findByUserIdAndCouponId(userId, couponId).orElseThrow(()-> new RuntimeException("쿠폰 에매가 존재하지 않습니다."));
        couponStock.increaseQuantity();

        reservationRepository.delete(couponReservation);
    }

    @Transactional
    public List<CouponReserveResponse> getUserCouponReservations(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        List<CouponReservation> couponReservations  = reservationRepository.findByUserId(user.getId());

        return couponReservations.stream().map(couponReservation -> {
            CouponType couponType = couponReservation.getCoupon().getType();
            return CouponReserveResponse.of(
                    user.getId(),
                    couponReservation.getId(),
                    couponType.name(),
                    couponType.getDescription());
        }).toList();
    }
}
