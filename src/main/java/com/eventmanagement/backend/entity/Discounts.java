package com.eventmanagement.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discounts {

    @Id
    @Column(name = "Code", length = 50)
    private String code;

    @Column(name = "Percentage")
    @Min(0)
    @Max(100)
    private Integer percentage;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Boolean isActive;
}
