package com.appcenter.wnt.repository;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.enums.CouponType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByType(CouponType couponType);
}
