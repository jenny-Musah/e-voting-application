package com.example.voting_app.service.electionService;

import com.example.voting_app.data.dto.requests.AddVoteRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.response.ElectionResponse;
import com.example.voting_app.data.models.Election;
import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.models.VoterCard;
import com.example.voting_app.data.repository.ElectionRepository;
import com.example.voting_app.service.nomineeService.NomineePortfolioService;
import com.example.voting_app.service.nomineeService.NomineeService;
import com.example.voting_app.service.votersService.VotersService;
import com.example.voting_app.utils.Validator;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@EnableScheduling
public class ElectionServiceImpl implements ElectionService{


    @Autowired
    private NomineeService nomineeService;

    @Autowired
    private VotersService votersService;

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private NomineePortfolioService nomineePortfolioService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Override
    public Election createElection(DeclareElectionRequest declareElectionRequest) throws MessagingException {
        if(!validateDate(declareElectionRequest)) throw new InvalidDetails("Invalid date format");
        if(LocalDate.parse(declareElectionRequest.getStartAt(),formatter).isBefore(LocalDate.now())
        || LocalDate.parse(declareElectionRequest.getEndsAt(),formatter).isBefore(LocalDate.now())) throw new InvalidDetails("date selected is not valid");
        Election election = new Election();
        election.setElectionName(declareElectionRequest.getElectionName());
        election.setStartAt(LocalDate.parse(declareElectionRequest.getStartAt(),formatter));
        election.setEndAt(LocalDate.parse(declareElectionRequest.getEndsAt(),formatter));
        for(String nomineeEmail : declareElectionRequest.getListOfNominee()){
            if(nomineeService.findNominee(nomineeEmail) != null) throw new InvalidDetails("Nominee is already added");
            Nominee nominee = nomineeService.addNominee(nomineeEmail);
            election.getListOfNominee().add(nominee);
        }
        return electionRepository.save(election);
    }

    @Override public ElectionResponse addVote(AddVoteRequest addVoteRequest, long userId) {
        VoterCard voterCard = votersService.findVoterCard(userId);
        Election declaredElection = electionRepository.findById(addVoteRequest.getElectionId()).orElseThrow(() -> new InvalidDetails("Election id: " + addVoteRequest.getElectionId() + "\n Is invalid"));
        if(!declaredElection.isActivated())return new ElectionResponse(declaredElection.getId(),declaredElection.getElectionName() +", as not yet been opened to voters");
        for (VoterCard voterCard1 : declaredElection.getListOfVoters()){
            if(voterCard1.getVotersId() == voterCard.getVotersId()) throw new InvalidDetails("Sorry, vote can be casted just once");
        }
        for(Nominee nominee : declaredElection.getListOfNominee()){
            if(nominee.getNomineePortfolio() ==null) throw new InvalidDetails("Nominee has no portfolio therefore can not be voted has no nominee id exist");
            if(nominee.getNomineePortfolio().getNomineeId() == addVoteRequest.getNomineeId()){
                nomineePortfolioService.addVote(nominee.getNomineePortfolio().getNomineeId());
                declaredElection.getListOfVoters().add(voterCard);
                electionRepository.save(declaredElection);
                return new ElectionResponse(declaredElection.getId(),"Thank you for voting " + nominee.getNomineePortfolio().getFirstName());
            }
        }
        return new ElectionResponse(declaredElection.getId(),addVoteRequest.getNomineeId() + ", is invalid.");
    }

    @Scheduled(cron = "0 */5 * * * *")
    private void electionActivation(){
    for(Election election : electionRepository.findAll()) {
        if (election.getStartAt().isEqual(LocalDate.now()) || election.getStartAt().isBefore(LocalDate.now())){
            election.setActivated(true);
           electionRepository.save(election);
            break;
        }
        else if (election.getEndAt().isBefore(LocalDate.now()))
            System.out.println(election.getEndAt().isBefore(LocalDate.now()));
            election.setActivated(false);
            electionRepository.save(election);
    }
    }

    private boolean validateDate(DeclareElectionRequest declareElectionRequest){
        if(!Validator.isDateValid(declareElectionRequest.getEndsAt()) ||
        !Validator.isDateValid(declareElectionRequest.getStartAt())) return false;
        return true;
    }

}
