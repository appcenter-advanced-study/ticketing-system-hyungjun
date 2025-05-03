package com.appcenter.wnt.consumer;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.CouponReservation;
import com.appcenter.wnt.domain.CouponStock;
import com.appcenter.wnt.dto.response.CouponReservationDetailResponse;
import com.appcenter.wnt.repository.CouponRepository;
import com.appcenter.wnt.repository.CouponReservationRepository;
import com.appcenter.wnt.repository.CouponStockRepository;
import com.appcenter.wnt.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final CouponStockRepository couponStockRepository;
    private final CouponReservationRepository reservationRepository;
    private final CouponRepository couponRepository;

    @KafkaListener(topics = "create_reservation", groupId = "group_1")
    @Transactional
    public void listener(String message) {
        try{
            ReservationRequest request = objectMapper.readValue(message, ReservationRequest.class);
            Long userId = request.userId();
            Long couponId = request.couponId();

            log.info("컨슈머가 받은 데이터 userId : {}, couponId : {}", userId, couponId);
            reservationRepository.findByUserIdAndCouponId(userId,couponId).ifPresent(cr ->{
                throw new RuntimeException("이미 존재하는 쿠폰입니다.");
            });

            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new RuntimeException("쿠폰이 존재하지 않습니다"));
            CouponStock stock = couponStockRepository.findByCoupon(coupon).orElseThrow(() -> new RuntimeException("재고 없음"));
            stock.decreaseQuantity();

            reservationRepository.save(CouponReservation.of(userId, coupon));
        } catch (Exception e) {
            throw new RuntimeException("kafka 메세지 처리 실패", e);
        }
    }
}
