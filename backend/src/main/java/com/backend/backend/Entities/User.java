package com.backend.backend.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder @ToString
@Table(name = "app_user")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false , unique = true)
    private String firebaseuid; //L'ID utilisateur firebase
    @Column(nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;



}
