package com.eventmanagement.backend.entity;

import com.eventmanagement.backend.entity.Users;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users user;

    @ManyToOne
    private Events event;

    private Integer rating; // 1-5
    @Column(length = 1000)
    private String comment;

    private LocalDateTime createdAt;

    // Getters and setters...
}