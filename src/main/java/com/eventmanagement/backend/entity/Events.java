package com.eventmanagement.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "Events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name ="Name")
    private String name;

    @Column(name = "BasePrice", precision = 10, scale = 2)
    private BigDecimal basePrice;
}
