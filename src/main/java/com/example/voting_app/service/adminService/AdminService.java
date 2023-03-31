package com.example.voting_app.service.adminService;

import com.example.voting_app.data.dto.requests.AdminLoginRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.response.ElectionResponse;
import com.example.voting_app.data.dto.response.LoginResponse;
import jakarta.mail.MessagingException;

public interface AdminService {

    LoginResponse login(AdminLoginRequest adminLoginRequest);

    ElectionResponse createElection(DeclareElectionRequest creatElectionRequest) throws MessagingException;
}
