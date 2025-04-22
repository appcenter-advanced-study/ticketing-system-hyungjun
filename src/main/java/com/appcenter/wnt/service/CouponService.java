package com.appcenter.wnt.service;

import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.CouponStock;
import com.appcenter.wnt.domain.enums.CouponType;
import com.appcenter.wnt.dto.response.CouponDetailResponse;
import com.appcenter.wnt.dto.response.CouponResponse;
import com.appcenter.wnt.repository.CouponRepository;
import com.appcenter.wnt.repository.CouponStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponStockRepository couponStockRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public CouponDetailResponse createCoupon(CouponType couponType, Long quantity) {
        couponRepository.findByType(couponType)
                .ifPresent(c -> {
                    throw new RuntimeException("이미 존재하는 쿠폰입니다.");
                });

        Coupon coupon = couponRepository.save(Coupon.of(couponType));
        CouponStock couponStock = couponStockRepository.save(CouponStock.of(quantity, coupon));
        return CouponDetailResponse.of(coupon.getId(), couponType.name(), couponType.getDiscountRate(), couponType.getDescription(), couponStock.getQuantity());
    }

    @Transactional(readOnly = true)
    public CouponResponse getCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(()-> new RuntimeException("쿠폰이 존재하지 않습니다."));
        CouponType couponType = coupon.getType();
        return CouponResponse.of(coupon.getId(), couponType.name(), couponType.getDescription());
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(()-> new RuntimeException("쿠폰이 존재하지 않습니다."));
        CouponStock couponStock = couponStockRepository.findByCoupon(coupon).orElseThrow(()-> new RuntimeException("쿠폰 재고가 존재하지 않습니다."));
        couponStockRepository.delete(couponStock);
        couponRepository.delete(coupon);
    }

}
