package com.example.voting_app.service.votersService;

import com.example.voting_app.data.models.VoterCard;
import com.example.voting_app.data.repository.VotersCardRepository;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VotersServiceImpl implements VotersService{

    @Autowired
    VotersCardRepository votersCardRepository;

    @Override public VoterCard creatVotersCard(long userId) {
        VoterCard voterCard = new VoterCard();
        voterCard.setVotersId(generateId());
        voterCard.setUserId(userId);
        return votersCardRepository.save(voterCard);
    }

    @Override public VoterCard findVoterCard(long userId) {
        return votersCardRepository.findVoterCardByUserId(userId).orElseThrow(() -> new InvalidDetails("Invalid user id "));
    }

    private long generateId(){
        Random random = new Random();
        return Math.abs(random.nextLong());
    }
}
