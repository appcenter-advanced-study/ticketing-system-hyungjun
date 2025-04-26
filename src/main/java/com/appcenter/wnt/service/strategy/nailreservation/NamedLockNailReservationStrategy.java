package com.appcenter.wnt.service.strategy.nailreservation;

import com.appcenter.wnt.domain.Store;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.request.NailReservationRequest;
import com.appcenter.wnt.dto.response.NailReservationResponse;
import com.appcenter.wnt.repository.StoreRepository;
import com.appcenter.wnt.repository.UserRepository;
import com.appcenter.wnt.service.executor.NamedLockExecutor;
import com.appcenter.wnt.service.processor.NailReservationCommandProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Qualifier("named")
public class NamedLockNailReservationStrategy implements NailReservationStrategy {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final NamedLockExecutor executor;
    private final NailReservationCommandProcessor namedLockProcessor;

    @Override
    public NailReservationResponse reserve(NailReservationRequest request) {
        User user = userRepository.findById(request.userId()).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        Store store = storeRepository.findById(request.storeId()).orElseThrow(()-> new RuntimeException("존재하지 않는 가게입니다."));

        String key = store.getId() + "_" + request.reservationDate().toString() + "_" + request.reservationTime().toString();
        // 네임드 락을 사용한 네일 서비스 예약
        return executor.executeWithLock(key, () -> {
            // 내부에서 @Transactional
            return namedLockProcessor.tryReserve(user, store, request);
        });
    }
}
