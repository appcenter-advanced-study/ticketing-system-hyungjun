package com.appcenter.wnt.service;


import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.producer.KafkaProducer;
import com.appcenter.wnt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class kafkaReservationService {
    private final KafkaProducer kafkaProducer;
    private final UserRepository userRepository;

    public void reserve(Long userId, Long couponId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("유저가 존재하지 않습니다."));
        kafkaProducer.create(user.getId(), couponId);
    }
}
