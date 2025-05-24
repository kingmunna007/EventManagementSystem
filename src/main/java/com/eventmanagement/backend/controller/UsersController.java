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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping
   // @PreAuthorize("hasRole('ADMIN')")
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

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users user) {
        Users updatedUser = usersService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
//    @GetMapping("/login")
//    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
//        Users user = usersService.authenticateUser(email, password);
//
//        if (user.getRole().equals(Users.Role.ADMIN)) {
//            return new ResponseEntity<>("Redirect to Admin Dashboard", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Redirect to User Dashboard", HttpStatus.OK);
//        }
//    }
//@GetMapping("/login")
//public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
//    Users user = usersService.authenticateUser(email, password);
//    if (user != null) {
//        return ResponseEntity.ok(Map.of(
//                "id", user.getId(),
//                "role", user.getRole(),
//                "username", user.getUsername()
//        ));
//    } else {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//    }
//}
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    Users user = usersService.findByEmail(loginRequest.getEmail());
    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
    }
    // Map to safe DTO
    UserLoginResponse response = new UserLoginResponse();
    response.setId(user.getId());
    response.setUsername(user.getUsername());
    response.setRole(user.getRole().toString());
    response.setEmail(user.getEmail());
    return ResponseEntity.ok(response);
}

    @PutMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        usersService.resetPassword(id, newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
