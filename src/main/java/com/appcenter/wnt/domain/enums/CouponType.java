package com.appcenter.wnt.domain.enums;

import lombok.Getter;

@Getter
public enum CouponType {
    TEN_PERCENT("10% 할인 쿠폰",10),
    THIRTY_PERCENT("30% 할인 쿠폰", 30),
    FIFTY_PERCENT("50% 할인 쿠폰", 50),
    HUNDRED_PERCENT("100% 전액 할인 쿠폰",100);

    private final int discountRate;
    private final String description;

    CouponType(String description, int discountRate) {
        this.description = description;
        this.discountRate = discountRate;
    }
}
