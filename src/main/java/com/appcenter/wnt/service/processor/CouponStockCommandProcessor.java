package com.appcenter.wnt.service.processor;


import com.appcenter.wnt.domain.Coupon;
import com.appcenter.wnt.domain.CouponReservation;
import com.appcenter.wnt.domain.CouponStock;
import com.appcenter.wnt.domain.User;
import com.appcenter.wnt.dto.response.CouponReservationDetailResponse;
import com.appcenter.wnt.repository.CouponReservationRepository;
import com.appcenter.wnt.repository.CouponStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 트랜잭션 안에서 예외가 발생하면 스프링은 그 트랜잭션은 rollback-only 상태로 바꿉니다.
 * 그 후에는 해당 트랜잭션 안에서 어떤 db작업도 불가능합니다.
 * -> 재시도 로직이 의미가 없어집니다.
 * 다른 클래스로 메서드를 빼서 낙관적 락 재시도 시 매번 새로운 트랜잭션을 열수 있다.
 * 그래서 따로 processor 클래스를 파서 처리하였다.
 **/
@Component
@RequiredArgsConstructor
public class CouponStockCommandProcessor {

    private final CouponStockRepository couponStockRepository;
    private final CouponReservationRepository reservationRepository;

    //@Transactional(propagation = Propagation.REQUIRES_NEW) // 현재 내 상황에서 필요가 없다.
    @Transactional
    public CouponReservationDetailResponse tryReserve(User user, Coupon coupon) {
        CouponStock stock = couponStockRepository.findByCoupon(coupon)
                .orElseThrow(() -> new RuntimeException("재고 없음"));
        stock.decreaseQuantity();

        CouponReservation couponReservation = reservationRepository.save(CouponReservation.of(user.getId(), coupon));
        return CouponReservationDetailResponse.of(coupon.getId(),couponReservation.getId(),user.getId(), coupon.getType().name(), coupon.getType().getDescription(), stock.getQuantity());
    }
}

/**
 * 트랜잭션 실패 시 rollback-only 상태가 되어 추가 작업이 불가능하기 때문에,
 * save/update 로직만 분리하여 매 재시도마다 새 트랜잭션을 시작하도록 Processor 클래스를 분리함.
 * 또한 User, Coupon 조회는 재시도 대상에서 제외하여 불필요한 오버헤드를 줄였다.
 **/

/** v1
 * 낙관적 락과 달리 REQUIRES_NEW 를 한 이유?
 * 낙관적락은 수정할 때 충돌이 있으면 실패하고 다시 요청보내는 구조이므로
 * 트랜잭션으로 묶어야 버전 충돌을 감지 할 수 있다.
 * 네임드락은 키를 잡고 로직을 처리한 후 키를 해제하는 동작을 합니다.
 * 기존 트랜잭션이 rollback-only가 되어버리면 락 해제도 실패할 수 있다.
 * 락을 잡은 뒤 트랜잭션이 무조건 새로 시작되어야 한다.
 **/

/** v2
 * v1의 설명이 틀린건 아니지만 현재 내 코드 상황에선 일반 Transactional도 상관없다..
 * 그래서 낙관적 락, 네임드 락 둘다 해당 클래스를 사용해도 무방.
 **/