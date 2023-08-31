package com.example.voting_app.service;


import com.example.voting_app.data.dto.requests.AdminLoginRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.response.ElectionResponse;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.repository.AdminRepository;
import com.example.voting_app.service.adminService.AdminService;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@Transactional
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    public void setUp(){
        adminRepository.deleteAll();
    }



}
