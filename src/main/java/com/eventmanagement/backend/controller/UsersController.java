package com.eventmanagement.backend.controller;

import com.eventmanagement.backend.DTO.LoginRequest;
import com.eventmanagement.backend.DTO.UserLoginResponse;
import com.eventmanagement.backend.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.eventmanagement.backend.entity.Users;
import com.eventmanagement.backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import com.eventmanagement.backend.security.JwtUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = usersService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> findUserById(@PathVariable Long id) {
        Users user = usersService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<Users> registerUser(@RequestBody Users user) {
        Users createdUser = usersService.registerUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Users> updateUser(
            @PathVariable Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) MultipartFile profileImage) {

        Users updatedUser = usersService.updateUserWithImage(id, username, email, phone, profileImage);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    Users user = usersService.findByIdentifier(loginRequest.getIdentifier());
    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
    }
    // Generate JWT token
    String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
    // Map to safe DTO
    UserLoginResponse response = new UserLoginResponse();
    response.setId(user.getId());
    response.setUsername(user.getUsername());
    response.setRole(user.getRole().toString());
    response.setEmail(user.getEmail());
    response.setToken(token);
    response.setPhone(user.getPhone());
    return ResponseEntity.ok(response);
}

    @PutMapping("/{id}/password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> passwords) {
        String currentPassword = passwords.get("currentPassword");
        String newPassword = passwords.get("newPassword");

        Users user = usersService.findUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
        }

        usersService.resetPassword(id, newPassword);
        return ResponseEntity.ok("Password updated successfully");
    }

}
