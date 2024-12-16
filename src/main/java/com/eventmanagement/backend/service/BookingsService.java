package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.*;
import com.eventmanagement.backend.repository.BookingsRepository;
import com.eventmanagement.backend.repository.DiscountsRepository;
import com.eventmanagement.backend.repository.EventsRepository;
import com.eventmanagement.backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BookingsService {
    @Autowired
    private BookingsRepository bookingsRepository;

    @Autowired
    private EventsRepository eventRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private DiscountsRepository discountRepository;

    @Autowired
    private DiscountsService discountService;

    // Method to create a booking
    public Bookings createBooking(Integer userId, Integer eventId, Integer participants, String bookingType, String discountCode) {
        // Fetch the user, event, and discount
        Users user = usersRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Events event = eventRepository.findById(Long.valueOf(eventId)).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        Discounts discount = (Discounts) discountRepository.findByCode(discountCode).orElse(null);

        // Calculate the base price based on participants and booking type (you can add your own logic here)
        BigDecimal basePrice = event.getBasePrice().multiply(BigDecimal.valueOf(participants));

        // Apply discount if available
        BigDecimal finalPrice = discountService.applyDiscount(basePrice.doubleValue(), discountCode);

        // Create the booking object
        Bookings booking = new Bookings();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setParticipants(participants);
        booking.setBookingType(BookingType.valueOf(bookingType)); // Assuming BookingType is an enum
        booking.setDiscount(discount);
        booking.setFinalPrice(finalPrice);

        // Save the booking
        return bookingsRepository.save(booking);
    }
}
