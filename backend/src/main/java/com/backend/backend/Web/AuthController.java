package com.backend.backend.Web;

import com.backend.backend.Service.FirebaseAuthService;
import com.google.firebase.auth.FirebaseToken;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    private FirebaseAuthService firebaseAuthService;

    @GetMapping("/secure-endpoint")
    public ResponseEntity<String> secureEndpoint(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extraire le token Bearer
            String idToken = authorizationHeader.replace("Bearer ", "");

            // Valider le jeton Firebase
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(idToken);

            // Renvoyer une réponse sécurisée
            return ResponseEntity.ok("Token is valid. User ID: " + decodedToken.getUid());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized: " + e.getMessage());
        }
    }
}
