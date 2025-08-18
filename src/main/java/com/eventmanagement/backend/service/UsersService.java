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

    @Autowired
    private NotificationService notificationService;

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users findUserById(Long id) {
        return usersRepository.findById(id).orElse(null);
    }

    public Users registerUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users createdUser = usersRepository.save(user);
        // Notify user
        notificationService.notifyUserAndEmail(
            createdUser,
            "USER_REGISTRATION",
            "Welcome to Event Management!",
            "Welcome to Event Management!",
            "Dear " + createdUser.getUsername() + ",\n\nWelcome to Event Management! Your account has been created."
        );
        // Optionally notify admin
        notificationService.notifyAdminAndEmail(
            "USER_REGISTRATION",
            "New user registered: " + createdUser.getUsername(),
            "New User Registered",
            "A new user has registered: " + createdUser.getUsername() + " (" + createdUser.getEmail() + ")"
        );
        return createdUser;
    }

    public Users updateUser(Long id, Users user) {
        Users existingUser = findUserById(id);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            return usersRepository.save(existingUser);
        }
        return null;
    }

    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }

    public void resetPassword(Long id, String newPassword) {
        Users user = findUserById(id);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            usersRepository.save(user);
        }
    }

    public Users findByIdentifier(String identifier) {
        return usersRepository.findByEmail(identifier)
                .or(() -> usersRepository.findByUsername(identifier))
                .orElse(null);
    }
}
