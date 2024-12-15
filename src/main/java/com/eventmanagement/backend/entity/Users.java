package com.eventmanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name")
    private String username;

    @Column(name = "Email")
    private String email;


    @Column(name = "Password")
    private String password;

    @Column(name = "role")
    private String role = Role.USER;

   public static class Role{
       public static final String USER = "USER";
       public static final String ADMIN = "ADMIN";

   }

}
