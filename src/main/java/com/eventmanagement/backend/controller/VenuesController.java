package com.eventmanagement.backend.controller;

import com.eventmanagement.backend.entity.Venues;
import com.eventmanagement.backend.service.VenuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
public class VenuesController {
    @Autowired
    private VenuesService venuesService;

    @GetMapping
    public List<Venues> getAllVenues() {
        return venuesService.getAllVenues();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venues> getVenueById(@PathVariable Integer id) {
        return venuesService.getVenueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Venues createVenue(@RequestBody Venues venue) {
        return venuesService.saveVenue(venue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venues> updateVenue(@PathVariable Integer id, @RequestBody Venues venue) {
        try {
            return ResponseEntity.ok(venuesService.updateVenue(id, venue));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Integer id) {
        venuesService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
