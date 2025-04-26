package com.appcenter.wnt.service.strategy.nailreservation;

import com.appcenter.wnt.dto.request.NailReservationRequest;
import com.appcenter.wnt.dto.response.NailReservationResponse;
import com.appcenter.wnt.service.strategy.type.LockType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NailReservationStrategyManager {
    private final Map<LockType, NailReservationStrategy> strategyMap;

    // 비관적락, 낙관적락은 레코드 기반 락이기 때문에, 네일 예매 내역이 존재하지 않을 수 있는 상황에서 해당 방식의 LOCK 이 문제가 발생할 수 있다.
    public NailReservationStrategyManager(
            @Qualifier("none") NailReservationStrategy noLock,
            @Qualifier("named") NailReservationStrategy named,
            @Qualifier("redis") NailReservationStrategy redis
    ){
        this.strategyMap = Map.of(
                LockType.NONE, noLock,
                LockType.NAMED, named,
                LockType.REDIS, redis
        );
    }

    public NailReservationResponse reserve(LockType lockType, NailReservationRequest request) throws InterruptedException {
        return strategyMap.get(lockType).reserve(request);
    }
}
