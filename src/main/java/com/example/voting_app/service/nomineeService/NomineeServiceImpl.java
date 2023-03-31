package com.example.voting_app.service.nomineeService;

import com.example.voting_app.data.dto.requests.NomineeDetailsRequest;
import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.models.Roles;
import com.example.voting_app.data.repository.NomineeRepository;
import com.example.voting_app.utils.Validator;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import com.example.voting_app.utils.mailServices.MailSender;
import jakarta.mail.MessagingException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class NomineeServiceImpl implements NomineeService{

    @Autowired
    private NomineeRepository nomineeRepository;
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


    private long generateId(){
        Random random = new Random();
        return Math.abs(random.nextLong());
    }
    private Nominee createNominee(NomineeDetailsRequest nomineeDetailsRequest, String password){
        Nominee nominee = new Nominee();
        nominee.setVoteId(generateId()); nominee.setRoles(Roles.NOMINEE); nominee.setPosition(nomineeDetailsRequest.getPosition());
        nominee.setEmail(nomineeDetailsRequest.getEmail());
        nominee.setPassword(BCrypt.hashpw(password,BCrypt.gensalt())); nominee.setLastName(nomineeDetailsRequest.getLastName());
        nominee.setFirstName(nomineeDetailsRequest.getFirstName());
        return nominee;
    }
}
