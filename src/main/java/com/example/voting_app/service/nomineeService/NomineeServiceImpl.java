package com.example.voting_app.service.nomineeService;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.NomineeDetailsRequest;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.Response;
import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.models.Roles;
import com.example.voting_app.data.repository.NomineeRepository;
import com.example.voting_app.service.votersService.VotersService;
import com.example.voting_app.utils.Validator;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import com.example.voting_app.utils.mailServices.MailSender;
import jakarta.mail.MessagingException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class NomineeServiceImpl implements NomineeService{

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private VotersService votersService;
    @Autowired
    private MailSender mailSender;
    @Override
    public Nominee addNominee(NomineeDetailsRequest nomineeDetailsRequest) throws MessagingException {
        if(!Validator.isEmailAddressValid(nomineeDetailsRequest.getEmail()) || nomineeRepository.findNomineeByEmail(nomineeDetailsRequest.getEmail()) != null) throw new InvalidDetails("Invalid email address");
        String nomineePassword =  UUID.randomUUID().toString().subSequence(0,10).toString().concat("NOMI#@");
        Nominee savedNominee = nomineeRepository.save(createNominee(nomineeDetailsRequest,nomineePassword));
        mailSender.send(nomineeDetailsRequest.getEmail(),mailSender.buildEmail(savedNominee.getLoginId(),nomineePassword)
        ,"Nominee login details");
        return savedNominee;
    }

    @Override
    public void addVote(long nomineeId) {
    Nominee nominee = nomineeRepository.findNomineeByNomineeId(nomineeId).orElseThrow(()-> new InvalidDetails("Nominee Id is invalid"));
    nominee.setVotes(nominee.getVotes() + 1);
    nomineeRepository.save(nominee);
    }

    @Override public LoginResponse login(LoginRequest loginRequest) {
        Nominee savedNominee = nomineeRepository.findByLoginId(loginRequest.getLoginId()).orElseThrow(() -> new InvalidDetails("Invalid details"));
        if(!BCrypt.checkpw(loginRequest.getPassword(),savedNominee.getPassword())) throw new InvalidDetails("Invalid details");
        return response(savedNominee.getId(), HttpStatus.OK.value(),"Login successful");
    }

    private LoginResponse response(long userId, int statusCode, String message ){
        LoginResponse response = new LoginResponse();
        response.setMessage(message); response.setStatusCode(statusCode);response.setUsersId(userId);
        return response;
    }

    private Nominee createNominee(NomineeDetailsRequest nomineeDetailsRequest, String password){
        Nominee nominee = new Nominee();
        nominee.setEmail(nomineeDetailsRequest.getEmail());
        nominee.setPassword(BCrypt.hashpw(password,BCrypt.gensalt())); nominee.setLastName(nomineeDetailsRequest.getLastName());
        nominee.setFirstName(nomineeDetailsRequest.getFirstName());
        nominee.setLoginId(generateId());
        nominee.setRoles(Roles.NOMINEE);
        nominee.setNomineeId(generateId());
        nominee.setPosition(nomineeDetailsRequest.getPosition());
        Nominee savedNominee = nomineeRepository.save(nominee);
        savedNominee.setVoterCard(votersService.creatVotersCard(savedNominee.getId()));
        return savedNominee;
    }




    private long generateId(){
        Random random = new Random();
        return Math.abs(random.nextLong());
    }

}
