package com.backend.backend.Repositories;

import com.backend.backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>
{
    Optional<User> findByFirebaseuid(String firebaseUid);
    Optional<User> findByUsername(String username);

}
