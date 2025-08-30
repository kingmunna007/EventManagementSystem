package com.eventmanagement.backend.controller;

import com.eventmanagement.backend.entity.Events;
import com.eventmanagement.backend.service.CloudinaryService;
import com.eventmanagement.backend.service.EventsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5173"})
public class EventsController {
    private static final Logger log = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    private EventsService eventsService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<List<Events>> getAllEvents() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Fetching events, user: {}, roles: {}", auth.getName(), auth.getAuthorities());
        List<Events> events = eventsService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public Events getEvent(@PathVariable Long id){
        return eventsService.getEvent(id).orElse(null);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createEvent(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "basePrice", required = false) String basePrice,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Creating event, user: {}, roles: {}, params: [name={}, basePrice={}, type={}, description={}, startDate={}, endDate={}, location={}, status={}, image={}]",
                auth.getName(), auth.getAuthorities(), name, basePrice, type, description, startDate, endDate, location, status, image != null ? image.getOriginalFilename() : null);
        try {
            // Validate required fields
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Event name is required");
            }
            if (basePrice == null || basePrice.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Base price is required");
            }
            BigDecimal parsedBasePrice;
            try {
                parsedBasePrice = new BigDecimal(basePrice);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid base price format");
            }
            if (startDate == null || startDate.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Start date is required");
            }
            if (endDate == null || endDate.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("End date is required");
            }
            if (location == null || location.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Location is required");
            }
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Status is required");
            }

            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = cloudinaryService.uploadFile(image, "events");
            }
            Events event = new Events();
            event.setName(name);
            event.setBasePrice(parsedBasePrice);
            event.setType(type);
            event.setDescription(description);
            event.setImageUrl(imageUrl);
            try {
                event.setStartDate(LocalDateTime.parse(startDate));
                event.setEndDate(LocalDateTime.parse(endDate));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid date format");
            }
            event.setLocation(location);
            event.setStatus(status);
            Events savedEvent = eventsService.createEvent(event);
            return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create event: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateEvent(
            @PathVariable String id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "basePrice", required = false) String basePrice,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Updating event, user: {}, roles: {}, id: {}, params: [name={}, basePrice={}, type={}, description={}, startDate={}, endDate={}, location={}, status={}, image={}]",
                auth.getName(), auth.getAuthorities(), id, name, basePrice, type, description, startDate, endDate, location, status, image != null ? image.getOriginalFilename() : null);
        try {
            Long eventId = Long.parseLong(id);
            if (eventId <= 0) {
                log.error("Invalid event ID: {}", id);
                return ResponseEntity.badRequest().body("Invalid event ID");
            }
            // Validate required fields
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Event name is required");
            }
            if (basePrice == null || basePrice.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Base price is required");
            }
            BigDecimal parsedBasePrice;
            try {
                parsedBasePrice = new BigDecimal(basePrice);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid base price format");
            }
            if (startDate == null || startDate.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Start date is required");
            }
            if (endDate == null || endDate.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("End date is required");
            }
            if (location == null || location.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Location is required");
            }
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Status is required");
            }

            Events existingEvent = eventsService.getEventById(eventId);
            if (existingEvent == null) {
                return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
            }
            String imageUrl = existingEvent.getImageUrl();
            if (image != null && !image.isEmpty()) {
                try {
                    if (imageUrl != null) {
                        cloudinaryService.deleteFile(imageUrl);
                    }
                    imageUrl = cloudinaryService.uploadFile(image, "events");
                } catch (Exception e) {
                    log.error("Error uploading image: {}", e.getMessage());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Failed to upload image: " + e.getMessage());
                }
            }
            existingEvent.setName(name);
            existingEvent.setBasePrice(parsedBasePrice);
            existingEvent.setType(type);
            existingEvent.setDescription(description);
            existingEvent.setImageUrl(imageUrl);
            try {
                existingEvent.setStartDate(LocalDateTime.parse(startDate));
                existingEvent.setEndDate(LocalDateTime.parse(endDate));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid date format");
            }
            existingEvent.setLocation(location);
            existingEvent.setStatus(status);
            Events updatedEvent = eventsService.updateEvent(eventId, existingEvent);
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        } catch (NumberFormatException e) {
            log.error("Invalid ID format: {}", id);
            return ResponseEntity.badRequest().body("Invalid event ID format");
        } catch (IllegalArgumentException e) {
            log.error("Event not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        } catch (Exception e) {
            log.error("Error updating event: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update event: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteEvent(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Deleting event, user: {}, roles: {}, id: {}",
                auth.getName(), auth.getAuthorities(), id);
        try {
            Long eventId = Long.parseLong(id);
            if (eventId <= 0) {
                log.error("Invalid event ID: {}", id);
                return ResponseEntity.badRequest().body("Invalid event ID");
            }
            eventsService.deleteEvent(eventId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            log.error("Invalid ID format: {}", id);
            return ResponseEntity.badRequest().body("Invalid event ID format");
        } catch (IllegalArgumentException e) {
            log.error("Event not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }
    }
}