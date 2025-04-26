package com.appcenter.wnt.controller;

import com.appcenter.wnt.dto.response.CouponReservationDetailResponse;
import com.appcenter.wnt.dto.response.CouponReservationResponse;
import com.appcenter.wnt.service.CouponReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "CouponReservation",description = "CouponReservation API")
@RequestMapping("/api/coupon-reservations")
@RequiredArgsConstructor
public class CouponReservationController {
    private final CouponReservationService couponReservationService;

    @PostMapping("/users/{userId}/coupons/{couponId}/reservations")
    public ResponseEntity<CouponReservationDetailResponse> reserve(@PathVariable("userId") Long userId,
                                                                         @PathVariable("couponId") Long couponId) {
        CouponReservationDetailResponse response = couponReservationService.reserve(userId, couponId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/users/{userId}/coupons/{couponId}/cancel")
    public ResponseEntity<String> cancelReservation(@PathVariable("userId") Long userId, @PathVariable("couponId") Long couponId) {
        couponReservationService.cancelReservation(userId, couponId);
        return ResponseEntity.status(HttpStatus.OK).body("쿠폰 버리기 성공");
    }

    @GetMapping("/users/{userId}/reservations")
    public ResponseEntity<List<CouponReservationResponse>> getUserReservations(@PathVariable("userId") Long userId) {
        List<CouponReservationResponse> response = couponReservationService.getUserReservations(userId);
        return ResponseEntity.ok(response);
    }
}
