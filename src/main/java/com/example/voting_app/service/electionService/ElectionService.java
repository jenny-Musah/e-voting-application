package com.example.voting_app.service.electionService;

import com.example.voting_app.data.dto.requests.AddNomineeRequest;
import com.example.voting_app.data.dto.requests.AddVoteRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.response.ApiResponse;
import com.example.voting_app.data.models.Election;
import jakarta.mail.MessagingException;

import java.util.List;

public interface ElectionService {

    ApiResponse createElection(DeclareElectionRequest declareElectionRequest);

    ApiResponse addVote(AddVoteRequest addVoteRequest, long userId);


    ApiResponse addNominee(AddNomineeRequest addNomineeRequest) throws MessagingException;

    List<Election> findAllElections();
}

