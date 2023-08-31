package com.example.voting_app.service.nomineeService;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.UploadPortfolioRequest;
import com.example.voting_app.data.dto.requests.UserRegisterRequest;
import com.example.voting_app.data.dto.response.ApiResponse;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.UploadPortfolioResponse;
import com.example.voting_app.data.models.Nominee;
import com.example.voting_app.data.models.Roles;
import com.example.voting_app.data.repository.NomineeRepository;
import com.example.voting_app.service.userService.UserService;
import com.example.voting_app.service.votersService.VotersService;
import com.example.voting_app.utils.ElectionConstant;
import com.example.voting_app.utils.GeneratedResponse;
import com.example.voting_app.utils.Validator;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import com.example.voting_app.utils.mailServices.MailSender;
import jakarta.mail.MessagingException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Random;
import java.util.UUID;

@Service
public class NomineeServiceImpl implements NomineeService{

    @Autowired
    private NomineeRepository nomineeRepository;

    @Autowired
    private NomineePortfolioService nomineePortfolioService;
    @Autowired
    private MailSender mailSender;

    @Autowired
    private UserService userService;
    @Override
    public Nominee addNominee(String  nomineeEmails)  {

        if(!Validator.isEmailAddressValid(nomineeEmails) || userService.findUserByEmail(nomineeEmails) != null) throw new InvalidDetails("Invalid email address");
        String nomineePassword =  UUID.randomUUID().toString().subSequence(0,10).toString().concat("NOMI#@");
        Nominee savedNominee = createNominee(nomineeEmails,nomineePassword);
        mailSender.send(nomineeEmails,mailSender.buildEmail(savedNominee.getUser().getLoginId(),nomineePassword)
        ,"Nominee login details");
        return savedNominee;

    }

    @Override
        public ApiResponse uploadPortfolio(UploadPortfolioRequest uploadPortfolioRequest,long id) {
        Nominee nominee = nomineeRepository.findById(id).orElseThrow(()-> new InvalidDetails(ElectionConstant.INVALID_DETAILS));
        nominee.setNomineePortfolio(nomineePortfolioService.uploadPortfolio(uploadPortfolioRequest));
        return GeneratedResponse.okResponse(nomineeRepository.save(nominee));
    }

    @Override public void addVote(long nomineeId) {
     nomineePortfolioService.findPortfolio(nomineeId);
           nomineePortfolioService.addVote(nomineeId);
    }

    @Override public Nominee findNominee(String nomineeEmail) {
        for(Nominee nominee : nomineeRepository.findAll()){
            if (nominee.getUser().getEmailAddress().equals(userService.findUserByEmail(nomineeEmail).getEmailAddress())) return nominee;
        }
        return null;
    }

    @Override public void deleteAll() {
        nomineeRepository.deleteAll();
    }

    private Nominee createNominee(String email, String password){
        Nominee nominee = new Nominee();
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setPassword(password);
        userRegisterRequest.setEmailAddress(email);
        nominee.setUser(userService.createUser(userRegisterRequest, Roles.NOMINEE));
        return nomineeRepository.save(nominee);
    }


}
