package com.eventmanagement.backend.controller;

import com.eventmanagement.backend.DTO.BookingDTO;
import com.eventmanagement.backend.annotations.AdminOnly;
import com.eventmanagement.backend.entity.Bookings;
import com.eventmanagement.backend.service.BookingsService;
import com.eventmanagement.backend.DTO.BookingCreateRequest;
import com.eventmanagement.backend.entity.Users;
import com.eventmanagement.backend.repository.UsersRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingsController {

    @Autowired
    private BookingsService bookingsService;

    @Autowired
    private UsersRepository usersRepository;

    @AdminOnly
    @GetMapping("")
    public List<BookingDTO> getAllBookings() {
        return bookingsService.getAllBookingDTOs();
    }

//    @PostMapping("/create")
//    public Bookings createBooking(
//            @RequestParam Integer userId,
//            @RequestParam Integer eventId,
//            @RequestParam Integer participants,
//            @RequestParam String bookingType,
//            @RequestParam(required = false) String discountCode) {
//        return bookingsService.createBooking(userId, eventId, participants, bookingType, discountCode);
//    }

    @GetMapping("/user")
    public List<BookingDTO> getBookingsByUserId(@RequestParam Integer userId) {
        return bookingsService.getBookingsByUserId(userId);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<Bookings> updateBooking(
            @PathVariable Integer bookingId,
            @RequestParam(required = false) Integer participants,
            @RequestParam(required = false) String bookingType,
            @RequestParam(required = false) String discountCode) {
        try {
            Bookings updated = bookingsService.updateBooking(bookingId, participants, bookingType, discountCode);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer bookingId) {
        try {
            bookingsService.deleteById(bookingId);
            return ResponseEntity.ok().body("Booking cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found or could not be deleted");
        }
    }

    @PostMapping("/create-json")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createBookingJson(@RequestBody BookingCreateRequest request) {
        try {
            Integer userId = request.getUserId() != null ? request.getUserId().intValue() : null;

            if (userId == null) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated()) {
                    String principal = auth.getName();
                    if (principal != null) {
                        Users u = usersRepository.findByUsernameOrEmail(principal).orElse(null);
                        if (u != null) userId = u.getId() != null ? u.getId().intValue() : null;
                    }
                }
            }
            if (userId == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");

        Bookings booking = bookingsService.createBooking(
            userId,
            request.getEventId() != null ? request.getEventId().intValue() : null,
            request.getParticipants(),
            request.getBookingType(),
            request.getDiscountCode(),
            request.getVenueId() != null ? request.getVenueId().intValue() : null,
            request.getEventStartDateTime(),
            request.getEventEndDateTime(),
            request.getFinalPrice()
        );
            return ResponseEntity.ok(booking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : "Booking failed";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    @PostMapping("/calculate-price")
    public com.eventmanagement.backend.DTO.PriceCalculationResponse calculatePrice(
            @RequestBody com.eventmanagement.backend.DTO.PriceCalculationRequest request) {
        return bookingsService.calculatePrice(
                request.getEventId(),
                request.getParticipants(),
                request.getVenueId(),
                request.getBookingType(),
                request.getDiscountCode(),
                request.getBookingStart(),
                request.getBookingEnd()
        );
    }
}
