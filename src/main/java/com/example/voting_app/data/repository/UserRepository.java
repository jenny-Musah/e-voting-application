package com.example.voting_app.data.repository;

import com.example.voting_app.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findUserByEmailAddress(String email);

}
