package com.example.voting_app.data.repository;

import com.example.voting_app.data.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
  Optional<Admin> findByLoginId(Long LoginId);

}
