package com.example.voting_app.data.repository;

import com.example.voting_app.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    User findUserByEmailAddress(String email);
    Optional<User> findUserByLoginId(long loginId);

}
