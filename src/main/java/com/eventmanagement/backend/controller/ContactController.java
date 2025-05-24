package com.eventmanagement.backend.controller;

import com.eventmanagement.backend.DTO.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/contact")
    public ResponseEntity<?> sendContactEmail(@RequestBody ContactRequest contact) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("kingmunna4296@gmail.com");
            message.setSubject("New Contact Us Message from " + contact.getName());
            message.setText("Name: " + contact.getName() + "\nEmail: " + contact.getEmail() + "\n\nMessage:\n" + contact.getMessage());
            mailSender.send(message);
            return ResponseEntity.ok().body("Message sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }
}