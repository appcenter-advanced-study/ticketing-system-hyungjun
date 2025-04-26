package com.appcenter.wnt.service.strategy.nailreservation;

import com.appcenter.wnt.dto.request.NailReservationRequest;
import com.appcenter.wnt.dto.response.NailReservationResponse;

public interface NailReservationStrategy {
    NailReservationResponse reserve(NailReservationRequest request) throws InterruptedException;
}
