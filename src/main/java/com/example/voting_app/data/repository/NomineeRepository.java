package com.example.voting_app.data.repository;

import com.example.voting_app.data.models.Nominee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NomineeRepository extends JpaRepository<Nominee, Long> {
     Nominee findNomineeByEmail(String email);

}
