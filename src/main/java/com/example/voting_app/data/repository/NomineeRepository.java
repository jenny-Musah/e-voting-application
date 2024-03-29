package com.example.voting_app.data.repository;

import com.example.voting_app.data.models.Nominee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {

}
