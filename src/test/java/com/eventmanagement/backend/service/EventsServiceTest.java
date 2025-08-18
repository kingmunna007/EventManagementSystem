package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Events;
import com.eventmanagement.backend.repository.EventsRepository;
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

class EventsServiceTest {
    @Mock
    private EventsRepository eventsRepository;

    @InjectMocks
    private EventsService eventsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvents() {
        Events event1 = new Events(1L, "Event1", new BigDecimal("100.00"), "Type1", "Desc1", "img1", LocalDateTime.now(), LocalDateTime.now(), "Loc1", "Active");
        Events event2 = new Events(2L, "Event2", new BigDecimal("200.00"), "Type2", "Desc2", "img2", LocalDateTime.now(), LocalDateTime.now(), "Loc2", "Inactive");
        when(eventsRepository.findAll()).thenReturn(Arrays.asList(event1, event2));
        List<Events> events = eventsService.getAllEvents();
        assertEquals(2, events.size());
        verify(eventsRepository, times(1)).findAll();
    }

    @Test
    void testGetEventById() {
        Events event = new Events(1L, "Event1", new BigDecimal("100.00"), "Type1", "Desc1", "img1", LocalDateTime.now(), LocalDateTime.now(), "Loc1", "Active");
        when(eventsRepository.findById(1L)).thenReturn(Optional.of(event));
        Events found = eventsService.getEventById(1L);
        assertEquals("Event1", found.getName());
        verify(eventsRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateEvent() {
        Events event = new Events(null, "Event1", new BigDecimal("100.00"), "Type1", "Desc1", "img1", LocalDateTime.now(), LocalDateTime.now(), "Loc1", "Active");
        Events savedEvent = new Events(1L, "Event1", new BigDecimal("100.00"), "Type1", "Desc1", "img1", LocalDateTime.now(), LocalDateTime.now(), "Loc1", "Active");
        when(eventsRepository.save(event)).thenReturn(savedEvent);
        Events result = eventsService.createEvent(event);
        assertNotNull(result.getID());
        verify(eventsRepository, times(1)).save(event);
    }

    @Test
    void testUpdateEvent() {
        Events existing = new Events(1L, "Old", new BigDecimal("100.00"), "Type1", "Desc1", "img1", LocalDateTime.now(), LocalDateTime.now(), "Loc1", "Active");
        Events updated = new Events(1L, "New", new BigDecimal("150.00"), "Type2", "Desc2", "img2", LocalDateTime.now(), LocalDateTime.now(), "Loc2", "Inactive");
        when(eventsRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(eventsRepository.save(any(Events.class))).thenReturn(updated);
        Events result = eventsService.updateEvent(1L, updated);
        assertEquals("New", result.getName());
        verify(eventsRepository, times(1)).findById(1L);
        verify(eventsRepository, times(1)).save(existing);
    }

    @Test
    void testDeleteEvent() {
        doNothing().when(eventsRepository).deleteById(1L);
        eventsService.deleteEvent(1L);
        verify(eventsRepository, times(1)).deleteById(1L);
    }
}
