package com.backend.backend.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {
    /**
     * Vérifie un jeton Firebase ID Token.
     *
     * @param idToken Le jeton Firebase à valider.
     * @return FirebaseToken Les informations décodées du jeton.
     * @throws RuntimeException Si le jeton est invalide ou expiré.
     */
    public FirebaseToken verifyToken(String idToken) {
        try {
            // Vérifie et décode le token avec FirebaseAuth
            return FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }
}
