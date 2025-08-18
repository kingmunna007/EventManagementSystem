package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Notification;
import com.eventmanagement.backend.entity.Users;
import com.eventmanagement.backend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MailService mailService;

    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public Notification updateNotification(Long id, Notification updatedNotification) {
        return notificationRepository.findById(id).map(notification -> {
            notification.setMessage(updatedNotification.getMessage());
            notification.setType(updatedNotification.getType());
            // Add more fields as needed
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new IllegalArgumentException("Notification not found with ID: " + id));
    }

    public void notifyUserAndEmail(Users user, String type, String message, String emailSubject, String emailBody) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setMessage(message);
        notification.setIsRead(false);
        notificationRepository.save(notification);
        if (user != null && user.getEmail() != null) {
            mailService.sendMail(user.getEmail(), emailSubject, emailBody);
        }
    }

    public void notifyAdminAndEmail(String type, String message, String emailSubject, String emailBody) {
        Notification notification = new Notification();
        notification.setUser(null); // or set to admin user if available
        notification.setType(type);
        notification.setMessage(message);
        notification.setIsRead(false);
        notificationRepository.save(notification);
        mailService.sendMail(adminEmail, emailSubject, emailBody);
    }
}
