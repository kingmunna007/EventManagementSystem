package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Venues;
import com.eventmanagement.backend.repository.VenueRepository;
import com.eventmanagement.backend.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VenuesService {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Venues> getAllVenues() {
        return venueRepository.findAll();
    }

    public Optional<Venues> getVenueById(Long id) {
        return venueRepository.findById(id);
    }

    public Venues saveVenue(Venues venue) {
        return venueRepository.save(venue);
    }

    public void deleteVenue(Long id) {
        venueRepository.deleteById(id);
    }

    public Venues updateVenue(Long id, Venues updatedVenue, MultipartFile image, MultipartFile video) {
        return venueRepository.findById(id).map(existingVenue -> {
            existingVenue.setName(updatedVenue.getName());
            existingVenue.setAddress(updatedVenue.getAddress());
            existingVenue.setCapacity(updatedVenue.getCapacity());
            existingVenue.setBasePrice(updatedVenue.getBasePrice());

            try {
                if (image != null && !image.isEmpty()) {
                    String newImageUrl = cloudinaryService.uploadFile(image, "venues/images");
                    existingVenue.setImageUrl(newImageUrl);
                }
                if (video != null && !video.isEmpty()) {
                    String newVideoUrl = cloudinaryService.uploadFile(video, "venues/videos");
                    existingVenue.setDemoVideoUrl(newVideoUrl);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload files", e);
            }

            return venueRepository.save(existingVenue);
        }).orElseThrow(() -> new IllegalArgumentException("Venue not found with ID: " + id));
    }

    /**
     * Create a venue and upload media to Cloudinary
     */
    public Venues createVenueWithMedia(
            String name,
            String address,
            String basePrice,
            Integer capacity,
            MultipartFile image,
            MultipartFile video
    ) throws IOException {
        Venues venue = new Venues();
        venue.setName(name);
        venue.setAddress(address);
        venue.setBasePrice(new BigDecimal(basePrice));
        venue.setCapacity(capacity);

        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(image, "image");
            venue.setImageUrl(imageUrl);
        }

        if (video != null && !video.isEmpty()) {
            String videoUrl = cloudinaryService.uploadFile(video, "video");
            venue.setDemoVideoUrl(videoUrl);
        }

        return venueRepository.save(venue);
    }
}
