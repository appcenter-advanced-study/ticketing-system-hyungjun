package com.appcenter.ticketing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="ticket_stock")
public class TicketStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_stock_id")
    private Long id;

    @Getter
    @Column(name = "ticket_quantity")
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Builder
    private TicketStock(Long quantity, Ticket ticket){
        this.quantity = quantity;
        this.ticket = ticket;
    }

    public static TicketStock of(Long quantity, Ticket ticket){
        return TicketStock.builder().quantity(quantity).ticket(ticket).build();
    }

    public void decreaseQuantity(){
        if(this.quantity  <= 0){
            throw new RuntimeException("Bad quantity");
        }
        this.quantity -= 1;
    }
}
