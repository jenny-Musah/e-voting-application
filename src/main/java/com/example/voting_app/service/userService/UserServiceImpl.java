package com.example.voting_app.service.userService;

import com.example.voting_app.data.dto.requests.LoginRequest;
import com.example.voting_app.data.dto.requests.UserRegisterRequest;
import com.example.voting_app.data.dto.response.ApiResponse;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.Response;
import com.example.voting_app.data.models.Roles;
import com.example.voting_app.data.models.User;
import com.example.voting_app.data.repository.UserRepository;
import com.example.voting_app.service.votersService.VotersService;
import com.example.voting_app.utils.GeneratedResponse;
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
    public ApiResponse registerUser(UserRegisterRequest userRegisterRequest) throws MessagingException {
        if((!Validator.isEmailAddressValid(userRegisterRequest.getEmailAddress()) || userRepository.findUserByEmailAddress(userRegisterRequest.getEmailAddress()) != null)) throw new InvalidDetails("Invalid details");
        if(!Validator.isPasswordValid(userRegisterRequest.getPassword())) throw new InvalidDetails("Invalid details");
        User savedUser = userRepository.save(createUser(userRegisterRequest, Roles.VOTER));
        mailSender.send(savedUser.getEmailAddress(),mailSender.buildEmail(savedUser.getLoginId(),userRegisterRequest.getPassword()),"Login Details");
        return GeneratedResponse.okResponse(savedUser);
    }

    @Override
    public ApiResponse login(LoginRequest loginRequest) {
        User savedUser = userRepository.findUserByLoginId(loginRequest.getLoginId()).orElseThrow(() -> new InvalidDetails("Invalid details"));
        if(!BCrypt.checkpw(loginRequest.getPassword(),savedUser.getPassword())) throw new InvalidDetails("Invalid details");
        return GeneratedResponse.okResponse(savedUser);
    }

    @Override
    public User createAdmin(UserRegisterRequest userRegisterRequest, long loginId) {
        User user = new User();
        user.setEmailAddress(userRegisterRequest.getEmailAddress());
        user.setPassword(BCrypt.hashpw(userRegisterRequest.getPassword(),BCrypt.gensalt()));;
        user.getUsersRoles().add(Roles.ADMIN);
        user.setLoginId(loginId);
        User savedUser= userRepository.save(user);
        savedUser.setVoterCard(votersService.creatVotersCard(savedUser.getId()));
        return userRepository.save(savedUser);
    }

    @Override
    public User createUser(UserRegisterRequest userRegisterRequest, Roles roles) {
        User user = new User();
        user.setEmailAddress(userRegisterRequest.getEmailAddress());
        user.setPassword(BCrypt.hashpw(userRegisterRequest.getPassword(),BCrypt.gensalt()));;
        user.getUsersRoles().add(roles);
        user.setLoginId(generateId());
        User savedUser= userRepository.save(user);
        savedUser.setVoterCard(votersService.creatVotersCard(savedUser.getId()));
        return userRepository.save(savedUser);
    }

    @Override public User findUserByEmail(String nomineeEmails) {
        return userRepository.findUserByEmailAddress(nomineeEmails);
    }

    private Response response(long loginId, int statusCode, String message ){
        Response response = new Response();
        response.setMessage(message); response.setStatusCode(statusCode);response.setLoginId(loginId);
        return response;
    }

    private long generateId(){
        Random random = new Random();
        return Math.abs(random.nextLong());
    }




}
