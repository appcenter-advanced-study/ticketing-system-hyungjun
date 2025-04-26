package com.appcenter.wnt.service.strategy.couponstock;

import com.appcenter.wnt.dto.response.CouponReservationDetailResponse;
import com.appcenter.wnt.service.strategy.type.LockType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CouponReservationStrategyManager {
    private final Map<LockType, CouponReservationStrategy> strategyMap;

    public CouponReservationStrategyManager(
            @Qualifier("none") CouponReservationStrategy noLock,
            @Qualifier("pessimistic") CouponReservationStrategy pessimistic,
            @Qualifier("optimistic") CouponReservationStrategy optimistic,
            @Qualifier("named") CouponReservationStrategy named,
            @Qualifier("redis") CouponReservationStrategy redis
    ) {
        this.strategyMap = Map.of(
                LockType.NONE, noLock,
                LockType.PESSIMISTIC, pessimistic,
                LockType.OPTIMISTIC, optimistic,
                LockType.NAMED, named,
                LockType.REDIS, redis
        );
    }

    public CouponReservationDetailResponse reserve(LockType lockType, Long userId, Long couponId) throws InterruptedException {
        return strategyMap.get(lockType).reserve(userId, couponId);
    }
}
