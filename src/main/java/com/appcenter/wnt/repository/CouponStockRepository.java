package com.appcenter.wnt.repository;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.CouponStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponStockRepository extends JpaRepository<CouponStock, Long> {
    Optional<CouponStock> findByCoupon(Coupon coupon);
}
