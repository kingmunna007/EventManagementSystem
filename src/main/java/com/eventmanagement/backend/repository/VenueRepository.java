package com.eventmanagement.backend.repository;

import com.eventmanagement.backend.entity.Venues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venues,Integer> {
}
