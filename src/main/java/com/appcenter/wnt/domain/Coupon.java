package com.appcenter.wnt.domain;

import com.appcenter.wnt.domain.enums.CouponType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="coupons")
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type")
    private CouponType type;

    @Builder
    private Coupon(CouponType type){
        this.type = type;
    }

    public static Coupon of(CouponType type){
        return Coupon.builder().type(type).build();
    }
}
