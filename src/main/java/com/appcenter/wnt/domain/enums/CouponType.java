package com.appcenter.wnt.domain.enums;

import lombok.Getter;

@Getter
public enum CouponType {
    TEN_PERCENT(10, "10% 할인 쿠폰"),
    THIRTY_PERCENT(30, "30% 할인 쿠폰"),
    FIFTY_PERCENT(50, "50% 할인 쿠폰"),
    HUNDRED_PERCENT(100, "100% 전액 할인 쿠폰");

    private final int discountRate;
    private final String description;

    CouponType(int discountRate, String description) {
        this.discountRate = discountRate;
        this.description = description;
    }
}
