package com.example.voting_app.controller;

import com.example.voting_app.data.dto.requests.AddNomineeRequest;
import com.example.voting_app.data.dto.requests.AddVoteRequest;
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


    @PostMapping("/vote/{userId}")
    public ResponseEntity<?> vote(@RequestBody AddVoteRequest addVoteRequest, @PathVariable long userId){
        return new ResponseEntity<>(electionService.addVote(addVoteRequest,userId), HttpStatus.OK);
    }

    @PostMapping("/add/nominee")
    public ResponseEntity<?> addNominee(@RequestBody AddNomineeRequest addNomineeRequest) throws MessagingException {
        return new ResponseEntity<>(electionService.addNominee(addNomineeRequest), HttpStatus.OK);
    }

}
