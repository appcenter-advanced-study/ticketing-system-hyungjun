package com.appcenter.wnt.service.strategy.couponstock;

import com.appcenter.wnt.dto.response.CouponReservationDetailResponse;

public interface CouponReservationStrategy {
    CouponReservationDetailResponse reserve(Long userId, Long couponId) throws InterruptedException;
}
