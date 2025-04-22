package com.appcenter.wnt.dto.response;

import com.appcenter.wnt.domain.Store;
import lombok.Builder;

@Builder
public record StoreResponse(
        Long storeId,
        Long userId,
        String storeName
) {
    public static StoreResponse from(Store store) {
        return StoreResponse.builder()
                .storeId(store.getId())
                .userId(store.getUser().getId())
                .storeName(store.getName())
                .build();
    }
}
