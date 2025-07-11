package com.eventmanagement.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
    private Long id;
    private String username;
    private String role;
    private String email;
    // getters and setters
}
