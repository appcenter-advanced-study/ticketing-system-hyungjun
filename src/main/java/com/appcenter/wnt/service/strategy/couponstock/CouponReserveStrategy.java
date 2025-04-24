package com.appcenter.wnt.service.strategy.couponstock;

import com.appcenter.wnt.dto.response.CouponReserveDetailResponse;

public interface CouponReserveStrategy {
    CouponReserveDetailResponse reserveCoupon(Long userId, Long couponId) throws InterruptedException;
}
