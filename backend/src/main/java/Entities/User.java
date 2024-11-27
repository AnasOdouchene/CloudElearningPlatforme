package Entities;

import jakarta.persistence.*;

@Entity

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
