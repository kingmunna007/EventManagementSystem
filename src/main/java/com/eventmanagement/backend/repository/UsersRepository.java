package com.eventmanagement.backend.repository;

import com.eventmanagement.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    boolean existsByEmail(String email);
    Optional<Users> findByEmail(String email);

   // Users findByName(String username);
   @Query("SELECT u FROM Users u WHERE u.username = :value OR u.email = :value")
   Optional<Users> findByUsernameOrEmail(@Param("value") String value); // ✅ WORKS



    Optional<Users> findByUsername(String username);
}
