package com.eventmanagement.backend.controller;

import com.eventmanagement.backend.entity.Venues;
import com.eventmanagement.backend.service.VenuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/venues")
//@CrossOrigin(origins = "*") // Allow frontend access
public class VenuesController {

    @Autowired
    private VenuesService venuesService;

    @GetMapping
    public List<Venues> getAllVenues() {
        return venuesService.getAllVenues();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venues> getVenueById(@PathVariable Long id) {
        return venuesService.getVenueById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new venue with image and video upload
     */
    @PostMapping("/create")
    public ResponseEntity<Venues> createVenue(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("basePrice") String basePrice,
            @RequestParam("capacity") Integer capacity,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "video", required = false) MultipartFile video
    ) throws IOException {
        Venues venue = venuesService.createVenueWithMedia(name, address, basePrice, capacity, image, video);
        return ResponseEntity.ok(venue);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venues> updateVenue(
            @PathVariable Long id,
            @RequestPart("venue") Venues venue,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "video", required = false) MultipartFile video) {

        return ResponseEntity.ok(venuesService.updateVenue(id, venue, image, video));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venuesService.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }
}
