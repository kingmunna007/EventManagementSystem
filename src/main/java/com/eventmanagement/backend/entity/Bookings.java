package com.eventmanagement.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.eventmanagement.backend.entity.BookingType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_ID" ,referencedColumnName = "id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Event_ID", referencedColumnName = "ID")
    private Events event;

    @Column(name = "Participants")
    private Integer participants;

    @Enumerated(EnumType.STRING)
    @Column(name = "BookingType")
    private BookingType bookingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DiscountCode")
    private Discounts discount;

    @Column(name = "FinalPrice", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Venue_ID", referencedColumnName = "id")
    private Venues venue;

    @Column(name = "EventStartDateTime", nullable = false)
    private LocalDateTime eventStartDateTime;

    @Column(name = "EventEndDateTime", nullable = false)
    private LocalDateTime eventEndDateTime;


    @Column(name = "BookingTime")
    private LocalDateTime bookingTime;

    @Column(name = "Status", nullable = false)
    private String status; // PENDING, CONFIRMED, CANCELLED

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = (status == null) ? "PENDING" : status;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
