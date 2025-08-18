package com.eventmanagement.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long id;
    private String eventName;
    private BigDecimal eventPrice;
    private String type;
    private int participants;
    private Long userId;       // matches service (Long not Integer)
    private String userName;
    private String userEmail;
    private Long venueId;
    private Long eventId;

    private String discountCode;
    private LocalDateTime eventStartDateTime;
    private LocalDateTime eventEndDateTime;

    // Backward compatibility constructor (used in your service)
    public BookingDTO(Long id, String eventName, BigDecimal eventPrice, String type, int participants,
                      Long userId, String userName, String userEmail) {
        this.id = id;
        this.eventName = eventName;
        this.eventPrice = eventPrice;
        this.type = type;
        this.participants = participants;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    // Explicit full-args constructor (keeps compatibility with callers expecting all fields)
    public BookingDTO(Long id, String eventName, BigDecimal eventPrice, String type, int participants,
                      Long userId, String userName, String userEmail, Long venueId, Long eventId,
                      String discountCode, LocalDateTime eventStartDateTime, LocalDateTime eventEndDateTime) {
        this.id = id;
        this.eventName = eventName;
        this.eventPrice = eventPrice;
        this.type = type;
        this.participants = participants;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.venueId = venueId;
        this.eventId = eventId;
        this.discountCode = discountCode;
        this.eventStartDateTime = eventStartDateTime;
        this.eventEndDateTime = eventEndDateTime;
    }
}
