package com.eventmanagement.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
        private Long id;
        private String eventName;
        private BigDecimal eventPrice;
        private String type;
        private int participants;
        private Integer userId;
        private String userName;
        private String userEmail;

        // Add this for backward compatibility:
        public BookingDTO(Long id, String eventName, BigDecimal eventPrice, String type, int participants) {
                this.id = id;
                this.eventName = eventName;
                this.eventPrice = eventPrice;
                this.type = type;
                this.participants = participants;
        }

}
