package com.example.voting_app.service.electionService;

import com.example.voting_app.data.dto.requests.AddNomineeRequest;
import com.example.voting_app.data.dto.requests.AddVoteRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.response.ApiResponse;
import com.example.voting_app.data.dto.response.ElectionResponse;
import com.example.voting_app.data.models.Election;
import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.models.VoterCard;
import com.example.voting_app.data.repository.ElectionRepository;
import com.example.voting_app.service.nomineeService.NomineePortfolioService;
import com.example.voting_app.service.nomineeService.NomineeService;
import com.example.voting_app.service.votersService.VotersService;
import com.example.voting_app.utils.ElectionConstant;
import com.example.voting_app.utils.GeneratedResponse;
import com.example.voting_app.utils.Validator;
import com.example.voting_app.utils.exceptions.ElectionException;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

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
    public ApiResponse createElection(DeclareElectionRequest declareElectionRequest){
        validateDate(declareElectionRequest);
        Election election = buildElectionObject(declareElectionRequest);
        return GeneratedResponse.okResponse(electionRepository.save(election));
    }

    private Election buildElectionObject(DeclareElectionRequest declareElectionRequest) {
        Election election = new Election();
        election.setElectionName(declareElectionRequest.getElectionName());
        election.setStartAt(LocalDate.parse(declareElectionRequest.getStartAt(),formatter));
        election.setEndAt(LocalDate.parse(declareElectionRequest.getEndsAt(),formatter));
        for(String nomineeEmail : declareElectionRequest.getListOfNominee()){
            if(findNominee(election, nomineeEmail) != null) throw new InvalidDetails("Nominee is already added");
            Nominee nominee = nomineeService.addNominee(nomineeEmail);
            election.getListOfNominee().add(nominee);
        }
        return election;
    }

    private Nominee findNominee(Election election, String nomineeEmail) {
        if(!election.getListOfNominee().isEmpty()) {
            for (Nominee nominee : election.getListOfNominee()) {
                if (nominee.getUser().getEmailAddress().equals(nomineeEmail)) return nominee;
            }
        }
        return null;
    }

    @Override public ApiResponse addVote(AddVoteRequest addVoteRequest, long userId) {
        VoterCard voterCard = votersService.findVoterCard(userId);
        Election declaredElection = findElection(addVoteRequest.getElectionId());
        if(!declaredElection.isActivated())throw new ElectionException(ElectionConstant.ELECTION_NON_ACTIVATED);
        for (VoterCard voterCard1 : declaredElection.getListOfVoters())
            if(voterCard1.getVotersId() == voterCard.getVotersId()) throw new InvalidDetails(ElectionConstant.CAN_NOT_VOTE);
        for(Nominee nominee : declaredElection.getListOfNominee()){
            if(nominee.getNomineePortfolio() == null ) continue;
            if(nominee.getNomineePortfolio().getNomineeId() == addVoteRequest.getNomineeId()){
                nomineePortfolioService.addVote(nominee.getNomineePortfolio().getNomineeId());
                declaredElection.getListOfVoters().add(voterCard);
               return GeneratedResponse.okResponse( electionRepository.save(declaredElection) );
            }
        }
       throw new ElectionException(ElectionConstant.INVALID_NOMINEE);
    }

    @Override public ApiResponse addNominee(AddNomineeRequest addNomineeRequest) {
        Election election = findElection(addNomineeRequest.getElectionId());
        for(Nominee nominee : election.getListOfNominee())
            if(Objects.equals(nominee.getUser().getEmailAddress(), addNomineeRequest.getNomineeMail())) throw  new InvalidDetails(ElectionConstant.NOMINEE_ALREADY_EXIST);
        Nominee nominee = nomineeService.addNominee(addNomineeRequest.getNomineeMail());
        election.getListOfNominee().add(nominee);
        return GeneratedResponse.okResponse( electionRepository.save(election));
    }

    @Override public List<Election> findAllElections() {
        return electionRepository.findAll();
    }

    private Election findElection(long electionId) {
        return electionRepository.findById(electionId).orElseThrow(() ->
                new InvalidDetails("Election with " + electionId +" does not exist"));
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

    private void validateDate(DeclareElectionRequest declareElectionRequest){
        if(!Validator.isDateValid(declareElectionRequest.getEndsAt()) || !Validator.isDateValid(declareElectionRequest.getStartAt())) throw new InvalidDetails(ElectionConstant.INVALID_DATE);
        if(LocalDate.parse(declareElectionRequest.getStartAt(),formatter).isBefore(LocalDate.now())
                || LocalDate.parse(declareElectionRequest.getEndsAt(),formatter).isBefore(LocalDate.now())) throw new InvalidDetails(ElectionConstant.INVALID_DATE);
    }

}
