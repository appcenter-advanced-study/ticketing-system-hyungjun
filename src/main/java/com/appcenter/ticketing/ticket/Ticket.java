package com.appcenter.ticketing.ticket;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="ticket")
public class Ticket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @Column(name = "ticket_name")
    private String name;

    @Builder
    private Ticket(String name){
        this.name = name;
    }

    public static Ticket of(String name){
        return Ticket.builder().name(name).build();
    }
}
