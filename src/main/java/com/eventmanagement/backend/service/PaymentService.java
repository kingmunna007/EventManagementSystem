package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Payment;
import com.eventmanagement.backend.entity.Users;
import com.eventmanagement.backend.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment savePayment(Payment payment) {
        Payment saved = paymentRepository.save(payment);
        Users user = payment.getBooking().getUser();
        if (user != null) {
            notificationService.notifyUserAndEmail(
                user,
                "PAYMENT_SUCCESS",
                "Your payment was successful.",
                "Payment Successful",
                "Dear " + user.getUsername() + ",\n\nYour payment for booking was successful. Amount: " + payment.getAmount()
            );
            notificationService.notifyAdminAndEmail(
                "PAYMENT_SUCCESS",
                "Payment received from: " + user.getUsername(),
                "Payment Received",
                "Payment received for booking by user: " + user.getUsername() + ". Amount: " + payment.getAmount()
            );
        }
        return saved;
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    public Payment updatePayment(Long id, Payment updatedPayment) {
        return paymentRepository.findById(id).map(payment -> {
            payment.setAmount(updatedPayment.getAmount());
            payment.setPaymentStatus(updatedPayment.getPaymentStatus());
            // Add more fields as needed
            return paymentRepository.save(payment);
        }).orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + id));
    }
}
