package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Events;
import com.eventmanagement.backend.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsService {
    @Autowired
    private EventsRepository eventsRepository;

    public List<Events> getAllEvents() {
        return eventsRepository.findAll();
    }

    public Events getEventById(Long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
    }
}
