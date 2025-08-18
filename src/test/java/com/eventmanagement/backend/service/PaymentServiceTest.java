package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Payment;
import com.eventmanagement.backend.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPayments() {
        Payment p1 = new Payment(); p1.setId(1L); p1.setAmount(new BigDecimal("100.00")); p1.setPaymentStatus("PAID");
        Payment p2 = new Payment(); p2.setId(2L); p2.setAmount(new BigDecimal("200.00")); p2.setPaymentStatus("PENDING");
        when(paymentRepository.findAll()).thenReturn(Arrays.asList(p1, p2));
        List<Payment> payments = paymentService.getAllPayments();
        assertEquals(2, payments.size());
    }

    @Test
    void testGetPaymentById() {
        Payment p = new Payment(); p.setId(1L); p.setAmount(new BigDecimal("100.00"));
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(p));
        Optional<Payment> found = paymentService.getPaymentById(1L);
        assertTrue(found.isPresent());
        assertEquals(new BigDecimal("100.00"), found.get().getAmount());
    }

    @Test
    void testSavePayment() {
        Payment p = new Payment(); p.setAmount(new BigDecimal("100.00"));
        when(paymentRepository.save(p)).thenReturn(p);
        Payment saved = paymentService.savePayment(p);
        assertEquals(new BigDecimal("100.00"), saved.getAmount());
    }

    @Test
    void testDeletePayment() {
        doNothing().when(paymentRepository).deleteById(1L);
        paymentService.deletePayment(1L);
        verify(paymentRepository, times(1)).deleteById(1L);
    }
}
