package Service;

import Entities.Role;
import Entities.User;
import Repositories.UserRepository;
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
    //Fonction pour enregistrer un user

    public User register(String email , String password , Role role)
    {
        //creation l'user dans firebase
        UserRecord.CreateRequest request = new UserRecord.CreateRequest().setEmail(email)
                                         .setPassword(password).setEmailVerified(false);
        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);


    }

}
