package com.eventmanagement.backend.repository;

import com.eventmanagement.backend.entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Integer> {
    // find bookings by user id (use Long to match Users.id type)
    List<Bookings> findByUserId(Long userId);

    @Query("SELECT b FROM Bookings b " +
        "JOIN FETCH b.user u " +
        "JOIN FETCH b.event e " +
        "LEFT JOIN FETCH b.discount d " +
        "WHERE u.id = :userId")
    List<Bookings> findByUserIdWithDetails(@Param("userId") Long userId);



}
