package com.example.voting_app.data.repository;

import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.models.NomineePortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NomineePortfolioRepository extends JpaRepository<NomineePortfolio, Long> {

    Optional<NomineePortfolio> findNomineeByNomineeId(long nomineeId);

}
