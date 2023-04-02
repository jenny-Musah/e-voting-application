package com.example.voting_app.data.repository;

import com.example.voting_app.data.models.VoterCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotersCardRepository extends JpaRepository<VoterCard, Long> {
Optional<VoterCard> findVoterCardByUserId( long userId);

}
