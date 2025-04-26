package com.appcenter.wnt.dto.response;

import lombok.Builder;

@Builder
public record CouponReservationResponse(
        Long userId,
        Long couponReserveId,
        String couponType,
        String couponDescription
) {
    public static CouponReservationResponse of(Long userId, Long couponReserveId, String couponType, String couponDescription) {
        return CouponReservationResponse.builder()
                .userId(userId)
                .couponReserveId(couponReserveId)
                .couponType(couponType)
                .couponDescription(couponDescription)
                .build();
    }
}
