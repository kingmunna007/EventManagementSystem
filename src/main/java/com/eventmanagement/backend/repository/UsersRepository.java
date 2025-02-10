package com.eventmanagement.backend.repository;

import com.eventmanagement.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    boolean existsByEmail(String email);
    Optional<Users> findByEmail(String email);

   // Users findByName(String username);


    Users findByUsername(String username);
}
