package com.appcenter.wnt.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="coupon_stock")
public class CouponStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_stock_id")
    private Long id;

    @Getter
    @Column(name = "coupon_quantity")
    private Long quantity;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Builder
    private CouponStock(Long quantity, Coupon coupon){
        this.quantity = quantity;
        this.coupon = coupon;
    }

    public static CouponStock of(Long quantity, Coupon coupon){
        return CouponStock.builder().quantity(quantity).coupon(coupon).build();
    }

    public void decreaseQuantity(){
        if(this.quantity  <= 0){
            throw new RuntimeException("남은 재고가 음수일 수 없습니다.");
        }
        this.quantity -= 1;
    }

    public void increaseQuantity(){
        this.quantity += 1;
    }
}
