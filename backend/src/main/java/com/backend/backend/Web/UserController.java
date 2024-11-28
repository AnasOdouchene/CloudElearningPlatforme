package com.backend.backend.Web;

import com.backend.backend.Entities.Role;
import com.backend.backend.Entities.User;
import com.backend.backend.Service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController
{
    private UserService userService;
    // Endpoint pour enregistrer un utilisateur
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Role role) {
        try {
            User user = userService.registerUser(
                    email,       // Correspond à `email` dans UserService
                    password,    // Correspond à `password`
                    firstName,   // Correspond à `firstName`
                    lastName,    // Correspond à `lastName`
                    username,    // Correspond à `username`
                    role);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Endpoint pour récupérer un utilisateur par Firebase UID
    @GetMapping("/{firebaseUid}")
    public ResponseEntity<User> getUserByFirebaseUid(@PathVariable String firebaseUid) {
        return userService.getUserByFirebaseUid(firebaseUid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint pour supprimer un utilisateur
    @DeleteMapping("/{firebaseUid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String firebaseUid) {
        try {
            userService.deleteUser(firebaseUid);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password) {
        try {
            // Étape 1 : Authentifier l'utilisateur avec Firebase
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            UserRecord userRecord = firebaseAuth.getUserByEmail(email);

            if (userRecord == null) {
                return ResponseEntity.status(404).body("Utilisateur non trouvé dans Firebase.");
            }

            // Étape 2 : Vérifier les métadonnées localement
            Optional<User> optionalUser = userService.getUserByFirebaseUid(((UserRecord) userRecord).getUid());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body("Utilisateur non trouvé localement.");
            }

            // Étape 3 : Retourner les métadonnées utilisateur
            User user = optionalUser.get();
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Erreur d'authentification : " + e.getMessage());
        }
    }


}
