package com.appcenter.wnt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="coupon_reservation")
public class CouponReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_reservation_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Builder
    private CouponReservation(Long userId, Coupon coupon){
        this.userId = userId;
        this.coupon = coupon;
    }

    public static CouponReservation of(Long userId, Coupon coupon){
        return CouponReservation.builder().userId(userId).coupon(coupon).build();
    }
}
