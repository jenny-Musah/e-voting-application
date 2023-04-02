package com.example.voting_app.service.userService;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.UserRegisterRequest;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.Response;
import com.example.voting_app.data.models.Roles;
import com.example.voting_app.data.models.User;
import com.example.voting_app.data.models.VoterCard;
import com.example.voting_app.data.repository.UserRepository;
import com.example.voting_app.data.repository.VotersCardRepository;
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

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VotersService votersService;

    @Autowired
    private MailSender mailSender;

    @Override
    public Response registerUser(UserRegisterRequest userRegisterRequest) throws MessagingException {
        if((!Validator.isEmailAddressValid(userRegisterRequest.getEmailAddress()) || userRepository.findUserByEmailAddress(userRegisterRequest.getEmailAddress()) != null)) throw new InvalidDetails("Invalid details");
        if(!Validator.isPasswordValid(userRegisterRequest.getPassword())) throw new InvalidDetails("Invalid details");
        User savedUser = userRepository.save(creatUser(userRegisterRequest));
        mailSender.send(savedUser.getEmailAddress(),mailSender.buildEmail(savedUser.getLoginId(),userRegisterRequest.getPassword()),"Login Details");
        return response(savedUser.getLoginId(),HttpStatus.OK.value(), "Registration successful, check your mail for login details");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User savedUser = userRepository.findUserByLoginId(loginRequest.getLoginId()).orElseThrow(() -> new InvalidDetails("Invalid details"));
        if(!BCrypt.checkpw(loginRequest.getPassword(),savedUser.getPassword())) throw new InvalidDetails("Invalid details");
       LoginResponse loginResponse = new LoginResponse();
       loginResponse.setUsersId(savedUser.getId());
       loginResponse.setStatusCode(HttpStatus.OK.value());
       loginResponse.setMessage("Login successful");
        return loginResponse;
    }

    private Response response(long loginId, int statusCode, String message ){
        Response response = new Response();
        response.setMessage(message); response.setStatusCode(statusCode);response.setLoginId(loginId);
        return response;
    }
    private User creatUser(UserRegisterRequest userRegisterRequest){
        User user = new User();
        user.setEmailAddress(userRegisterRequest.getEmailAddress());
        user.setPassword(BCrypt.hashpw(userRegisterRequest.getPassword(),BCrypt.gensalt()));;
        user.getUsersRoles().add(Roles.VOTER);
        user.setLoginId(generateId());
        User savedUser= userRepository.save(user);
        savedUser.setVoterCard(votersService.creatVotersCard(savedUser.getId()));
        return savedUser;
    }
    private long generateId(){
        Random random = new Random();
        return Math.abs(random.nextLong());
    }




}
