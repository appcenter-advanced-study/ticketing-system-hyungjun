package com.appcenter.wnt.dto.response;

import lombok.Builder;

@Builder
public record CouponReserveDetailResponse(
        Long couponId,
        Long couponReserveId,
        Long userId,
        String type,
        long quantity
) {
    public static CouponReserveDetailResponse of(Long couponId, Long couponReserveId, Long userId, String type, long quantity) {
        return CouponReserveDetailResponse.builder()
                .couponId(couponId)
                .couponReserveId(couponReserveId)
                .userId(userId)
                .type(type)
                .quantity(quantity)
                .build();
    }
}
