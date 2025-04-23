package com.appcenter.wnt.repository;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.CouponStock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponStockRepository extends JpaRepository<CouponStock, Long> {
    Optional<CouponStock> findByCoupon(Coupon coupon);

    // 비관적 락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select cs from CouponStock cs where cs.id = :couponId")
    Optional<CouponStock> findByIdWithPessimisticLock(Long couponId);

    // 낙관적 락
    @Lock(LockModeType.OPTIMISTIC)
    @Query("select cs from CouponStock cs where cs.id = :couponId")
    Optional<CouponStock> findByIdWithOptimisticLock(Long couponId);
}
