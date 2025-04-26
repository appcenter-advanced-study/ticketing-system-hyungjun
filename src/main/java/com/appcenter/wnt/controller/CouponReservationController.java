package com.appcenter.wnt.controller;

import com.appcenter.wnt.dto.response.CouponReserveDetailResponse;
import com.appcenter.wnt.dto.response.CouponReserveResponse;
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
    public ResponseEntity<CouponReserveDetailResponse> reserveCoupon(@PathVariable("userId") Long userId,
                                                                     @PathVariable("couponId") Long couponId) {
        CouponReserveDetailResponse response = couponReservationService.reserveCoupon(userId, couponId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/users/{userId}/coupons/{couponId}/cancel")
    public ResponseEntity<String> cancelCouponReservation(@PathVariable("userId") Long userId, @PathVariable("couponId") Long couponId) {
        couponReservationService.cancelCouponReservation(userId, couponId);
        return ResponseEntity.status(HttpStatus.OK).body("쿠폰 버리기 성공");
    }

    @GetMapping("/users/{userId}/reservations")
    public ResponseEntity<List<CouponReserveResponse>> getUserReservations(@PathVariable("userId") Long userId) {
        List<CouponReserveResponse> response = couponReservationService.getUserCouponReservations(userId);
        return ResponseEntity.ok(response);
    }
}
