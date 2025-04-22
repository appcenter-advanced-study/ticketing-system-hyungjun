package com.appcenter.wnt.dto.response;

import lombok.Builder;

@Builder
public record CouponReserveResponse(
        Long userId,
        Long couponReserveId,
        String couponType,
        String couponDescription
) {
    public static CouponReserveResponse of(Long userId, Long couponReserveId, String couponType, String couponDescription) {
        return CouponReserveResponse.builder()
                .userId(userId)
                .couponReserveId(couponReserveId)
                .couponType(couponType)
                .couponDescription(couponDescription)
                .build();
    }
}
