package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Venues;
import com.eventmanagement.backend.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VenuesService {
    @Autowired
    private VenueRepository venueRepository;

    public List<Venues> getAllVenues() {
        return venueRepository.findAll();
    }

    public Optional<Venues> getVenueById(Integer id) {
        return venueRepository.findById(id);
    }

    public Venues saveVenue(Venues venue) {
        return venueRepository.save(venue);
    }

    public void deleteVenue(Integer id) {
        venueRepository.deleteById(id);
    }

    public Venues updateVenue(Integer id, Venues updatedVenue) {
        return venueRepository.findById(id).map(venue -> {
            venue.setName(updatedVenue.getName());
            venue.setAddress(updatedVenue.getAddress());
            venue.setCapacity(updatedVenue.getCapacity());
            // Add more fields as needed
            return venueRepository.save(venue);
        }).orElseThrow(() -> new IllegalArgumentException("Venue not found with ID: " + id));
    }
}
