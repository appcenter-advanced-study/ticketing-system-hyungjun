package com.appcenter.wnt.dto.response;

import lombok.Builder;

@Builder
public record CouponReservationDetailResponse(
        Long couponId,
        Long couponReserveId,
        Long userId,
        String couponType,
        String couponDescription,
        long quantity
) {
    public static CouponReservationDetailResponse of(Long couponId, Long couponReserveId, Long userId, String couponType, String couponDescription, long quantity) {
        return CouponReservationDetailResponse.builder()
                .couponId(couponId)
                .couponReserveId(couponReserveId)
                .userId(userId)
                .couponType(couponType)
                .couponDescription(couponDescription)
                .quantity(quantity)
                .build();
    }
}
