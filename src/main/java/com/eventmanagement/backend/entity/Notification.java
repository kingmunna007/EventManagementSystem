package com.eventmanagement.backend.entity;

import com.eventmanagement.backend.entity.Users;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users user;

    private String type; // BOOKING, PAYMENT, ADMIN, etc.
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;

    // Getters and setters...
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}