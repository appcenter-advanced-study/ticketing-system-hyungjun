package com.appcenter.ticketing.ticketstock;

import com.appcenter.ticketing.ticket.Ticket;
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
}
