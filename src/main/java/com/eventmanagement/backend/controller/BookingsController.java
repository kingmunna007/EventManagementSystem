package com.eventmanagement.backend.controller;

import com.eventmanagement.backend.DTO.BookingDTO;
import com.eventmanagement.backend.entity.Bookings;
import com.eventmanagement.backend.service.BookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Controller
public class BookingsController {

    @Autowired
    private BookingsService bookingsService;


    @GetMapping("")
    public List<BookingDTO> getAllBookings() {
        return bookingsService.getAllBookingDTOs();
    }

    @PostMapping("/create")
    public Bookings createBooking(
            @RequestParam Integer userId,
            @RequestParam Integer eventId,
            @RequestParam Integer participants,
            @RequestParam String bookingType,
            @RequestParam(required = false) String discountCode) {
        return bookingsService.createBooking(userId, eventId, participants, bookingType, discountCode);
    }
    @GetMapping("/user")
    public List<BookingDTO> getBookingsByUserId(@RequestParam Integer userId) {
        return bookingsService.getBookingsByUserId(userId);
    }
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        try {
            bookingsService.deleteById(bookingId);
            return ResponseEntity.ok().body("Booking cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found or could not be deleted");
        }
    }

}
