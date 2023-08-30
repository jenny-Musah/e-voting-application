package com.example.voting_app.controller;

import com.example.voting_app.data.dto.requests.AddNomineeRequest;
import com.example.voting_app.data.dto.requests.AddVoteRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.response.ApiResponse;
import com.example.voting_app.service.electionService.ElectionService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/election")
public class ElectionController {

    @Autowired
    private ElectionService electionService;


    @PostMapping("/vote")
    public ResponseEntity<ApiResponse> vote(@RequestBody AddVoteRequest addVoteRequest, @RequestParam("id") long userId){
        return ResponseEntity.ok(electionService.addVote(addVoteRequest,userId));
    }

    @PostMapping("/nominee")
    public ResponseEntity<ApiResponse> addNominee(@RequestBody AddNomineeRequest addNomineeRequest) throws MessagingException {
        return ResponseEntity.ok(electionService.addNominee(addNomineeRequest));
    }
    @PostMapping
    public ResponseEntity<ApiResponse> declareElection(@RequestBody DeclareElectionRequest declareElectionRequest){
        return ResponseEntity.ok(electionService.createElection(declareElectionRequest));
    }

}
