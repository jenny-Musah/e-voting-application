package com.example.voting_app.data.repository;

import com.example.voting_app.data.models.Election;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectionRepository extends JpaRepository<Election, Long> {

}
