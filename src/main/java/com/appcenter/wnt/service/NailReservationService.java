package com.appcenter.wnt.service;

import com.appcenter.wnt.domain.NailReservation;
import com.appcenter.wnt.domain.Store;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.request.NailReservationRequest;
import com.appcenter.wnt.dto.response.NailReservationResponse;
import com.appcenter.wnt.repository.NailReservationRepository;
import com.appcenter.wnt.repository.StoreRepository;
import com.appcenter.wnt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NailReservationService {

    private final NailReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public NailReservationResponse reserve(NailReservationRequest request) {
        User user = userRepository.findById(request.userId()).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        Store store = storeRepository.findById(request.storeId()).orElseThrow(()-> new RuntimeException("존재하지 않는 가게입니다."));

        reservationRepository.findByStoreAndReservationDateAndReservationTime(store, request.reservationDate(), request.reservationTime()).ifPresent(nr ->{
            throw new RuntimeException("이미 예약이 존재합니다.");
        });

        NailReservation nailReservation = reservationRepository.save(NailReservation.of(user,store,request.nailCategory(),request.reservationDate(),request.reservationTime()));
        return NailReservationResponse.from(nailReservation);
    }

    @Transactional
    public void cancel(Long nailReservationId) {
        NailReservation nailReservation = reservationRepository.findById(nailReservationId).orElseThrow(()-> new RuntimeException("예약이 존재하지 않습니다."));
        reservationRepository.delete(nailReservation);
    }

    @Transactional(readOnly = true)
    public List<NailReservationResponse> getReservations(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        List<NailReservation> nailReservations = reservationRepository.findByUser(user);

        return nailReservations.stream().map(NailReservationResponse::from).collect(Collectors.toList());
    }
}
