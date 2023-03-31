package com.example.voting_app.service.userService;

import com.example.voting_app.data.dto.requests.UserLoginRequest;
import com.example.voting_app.data.dto.requests.UserRegisterRequest;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.Response;
import com.example.voting_app.data.models.Roles;
import com.example.voting_app.data.models.User;
import com.example.voting_app.data.repository.UserRepository;
import com.example.voting_app.service.userService.UserService;
import com.example.voting_app.utils.Validator;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import com.example.voting_app.utils.mailServices.MailSender;
import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Override
    public Response registerUser(UserRegisterRequest userRegisterRequest) throws MessagingException {
        if((!Validator.isEmailAddressValid(userRegisterRequest.getEmailAddress()) || userRepository.findUserByEmailAddress(userRegisterRequest.getEmailAddress()) != null)) throw new InvalidDetails("Invalid details");
        if(!Validator.isPasswordValid(userRegisterRequest.getPassword())) throw new InvalidDetails("Invalid details");
        User savedUser = userRepository.save(creatUser(userRegisterRequest));
        mailSender.send(savedUser.getEmailAddress(),mailSender.buildEmail(savedUser.getLoginId(),userRegisterRequest.getPassword()),"Login Details");
        Response response = new Response();
        response.setMessage("Registration successful, check your mail for login details"); response.setLoginId(savedUser.getLoginId());  response.setStatusCode(HttpStatus.OK.value());
        return response;
    }

    @Override
    public LoginResponse login(UserLoginRequest userLoginRequest) {
        User savedUser = userRepository.findById(userLoginRequest.getLoginId()).orElseThrow(() -> new InvalidDetails("Invalid details"));
        if(!BCrypt.checkpw(userLoginRequest.getPassword(),savedUser.getPassword())) throw new InvalidDetails("Invalid details");
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage("Login successful"); loginResponse.setVoteId(savedUser.getVoteId());
        return loginResponse;
    }

    private User creatUser(UserRegisterRequest userRegisterRequest){
        User user = new User();
        user.setEmailAddress(userRegisterRequest.getEmailAddress());
        user.setPassword(BCrypt.hashpw(userRegisterRequest.getPassword(),BCrypt.gensalt()));
        user.setVoteId(generateId());
        user.getUsersRoles().add(Roles.VOTER);
        return user;
    }

    private long generateId(){
        Random random = new Random();
        return Math.abs(random.nextLong());
    }


}
