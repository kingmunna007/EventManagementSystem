package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Notification;
import com.eventmanagement.backend.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllNotifications() {
        Notification n1 = new Notification(); n1.setId(1L); n1.setMessage("Msg1");
        Notification n2 = new Notification(); n2.setId(2L); n2.setMessage("Msg2");
        when(notificationRepository.findAll()).thenReturn(Arrays.asList(n1, n2));
        List<Notification> notifications = notificationService.getAllNotifications();
        assertEquals(2, notifications.size());
    }

    @Test
    void testGetNotificationById() {
        Notification n = new Notification(); n.setId(1L); n.setMessage("Msg1");
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(n));
        Optional<Notification> found = notificationService.getNotificationById(1L);
        assertTrue(found.isPresent());
        assertEquals("Msg1", found.get().getMessage());
    }

    @Test
    void testSaveNotification() {
        Notification n = new Notification(); n.setMessage("Msg1");
        when(notificationRepository.save(n)).thenReturn(n);
        Notification saved = notificationService.saveNotification(n);
        assertEquals("Msg1", saved.getMessage());
    }

    @Test
    void testDeleteNotification() {
        doNothing().when(notificationRepository).deleteById(1L);
        notificationService.deleteNotification(1L);
        verify(notificationRepository, times(1)).deleteById(1L);
    }
}
