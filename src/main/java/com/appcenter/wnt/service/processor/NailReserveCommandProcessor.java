package com.appcenter.wnt.service.processor;

import com.appcenter.wnt.domain.NailReservation;
import com.appcenter.wnt.domain.Store;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.request.NailReserveRequest;
import com.appcenter.wnt.dto.response.NailReserveResponse;
import com.appcenter.wnt.repository.NailReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class NailReserveCommandProcessor {
    private final NailReservationRepository reservationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public NailReserveResponse tryReserve(User user, Store store, NailReserveRequest request) {
        reservationRepository.findByStoreAndReservationDateAndReservationTime(store, request.reservationDate(), request.reservationTime()).ifPresent(nr ->{
            throw new RuntimeException("이미 예약이 존재합니다.");
        });

        NailReservation nailReservation = reservationRepository.save(NailReservation.of(user,store,request.nailCategory(),request.reservationDate(),request.reservationTime()));
        return NailReserveResponse.from(nailReservation);
    }
}
