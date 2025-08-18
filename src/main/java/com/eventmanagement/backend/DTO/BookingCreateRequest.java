package com.eventmanagement.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateRequest {
    private Long userId;
    private Long eventId;
    private Long venueId;
    private String bookingType;       // still String, converted to Enum in service
    private int participants;
    private String discountCode;      // optional discount
    private LocalDateTime eventStartDateTime;
    private LocalDateTime eventEndDateTime;
    private BigDecimal finalPrice;
}

