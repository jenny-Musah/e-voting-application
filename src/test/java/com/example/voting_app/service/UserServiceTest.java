package com.example.voting_app.service;

import com.example.voting_app.data.dto.requests.UserLoginRequest;
import com.example.voting_app.data.dto.requests.UserRegisterRequest;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.Response;
import com.example.voting_app.data.repository.UserRepository;
import com.example.voting_app.service.userService.UserService;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {

    private UserRegisterRequest userRegisterRequest;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Name("Clears the database before each test")
    public void tearDown(){
        userRepository.deleteAll();
    }



    @Test
    @Name("Test that user can register")
    public void testThatUserCanRegister() throws MessagingException {
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setPassword("Jennymush#123");
        userRegisterRequest.setEmailAddress("jennymusah99@gmail.com");
        Response response = userService.registerUser(userRegisterRequest);
        assertEquals("Registration successful, check your mail for login details", response.getMessage());
    }

    @Test
    @Name("Test that user can not register with the same email")
    public void testThatUserCanNotRegisterWithTheSameEmailAddress() throws MessagingException {
        testThatUserCanRegister();
        userRegisterRequest.setPassword("Jennymush#123");
        userRegisterRequest.setEmailAddress("jennymusah99@gmail.com");
        assertThrows(InvalidDetails.class,() ->userService.registerUser(userRegisterRequest));
    }

    @Test
    @Name("Test that user can login")
    public void testThatUserCanLogin() throws MessagingException {
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setPassword("Jennymush#123");
        userRegisterRequest.setEmailAddress("jennymusah99@gmail.com");
        Response response = userService.registerUser(userRegisterRequest);
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setLoginId(response.getLoginId());
        userLoginRequest.setPassword("Jennymush#123");
        LoginResponse response2 = userService.login(userLoginRequest);
        assertEquals("Login successful", response2.getMessage());
    }
    @Test
    @Name("Test that user can not login with invalid login id")
    public void testThatUserCanNotLoginWithInvalidDetails() throws MessagingException {
        testThatUserCanRegister();
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setLoginId(3451818290200l);
        userLoginRequest.setPassword("Jennymush#123");
       assertThrows(InvalidDetails.class, () -> userService.login(userLoginRequest));
    }

    @Test
    @Name("Test that user can not login with invalid password")
    public void testThatUserCanNotLoginWithInvalidPassword() throws MessagingException {
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setPassword("Jennymush#123");
        userRegisterRequest.setEmailAddress("jennymusah99@gmail.com");
        Response response = userService.registerUser(userRegisterRequest);
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setLoginId(response.getLoginId());
        userLoginRequest.setPassword("Jennymu");
        assertThrows(InvalidDetails.class, () -> userService.login(userLoginRequest));
    }


}
