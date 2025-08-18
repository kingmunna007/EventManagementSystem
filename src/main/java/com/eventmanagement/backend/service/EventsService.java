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

    public Events createEvent(Events event) {
        return eventsRepository.save(event);
    }

    public Events updateEvent(Long id, Events updatedEvent) {
        return eventsRepository.findById(id).map(event -> {
            event.setName(updatedEvent.getName());
            event.setBasePrice(updatedEvent.getBasePrice());
            event.setType(updatedEvent.getType());
            event.setDescription(updatedEvent.getDescription());
            event.setImageUrl(updatedEvent.getImageUrl());
            event.setStartDate(updatedEvent.getStartDate());
            event.setEndDate(updatedEvent.getEndDate());
            event.setLocation(updatedEvent.getLocation());
            event.setStatus(updatedEvent.getStatus());
            return eventsRepository.save(event);
        }).orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + id));
    }

    public void deleteEvent(Long id) {
        eventsRepository.deleteById(id);
    }
}
