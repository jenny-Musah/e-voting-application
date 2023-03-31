package com.example.voting_app.service.electionService;

import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.models.Election;
import jakarta.mail.MessagingException;

public interface ElectionService {

    Election createElection(DeclareElectionRequest declareElectionRequest) throws MessagingException;

}
