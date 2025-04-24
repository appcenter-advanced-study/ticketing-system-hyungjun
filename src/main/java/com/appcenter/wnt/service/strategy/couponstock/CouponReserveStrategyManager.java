package com.appcenter.wnt.service.strategy.couponstock;

import com.appcenter.wnt.dto.response.CouponReserveDetailResponse;
import com.appcenter.wnt.service.type.LockType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CouponReserveStrategyManager {
    private final Map<LockType, CouponReserveStrategy> strategyMap;

    public CouponReserveStrategyManager(
            @Qualifier("none") CouponReserveStrategy noLock,
            @Qualifier("pessimistic") CouponReserveStrategy pessimistic,
            @Qualifier("optimistic") CouponReserveStrategy optimistic,
            @Qualifier("named") CouponReserveStrategy named,
            @Qualifier("redis") CouponReserveStrategy redis
    ) {
        this.strategyMap = Map.of(
                LockType.NONE, noLock,
                LockType.PESSIMISTIC, pessimistic,
                LockType.OPTIMISTIC, optimistic,
                LockType.NAMED, named,
                LockType.REDIS, redis
        );
    }

    public CouponReserveDetailResponse reserveCoupon(LockType lockType, Long userId, Long couponId) throws InterruptedException {
        return strategyMap.get(lockType).reserveCoupon(userId, couponId);
    }
}
