package com.appcenter.wnt.domain;

import com.appcenter.wnt.domain.enums.NailCategory;
import com.appcenter.wnt.domain.enums.NailReservationTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="nail_reservation",
        uniqueConstraints = @UniqueConstraint(name="uq_store_date_time",
                columnNames = {"store_id", "nail_reservation_date","nail_reservation_time"})) // jpa를 통해 unique를 걸어줄 수 있다.
public class NailReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nail_reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(name = "nail_category")
    private NailCategory nailCategory;

    @Column(name = "nail_reservation_date")
    private LocalDate reservationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "nail_reservation_time")
    private NailReservationTime reservationTime;

    @Builder
    private NailReservation(User user, Store store, NailCategory nailCategory, LocalDate reservationDate, NailReservationTime reservationTime) {
        this.user = user;
        this.store = store;
        this.nailCategory = nailCategory;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    public static NailReservation of(User user, Store store, NailCategory nailCategory, LocalDate reservationDate, NailReservationTime reservationTime) {
        return NailReservation.builder()
                .user(user)
                .store(store)
                .nailCategory(nailCategory)
                .reservationDate(reservationDate)
                .reservationTime(reservationTime).build();
    }
}
