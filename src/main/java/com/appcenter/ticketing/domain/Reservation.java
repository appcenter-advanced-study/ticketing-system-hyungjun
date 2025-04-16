package com.appcenter.ticketing.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Builder
    private Reservation(Long userId, Ticket ticket){
        this.userId = userId;
        this.ticket = ticket;
    }

    public static Reservation of(Long userId, Ticket ticket){
        return Reservation.builder().userId(userId).ticket(ticket).build();
    }
}
