package com.appcenter.wnt.dto.response;

import lombok.Builder;

@Builder
public record CouponResponse(
        Long couponId,
        String type,
        String description
) {
    public static CouponResponse of(Long couponId, String type, String description) {
        return CouponResponse.builder()
                .couponId(couponId)
                .type(type)
                .description(description)
                .build();
    }
}
