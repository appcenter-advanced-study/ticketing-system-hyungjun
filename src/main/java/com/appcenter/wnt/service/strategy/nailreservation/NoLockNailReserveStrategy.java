package com.appcenter.wnt.service.strategy.nailreservation;

import com.appcenter.wnt.domain.NailReservation;
import com.appcenter.wnt.domain.Store;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.request.NailReserveRequest;
import com.appcenter.wnt.dto.response.NailReserveResponse;
import com.appcenter.wnt.repository.NailReservationRepository;
import com.appcenter.wnt.repository.StoreRepository;
import com.appcenter.wnt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Qualifier("none")
public class NoLockNailReserveStrategy implements NailReserveStrategy {
    private final NailReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public NailReserveResponse reserveNail(NailReserveRequest request) {
        User user = userRepository.findById(request.userId()).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        Store store = storeRepository.findById(request.storeId()).orElseThrow(()-> new RuntimeException("존재하지 않는 가게입니다."));

        reservationRepository.findByStoreAndReservationDateAndReservationTime(store, request.reservationDate(), request.reservationTime()).ifPresent(nr ->{
            throw new RuntimeException("이미 예약이 존재합니다.");
        });

        NailReservation nailReservation = reservationRepository.save(NailReservation.of(user,store,request.nailCategory(),request.reservationDate(),request.reservationTime()));
        return NailReserveResponse.from(nailReservation);
    }
}
