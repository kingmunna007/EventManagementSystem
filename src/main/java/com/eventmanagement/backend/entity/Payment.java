package com.eventmanagement.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Bookings booking;

    private BigDecimal amount;
    private String paymentStatus; // PENDING, PAID, FAILED, REFUNDED
    private String paymentMethod; // CARD, UPI, etc.
    private String transactionId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters...
}