package com.eventmanagement.backend.service;

import com.eventmanagement.backend.DTO.BookingDTO;
import com.eventmanagement.backend.DTO.PriceCalculationResponse;
import com.eventmanagement.backend.entity.*;
import com.eventmanagement.backend.repository.BookingsRepository;
import com.eventmanagement.backend.repository.DiscountsRepository;
import com.eventmanagement.backend.repository.EventsRepository;
import com.eventmanagement.backend.repository.UsersRepository;
import com.eventmanagement.backend.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private VenueRepository venueRepository;

    @Autowired
    private DiscountsService discountsService;

    @Autowired
    private NotificationService notificationService;

    // --- CREATE BOOKING ---
    public Bookings createBooking(Integer userId, Integer eventId, Integer participants, String bookingType,
                                  String discountCode, Integer venueId, LocalDateTime eventStartDateTime,
                                  LocalDateTime eventEndDateTime) {

        Users user = usersRepository.findById(userId.longValue())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Events event = eventsRepository.findById(eventId.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        // calculate price
        PriceCalculationResponse priceResp = calculatePrice(eventId, participants, venueId, bookingType, discountCode,
                eventStartDateTime, eventEndDateTime);

        Discounts discount = null;
        if (discountCode != null && !discountCode.isBlank()) {
            discount = discountsRepository.findByCode(discountCode).orElse(null);
        }

        Venues venue = null;
        if (venueId != null) {
            venue = venueRepository.findById(venueId).orElse(null);
        }

        Bookings booking = new Bookings();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setVenue(venue);
        booking.setParticipants(participants);
        booking.setBookingType(BookingType.valueOf(bookingType));
        booking.setDiscount(discount);
        booking.setFinalPrice(priceResp.getFinalPrice());

        if (eventStartDateTime != null) booking.setEventStartDateTime(eventStartDateTime);
        if (eventEndDateTime != null) booking.setEventEndDateTime(eventEndDateTime);

        Bookings saved = bookingsRepository.save(booking);

        // Notify user
        notificationService.notifyUserAndEmail(
                user,
                "BOOKING_CREATED",
                "Your booking is confirmed!",
                "Booking Confirmed",
                "Dear " + user.getUsername() + ",\n\nYour booking for event '" + event.getName()
                        + "' is confirmed. Participants: " + participants + "."
        );

        // Notify admin
        notificationService.notifyAdminAndEmail(
                "BOOKING_CREATED",
                "New booking by: " + user.getUsername(),
                "New Booking Created",
                "A new booking has been created by user: " + user.getUsername() + " for event: " + event.getName()
        );

        return saved;
    }

    // Overload with explicit price (e.g. frontend passed)
    public Bookings createBooking(Integer userId, Integer eventId, Integer participants, String bookingType,
                                  String discountCode, Integer venueId, LocalDateTime eventStartDateTime,
                                  LocalDateTime eventEndDateTime, BigDecimal explicitFinalPrice) {

        if (explicitFinalPrice == null) {
            return createBooking(userId, eventId, participants, bookingType, discountCode, venueId,
                    eventStartDateTime, eventEndDateTime);
        }

        Users user = usersRepository.findById(userId.longValue())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Events event = eventsRepository.findById(eventId.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Discounts discount = null;
        if (discountCode != null && !discountCode.isBlank()) {
            discount = discountsRepository.findByCode(discountCode).orElse(null);
        }

        Venues venue = null;
        if (venueId != null) {
            venue = venueRepository.findById(venueId).orElse(null);
        }

        Bookings booking = new Bookings();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setVenue(venue);
        booking.setParticipants(participants);
        booking.setBookingType(BookingType.valueOf(bookingType));
        booking.setDiscount(discount);
        booking.setFinalPrice(explicitFinalPrice.setScale(2, RoundingMode.HALF_UP));

        if (eventStartDateTime != null) booking.setEventStartDateTime(eventStartDateTime);
        if (eventEndDateTime != null) booking.setEventEndDateTime(eventEndDateTime);

        Bookings saved = bookingsRepository.save(booking);

        // Notify user
        notificationService.notifyUserAndEmail(
                user,
                "BOOKING_CREATED",
                "Your booking is confirmed!",
                "Booking Confirmed",
                "Dear " + user.getUsername() + ",\n\nYour booking for event '" + event.getName()
                        + "' is confirmed. Participants: " + participants + "."
        );

        // Notify admin
        notificationService.notifyAdminAndEmail(
                "BOOKING_CREATED",
                "New booking by: " + user.getUsername(),
                "New Booking Created",
                "A new booking has been created by user: " + user.getUsername() + " for event: " + event.getName()
        );

        return saved;
    }

    // Backward-compatible overload
//    public Bookings createBooking(Integer userId, Integer eventId, Integer participants, String bookingType,
//                                  String discountCode) {
//        return createBooking(userId, eventId, participants, bookingType, discountCode, null, null, null);
//    }

    // --- UPDATE BOOKING ---
    public Bookings updateBooking(Integer bookingId, Integer participants, String bookingType, String discountCode) {
        return bookingsRepository.findById(bookingId).map(booking -> {
            if (participants != null) booking.setParticipants(participants);
            if (bookingType != null) booking.setBookingType(BookingType.valueOf(bookingType));

            Integer venueId = booking.getVenue() != null ? booking.getVenue().getId().intValue() : null;

            PriceCalculationResponse priceResp = calculatePrice(
                    booking.getEvent().getID().intValue(),
                    booking.getParticipants(),
                    venueId,
                    booking.getBookingType().toString(),
                    discountCode,
                    booking.getEventStartDateTime(),
                    booking.getEventEndDateTime()
            );

            Discounts discount = null;
            if (discountCode != null && !discountCode.isBlank()) {
                discount = discountsRepository.findByCode(discountCode).orElse(null);
            }
            booking.setDiscount(discount);
            booking.setFinalPrice(priceResp.getFinalPrice());

            Bookings updated = bookingsRepository.save(booking);

            // Notify user
            notificationService.notifyUserAndEmail(
                    booking.getUser(),
                    "BOOKING_UPDATED",
                    "Your booking has been updated.",
                    "Booking Updated",
                    "Dear " + booking.getUser().getUsername()
                            + ",\n\nYour booking for event '" + booking.getEvent().getName() + "' has been updated."
            );

            return updated;
        }).orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));
    }

    // --- GET BOOKINGS BY USER ---
    public List<BookingDTO> getBookingsByUserId(Integer userId) {
        Long uid = userId.longValue();
        List<Bookings> bookings = bookingsRepository.findByUserIdWithDetails(uid);
        return bookings.stream().map(booking -> {
            BookingDTO dto = new BookingDTO();
            dto.setId(booking.getId().longValue());
            dto.setEventName(booking.getEvent().getName());
            dto.setEventPrice(booking.getFinalPrice());
            dto.setType(String.valueOf(booking.getBookingType()));
            dto.setParticipants(booking.getParticipants());
            dto.setUserId((long) uid.intValue());
            dto.setUserName(booking.getUser().getUsername());
            dto.setUserEmail(booking.getUser().getEmail());
            dto.setVenueId((long) (booking.getVenue() != null ? booking.getVenue().getId().intValue() : 0));
            dto.setEventId((long) booking.getEvent().getID().intValue());
            // populate start/end datetimes and discount code so frontend can show details
            dto.setEventStartDateTime(booking.getEventStartDateTime());
            dto.setEventEndDateTime(booking.getEventEndDateTime());
            dto.setDiscountCode(booking.getDiscount() != null ? booking.getDiscount().getCode() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    // --- DELETE BOOKING ---
    public void deleteById(Integer bookingId) {
        bookingsRepository.findById(bookingId).ifPresent(booking -> {
            notificationService.notifyUserAndEmail(
                    booking.getUser(),
                    "BOOKING_CANCELLED",
                    "Your booking has been cancelled.",
                    "Booking Cancelled",
                    "Dear " + booking.getUser().getUsername()
                            + ",\n\nYour booking for event '" + booking.getEvent().getName() + "' has been cancelled."
            );
            notificationService.notifyAdminAndEmail(
                    "BOOKING_CANCELLED",
                    "Booking cancelled by: " + booking.getUser().getUsername(),
                    "Booking Cancelled",
                    "Booking for event: " + booking.getEvent().getName() + " cancelled by user: " + booking.getUser().getUsername()
            );
        });
        bookingsRepository.deleteById(bookingId);
    }

    // --- GET ALL BOOKINGS ---
    public List<BookingDTO> getAllBookingDTOs() {
        List<Bookings> bookings = bookingsRepository.findAll();
        List<BookingDTO> dtos = new ArrayList<>();
        for (Bookings booking : bookings) {
            Users user = booking.getUser();
            Events event = booking.getEvent();

            String userName = user != null ? user.getUsername() : "";
            String userEmail = user != null ? user.getEmail() : "";
            Long userId = Long.valueOf(user != null ? user.getId().intValue() : null);

            String eventName = event != null ? event.getName() : "";
            BigDecimal eventPrice = event != null ? event.getBasePrice() : null;
            Integer eventId = event != null ? event.getID().intValue() : null;

            BookingDTO dto = new BookingDTO(
                    booking.getId().longValue(),
                    eventName,
                    eventPrice,
                    booking.getBookingType().toString(),
                    booking.getParticipants(),
                    userId,
                    userName,
                    userEmail
            );
            dto.setVenueId((long) (booking.getVenue() != null ? booking.getVenue().getId().intValue() : 0));
            dto.setEventId((long) (eventId != null ? eventId : 0));

            dtos.add(dto);
        }
        return dtos;
    }

    // --- CALCULATE PRICE ---
    public PriceCalculationResponse calculatePrice(Integer eventId, Integer participants, Integer venueId,
                                                   String bookingType, String discountCode,
                                                   LocalDateTime bookingStart, LocalDateTime bookingEnd) {

        PriceCalculationResponse resp = new PriceCalculationResponse();

        Events event = eventsRepository.findById(eventId.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        BigDecimal eventBase = event.getBasePrice() != null ? event.getBasePrice() : BigDecimal.ZERO;
        BigDecimal eventCost = eventBase.multiply(BigDecimal.valueOf(participants));

        BigDecimal venueCost = BigDecimal.ZERO;
        if (venueId != null && venueRepository != null) {
            Venues venue = venueRepository.findById(venueId).orElse(null);
            if (venue != null && venue.getBasePrice() != null && bookingStart != null && bookingEnd != null) {
                long hours = Duration.between(bookingStart, bookingEnd).toHours();
                BigDecimal hoursBd = BigDecimal.valueOf(hours)
                        .divide(BigDecimal.valueOf(24), 10, RoundingMode.HALF_UP); // safe divide
                venueCost = venue.getBasePrice().multiply(hoursBd);
            }
        }

        BigDecimal subtotal = eventCost.add(venueCost);

        double multiplier = 1.0;
        if ("VIP".equalsIgnoreCase(bookingType)) multiplier = 1.3;
        BigDecimal totalBeforeDiscount = subtotal.multiply(BigDecimal.valueOf(multiplier));

        Integer discountPerc = 0;
        if (discountCode != null && !discountCode.isBlank()) {
            Discounts d = discountsRepository.findByCode(discountCode).orElse(null);
            if (d != null && d.getPercentage() != null) discountPerc = d.getPercentage();
        }

        BigDecimal discountAmount = totalBeforeDiscount.multiply(
                BigDecimal.valueOf(discountPerc)
                        .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
        );

        BigDecimal finalPrice = totalBeforeDiscount.subtract(discountAmount)
                .setScale(2, RoundingMode.HALF_UP); // round currency

        resp.setEventCost(eventCost);
        resp.setVenueCost(venueCost);
        resp.setSubtotal(subtotal);
        resp.setTotalBeforeDiscount(totalBeforeDiscount);
        resp.setDiscountPercentage(discountPerc);
        resp.setFinalPrice(finalPrice);

        return resp;
    }
}
