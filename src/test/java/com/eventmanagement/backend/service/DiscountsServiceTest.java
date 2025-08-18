package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Discounts;
import com.eventmanagement.backend.repository.DiscountsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiscountsServiceTest {
    @Mock
    private DiscountsRepository discountsRepository;

    @InjectMocks
    private DiscountsService discountsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDiscountByCode() {
        Discounts discount = new Discounts("CODE10", 10, LocalDateTime.now(), LocalDateTime.now().plusDays(1), true);
        when(discountsRepository.findByCode("CODE10")).thenReturn(Optional.of(discount));
        Discounts found = discountsService.getDiscountByCode("CODE10");
        assertNotNull(found);
        assertEquals(10, found.getPercentage());
    }

    @Test
    void testApplyDiscount() {
        Discounts discount = new Discounts("CODE10", 10, LocalDateTime.now(), LocalDateTime.now().plusDays(1), true);
        when(discountsRepository.findByCode("CODE10")).thenReturn(Optional.of(discount));
        BigDecimal discounted = discountsService.applyDiscount(100.0, "CODE10");
        assertEquals(new BigDecimal("90.0"), discounted);
    }

    @Test
    void testIsDiscountValid() {
        when(discountsRepository.existsByCode("CODE10")).thenReturn(true);
        assertTrue(discountsService.isDiscountValid("CODE10"));
    }

    @Test
    void testSave() {
        Discounts discount = new Discounts("CODE10", 10, LocalDateTime.now(), LocalDateTime.now().plusDays(1), true);
        when(discountsRepository.save(discount)).thenReturn(discount);
        Discounts saved = discountsService.save(discount);
        assertEquals("CODE10", saved.getCode());
    }
}
