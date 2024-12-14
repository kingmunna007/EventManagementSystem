package com.eventmanagement.backend.repository;

import com.eventmanagement.backend.entity.Discounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountsRepository extends JpaRepository<Discounts, Long> {
}