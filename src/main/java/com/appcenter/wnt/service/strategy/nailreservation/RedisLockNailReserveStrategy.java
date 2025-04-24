package com.appcenter.wnt.service.strategy.nailreservation;

import com.appcenter.wnt.domain.Store;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.request.NailReserveRequest;
import com.appcenter.wnt.dto.response.NailReserveResponse;
import com.appcenter.wnt.repository.RedisLockRepository;
import com.appcenter.wnt.repository.StoreRepository;
import com.appcenter.wnt.repository.UserRepository;
import com.appcenter.wnt.repository.namedlock.NamedLockRepository;
import com.appcenter.wnt.service.processor.NailReserveCommandProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Qualifier("redis")
public class RedisLockNailReserveStrategy implements NailReserveStrategy {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final RedisLockRepository redisLockRepository;
    private final NailReserveCommandProcessor nailReserveCommandProcessor;

    @Override
    public NailReserveResponse reserveNail(NailReserveRequest request) throws InterruptedException {
        User user = userRepository.findById(request.userId()).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        Store store = storeRepository.findById(request.storeId()).orElseThrow(()-> new RuntimeException("존재하지 않는 가게입니다."));

        // 레디스 락을 사용한 네일 서비스 예약
        return tryReserve(user, store, request);
    }

    private NailReserveResponse tryReserve(User user, Store store,NailReserveRequest request) throws InterruptedException {
        String key = store.getId() + "_" + request.reservationDate().toString() + "_" + request.reservationTime().toString();
        while(!redisLockRepository.lock(key)){
            log.info("락 획득 완료");
            Thread.sleep(100);
        }
        try {
            return nailReserveCommandProcessor.tryReserve(user,store,request);
        }finally {
            redisLockRepository.unlock(key);
        }
    }
}