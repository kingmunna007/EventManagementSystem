package com.eventmanagement.backend.service;

import com.eventmanagement.backend.entity.Users;
import com.eventmanagement.backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users registerUser(Users user) {
        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Users.Role.USER);
        return usersRepository.save(user);
    }

    public Users authenticateUser(String email, String rawPassword) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials.");
        }
        return user;
    }

    public Users updateUser(Long userId, Users updatedDetails) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setUsername(updatedDetails.getUsername());
        user.setEmail(updatedDetails.getEmail());
        user.setPassword(passwordEncoder.encode(updatedDetails.getPassword()));

        return usersRepository.save(user);
    }


    public Users findUserById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    public Users findUserByEmail(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public void deleteUser(Long userId) {
        usersRepository.deleteById(userId);
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public void resetPassword(Long userId, String newPassword) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
    }



}
