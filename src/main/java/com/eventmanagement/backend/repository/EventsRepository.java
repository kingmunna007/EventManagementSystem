package com.eventmanagement.backend.repository;

import com.eventmanagement.backend.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {
}
