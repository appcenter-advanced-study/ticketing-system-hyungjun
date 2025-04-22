package com.appcenter.wnt.dto.response;

import lombok.Builder;

@Builder
public record CouponDetailResponse(
        Long couponId,
        String type,
        int discountRate,
        String description,
        long quantity
) {
    public static CouponDetailResponse of(Long couponId, String type, int discountRate, String description, long quantity) {
        return CouponDetailResponse.builder()
                .couponId(couponId)
                .type(type)
                .discountRate(discountRate)
                .description(description)
                .quantity(quantity)
                .build();
    }
}
