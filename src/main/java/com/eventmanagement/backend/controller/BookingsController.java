package com.eventmanagement.backend.controller;

import com.eventmanagement.backend.entity.Bookings;
import com.eventmanagement.backend.service.BookingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@Controller
public class BookingsController {

    @Autowired
    private BookingsService bookingsService;

    // Endpoint to create a booking
    @PostMapping("/create")
    public Bookings createBooking(
            @RequestParam Integer userId,
            @RequestParam Integer eventId,
            @RequestParam Integer participants,
            @RequestParam String bookingType,
            @RequestParam(required = false) String discountCode) {
        return bookingsService.createBooking(userId, eventId, participants, bookingType, discountCode);
    }
}
