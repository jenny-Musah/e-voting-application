package com.example.voting_app.service.electionService;

import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.requests.NomineeDetailsRequest;
import com.example.voting_app.data.models.Election;
import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.repository.ElectionRepository;
import com.example.voting_app.service.nomineeService.NomineeService;
import com.example.voting_app.utils.Validator;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ElectionServiceImpl implements ElectionService{


    @Autowired
    private NomineeService nomineeService;

    @Autowired
    private ElectionRepository electionRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Override
    public Election createElection(DeclareElectionRequest createElectionRequest) throws MessagingException {
        if(!validateDate(createElectionRequest)) throw new InvalidDetails("Invalid date format");
        if(LocalDate.parse(createElectionRequest.getStartAt(),formatter).isBefore(LocalDate.now())
        || LocalDate.parse(createElectionRequest.getEndsAt(),formatter).isBefore(LocalDate.now())) throw new InvalidDetails("date selected is not valid");
        Election election = new Election();
        election.setElectionName(createElectionRequest.getElectionName());
        election.setStartAt(LocalDate.parse(createElectionRequest.getStartAt(),formatter));
        election.setEndAt(LocalDate.parse(createElectionRequest.getEndsAt(),formatter));
        for(NomineeDetailsRequest nomineeDetailsRequest : createElectionRequest.getListOfNominee()){
            Nominee nominee = nomineeService.addNominee(nomineeDetailsRequest);
            election.getListOfNominee().add(nominee);
        }
        return electionRepository.save(election);
    }

    private boolean validateDate(DeclareElectionRequest declareElectionRequest){
        if(!Validator.isDateValid(declareElectionRequest.getEndsAt()) ||
        !Validator.isDateValid(declareElectionRequest.getStartAt())) return false;
        return true;
    }

}
