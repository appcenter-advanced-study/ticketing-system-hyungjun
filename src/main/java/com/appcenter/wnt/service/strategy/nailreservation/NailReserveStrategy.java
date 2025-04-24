package com.appcenter.wnt.service.strategy.nailreservation;

import com.appcenter.wnt.dto.request.NailReserveRequest;
import com.appcenter.wnt.dto.response.NailReserveResponse;

public interface NailReserveStrategy {
    NailReserveResponse reserveNail(NailReserveRequest request) throws InterruptedException;
}
