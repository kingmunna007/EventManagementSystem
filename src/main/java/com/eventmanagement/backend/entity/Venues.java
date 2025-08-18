package com.eventmanagement.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Venues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venues {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String address;
        private java.math.BigDecimal basePrice;
        private Integer capacity;
        private String imageUrl;      // Cloudinary
        private String demoVideoUrl;  // Cloudinary
}
