package com.example.voting_app.service.votersService;

import com.example.voting_app.data.models.VoterCard;

public interface VotersService {

    VoterCard creatVotersCard(long userId);

    VoterCard findVoterCard(long userId);
}
