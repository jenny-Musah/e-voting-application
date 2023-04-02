package com.example.voting_app.service;

import com.example.voting_app.data.models.VoterCard;
import com.example.voting_app.service.votersService.VotersService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
public class VoteServiceTest {

    @Autowired
    private VotersService votersService;
    @Test
    public void testThatVoterCardCanBeCreated(){
        VoterCard voterCard = votersService.creatVotersCard(234555L);
        assertNotNull(voterCard);
    }
    @Test
    public void testThatVotersCanBeFoundWithVoteId(){
        VoterCard voterCard = votersService.creatVotersCard(234555L);
        System.out.println(voterCard.getUserId());
        VoterCard foundVotersCard = votersService.findVoterCard(voterCard.getUserId());
        assertEquals(voterCard,foundVotersCard);
    }


}
