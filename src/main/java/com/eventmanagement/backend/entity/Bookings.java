package com.eventmanagement.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.eventmanagement.backend.entity.BookingType;
import java.math.BigDecimal;

@Entity
@Table(name = "Bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "EventID")
    private Events event;

    @Column(name = "Participants")
    private Integer participants;

    @Enumerated(EnumType.STRING)
    @Column(name = "BookingType")
    private BookingType bookingType;

    @ManyToOne
    @JoinColumn(name = "DiscountCode")
    private Discounts discount;

    @Column(name = "FinalPrice", precision = 10, scale = 2)
    private BigDecimal finalPrice;

}
