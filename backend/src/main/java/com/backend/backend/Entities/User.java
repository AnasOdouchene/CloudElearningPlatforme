package com.backend.backend.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder @ToString
@Table(name = "app_user")
public class User
{
    @Id
    @Column(nullable = false , unique = true)
    private String firebaseuid; //L'ID utilisateur firebase

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String firstName; // Prénom

    @Column(nullable = false)
    private String lastName; // Nom

    @Column(nullable = false, unique = true)
    private String username; // Nom d'utilisateur

    @Enumerated(EnumType.STRING)
    private Role role;



}
