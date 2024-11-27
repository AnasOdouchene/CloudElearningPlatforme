package com.backend.backend.Service;

import com.backend.backend.Entities.Role;
import com.backend.backend.Entities.User;
import com.backend.backend.Repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService
{
    private UserRepository userRepository;
    //Méthode pour enregistrer un utilisateur dans Firebase et sauvegarder ses métadonnées localement.
    public User registerUser(String email , String password , Role role)
   {
       try{UserRecord.CreateRequest request = new UserRecord.CreateRequest().setEmail(email)
               .setPassword(password).setEmailVerified(false); // Par défaut, l'e-mail n'est pas vérifié
       UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
       // Sauvegarde des métadonnées dans la base de données locale
       User user = User.builder()
               .firebaseuid(userRecord.getUid()) // UID généré par Firebase
               .email(email)
               .role(role)
               .build();
       return userRepository.save(user);}catch (Exception e){
           throw new RuntimeException("Erreur lors de l'enregistrement de l'utilisateur : " + e.getMessage());
       }
   }
    // Méthode pour récupérer un utilisateur localement à partir de son UID Firebase
    public Optional<User> getUserByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseuid(firebaseUid);
    }
    //Méthode pour sauvegarder ou mettre à jour un utilisateur localement
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    //Méthode pour supprimer un utilisateur dans Firebase et localement
    public void deleteUser(String firebaseUid) {
        try {
            // Suppression dans Firebase
            FirebaseAuth.getInstance().deleteUser(firebaseUid);

            // Suppression dans la base de données locale
            userRepository.findByFirebaseuid(firebaseUid).
                    ifPresent(user->userRepository.delete(user));

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

}
