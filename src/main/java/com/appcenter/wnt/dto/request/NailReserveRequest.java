package com.appcenter.wnt.dto.request;

import com.appcenter.wnt.domain.enums.NailCategory;
import com.appcenter.wnt.domain.enums.NailReservationTime;

import java.time.LocalDate;

public record NailReserveRequest(
        Long userId,
        Long storeId,
        NailCategory nailCategory,
        LocalDate reservationDate,
        NailReservationTime reservationTime
) {
}
