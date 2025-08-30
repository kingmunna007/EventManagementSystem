package com.eventmanagement.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.eventmanagement.backend.entity.Users;
import com.eventmanagement.backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private Cloudinary cloudinary;

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
    public Users updateUserWithImage(Long id, String username, String email, String phone, MultipartFile profileImage) {
        Users existingUser = findUserById(id);
        if (existingUser == null) return null;

        if (username != null) existingUser.setUsername(username);
        if (email != null) existingUser.setEmail(email);
        if (phone != null) existingUser.setPhone(phone);

        try {
            if (profileImage != null && !profileImage.isEmpty()) {
                String imageUrl = uploadToCloudinary(profileImage);
                existingUser.setProfilePictureUrl(imageUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error uploading image", e);
        }

        return usersRepository.save(existingUser);
    }

    public String uploadToCloudinary(MultipartFile file) throws IOException {

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", "image"));
        return uploadResult.get("secure_url").toString();
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
