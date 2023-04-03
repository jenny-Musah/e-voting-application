package com.example.voting_app.data.repository;

import com.example.voting_app.data.models.NomineePortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NomineePortfolioRepository extends JpaRepository<NomineePortfolio, Long> {

}
