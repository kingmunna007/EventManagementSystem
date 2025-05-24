package com.eventmanagement.backend.service;

import com.eventmanagement.backend.DTO.BookingDTO;
import com.eventmanagement.backend.entity.*;
import com.eventmanagement.backend.repository.BookingsRepository;
import com.eventmanagement.backend.repository.DiscountsRepository;
import com.eventmanagement.backend.repository.EventsRepository;
import com.eventmanagement.backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingsService {
    @Autowired
    private BookingsRepository bookingsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private DiscountsRepository discountsRepository;

    @Autowired
    private DiscountsService discountsService;

    // Method to create a booking
    public Bookings createBooking(Integer userId, Integer eventId, Integer participants, String bookingType, String discountCode) {

        Users user = usersRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Events event = eventsRepository.findById(Long.valueOf(eventId)).orElseThrow(() -> new IllegalArgumentException("Event not found"));
        Discounts discount = (Discounts) discountsRepository.findByCode(discountCode).orElse(null);

        // Calculate the base price based on participants and booking type (you can add your own logic here)
        BigDecimal basePrice = event.getBasePrice().multiply(BigDecimal.valueOf(participants));

        // Apply discount if available
        BigDecimal finalPrice = discountsService.applyDiscount(basePrice.doubleValue(), discountCode);

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
    public List<BookingDTO> getBookingsByUserId(Integer userId) {
        // In your service or controller method:
        List<Bookings> bookings = bookingsRepository.findByUserId(userId);
        List<BookingDTO> bookingDTOs = bookings.stream().map(booking -> {
            BookingDTO dto = new BookingDTO();
            dto.setId(Long.valueOf(booking.getId()));
            dto.setEventName(booking.getEvent().getName());
            dto.setEventPrice(booking.getFinalPrice());
            dto.setType(String.valueOf(booking.getBookingType()));
            dto.setParticipants(booking.getParticipants());
            return dto;
        }).collect(Collectors.toList());
        return bookingDTOs;
    }

    public void deleteById(Long bookingId) {
        bookingsRepository.deleteById(bookingId);
    }

    public List<BookingDTO> getAllBookingDTOs() {
        List<Bookings> bookings = bookingsRepository.findAll();
        List<BookingDTO> dtos = new ArrayList<>();
        for (Bookings booking : bookings) {
            Users user = booking.getUser();
            Events event = booking.getEvent();

            String userName = user != null ? user.getUsername() : "";
            String userEmail = user != null ? user.getEmail() : "";
            Long userId = user != null ? user.getId() : null;

            String eventName = event != null ? event.getName() : "";
            BigDecimal eventPrice = event != null ? event.getBasePrice() : null;
            Long eventId = event != null ? event.getID() : null;

            BookingDTO dto = new BookingDTO(
                    (long) booking.getId(),
                    eventName,
                    eventPrice,
                    booking.getBookingType().toString(),
                    booking.getParticipants(),
                    Math.toIntExact(userId),
                    userName,
                    userEmail
            );
            dtos.add(dto);
        }
        return dtos;
    }
}
