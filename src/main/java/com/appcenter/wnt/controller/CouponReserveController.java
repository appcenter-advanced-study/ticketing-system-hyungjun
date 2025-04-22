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
public class CouponReserveController {
    private final CouponReservationService couponReservationService;

    @PostMapping("/{userId}/user/{couponId}/reservation")
    public ResponseEntity<CouponReserveDetailResponse> reserveCoupon(@PathVariable("couponId") Long couponId, @PathVariable("userId") Long userId) {
        CouponReserveDetailResponse response = couponReservationService.reserveCoupon(userId, couponId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{userId}/user/{couponId}/cancel")
    public ResponseEntity<String> cancelCouponReservation(@PathVariable("couponId") Long couponId, @PathVariable("userId") Long userId) {
        couponReservationService.cancelCouponReservation(userId, couponId);
        return ResponseEntity.status(HttpStatus.OK).body("쿠폰 버리기 성공");
    }

    @GetMapping("/user/reservations")
    public ResponseEntity<List<CouponReserveResponse>> getUserReservations(@RequestParam Long userId) {
        List<CouponReserveResponse> response = couponReservationService.getUserCouponReservations(userId);
        return ResponseEntity.ok(response);
    }
}
