package com.appcenter.wnt.domain.enums;

import lombok.Getter;

@Getter
public enum NailCategory {
    CARE("손케어", 20000),
    ART("아트", 50000),
    FIX("보수", 10000),
    GEL_REMOVAL("젤제거", 5000);

    private final String description;
    private final long price;

    NailCategory(String description, long price) {
        this.description = description;
        this.price = price;
    }

}
