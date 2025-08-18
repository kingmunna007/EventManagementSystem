package com.eventmanagement.backend.service;

import com.eventmanagement.backend.DTO.BookingDTO;
import com.eventmanagement.backend.entity.*;
import com.eventmanagement.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingsServiceTest {
    @Mock
    private BookingsRepository bookingsRepository;
    @Mock
    private EventsRepository eventsRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private DiscountsRepository discountsRepository;
    @Mock
    private DiscountsService discountsService;

    @InjectMocks
    private BookingsService bookingsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBooking() {
        Users user = new Users(); user.setId(1L); user.setUsername("test");
        Events event = new Events(); event.setID(1L); event.setBasePrice(new BigDecimal("100.00"));
        Discounts discount = new Discounts(); discount.setCode("CODE10"); discount.setPercentage(10);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventsRepository.findById(1L)).thenReturn(Optional.of(event));
        when(discountsRepository.findByCode("CODE10")).thenReturn(Optional.of(discount));
        when(discountsService.applyDiscount(200.0, "CODE10")).thenReturn(new BigDecimal("180.0"));
        when(bookingsRepository.save(any(Bookings.class))).thenAnswer(i -> i.getArgument(0));
        Bookings booking = bookingsService.createBooking(1, 1, 2, "STANDARD", "CODE10");
        assertEquals(user, booking.getUser());
        assertEquals(event, booking.getEvent());
        assertEquals(new BigDecimal("180.0"), booking.getFinalPrice());
    }

//    @Test
//    void testDeleteById() {
//        doNothing().when(bookingsRepository).deleteById(1L);
//        bookingsService.deleteById(1L);
//        verify(bookingsRepository, times(1)).deleteById(1L);
//    }
}
