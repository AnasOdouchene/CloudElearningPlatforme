package Service;

import Entities.User;
import Repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService
{
    private UserRepository userRepository;

    public User SaveUser(User user)
    {
        return userRepository.save(user);
    }
    public Optional<User> getUserByFirebase(String firebaseUid)
    {
        return userRepository.findByFirebaseuid(firebaseUid);
    }

}
