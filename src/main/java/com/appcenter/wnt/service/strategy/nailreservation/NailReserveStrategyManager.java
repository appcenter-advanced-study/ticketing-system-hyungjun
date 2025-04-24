package com.appcenter.wnt.service.strategy.nailreservation;

import com.appcenter.wnt.dto.request.NailReserveRequest;
import com.appcenter.wnt.dto.response.NailReserveResponse;
import com.appcenter.wnt.service.type.LockType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NailReserveStrategyManager {
    private final Map<LockType, NailReserveStrategy> strategyMap;

    // 비관적락, 낙관적락은 레코드 기반 락이기 때문에, 네일 예매 내역이 존재하지 않을 수 있는 코드에서 해당 방식의 LOCK이 효율이 좋지 않다.
    public NailReserveStrategyManager(
            @Qualifier("none") NailReserveStrategy noLock,
            @Qualifier("named") NailReserveStrategy named,
            @Qualifier("redis") NailReserveStrategy redis
    ){
        this.strategyMap = Map.of(
                LockType.NONE, noLock,
                LockType.NAMED, named,
                LockType.REDIS, redis
        );
    }

    public NailReserveResponse reserveNail(LockType lockType, NailReserveRequest request) throws InterruptedException {
        return strategyMap.get(lockType).reserveNail(request);
    }
}
