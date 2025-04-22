package com.appcenter.wnt.controller;

import com.appcenter.wnt.dto.request.CreateCouponRequest;
import com.appcenter.wnt.dto.response.CouponDetailResponse;
import com.appcenter.wnt.dto.response.CouponResponse;
import com.appcenter.wnt.service.CouponService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Coupon",description = "Coupon API")
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    // 쿠폰 생성
    @PostMapping
    public ResponseEntity<CouponDetailResponse> createCoupon(@RequestBody CreateCouponRequest request) {
        CouponDetailResponse response = couponService.createCoupon(request.couponType(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 쿠폰 조회
    @GetMapping("/{couponId}")
    public ResponseEntity<CouponResponse> getCoupon(@PathVariable Long couponId) {
        CouponResponse response = couponService.getCoupon(couponId);
        return ResponseEntity.ok(response);
    }

    // 쿠폰 삭제
    @DeleteMapping("/{couponId}")
    public ResponseEntity<String> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.status(HttpStatus.OK).body("쿠폰 삭제 성공");
    }
}
