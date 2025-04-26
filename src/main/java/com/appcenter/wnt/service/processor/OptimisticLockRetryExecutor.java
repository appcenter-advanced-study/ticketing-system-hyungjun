package com.appcenter.wnt.service.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class OptimisticLockRetryExecutor {

    private static final int MAX_RETRY_COUNT = 30;

    public <T> T execute(Supplier<T> action) {
        int attempt = 0;
        while(true){   // 버전이 달라 요청이 실패하면 재시도.
            try {
                return action.get();
            }catch (ObjectOptimisticLockingFailureException e){
                if(++attempt >= MAX_RETRY_COUNT){  // 30회 이상 틀리면 예외 처리
                    throw new RuntimeException("재시도 실패: 동시성 충돌", e);
                }
            }
        }
    }
}
