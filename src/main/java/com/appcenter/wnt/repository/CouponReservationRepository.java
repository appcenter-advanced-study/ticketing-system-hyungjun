package com.appcenter.wnt.repository;

import com.appcenter.wnt.domain.CouponReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponReservationRepository extends JpaRepository<CouponReservation, Long> {
    Optional<CouponReservation> findByUserIdAndCouponId(Long userId, Long couponId);
    List<CouponReservation> findByUserId(Long userId);
}
