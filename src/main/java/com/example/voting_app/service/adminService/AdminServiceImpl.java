package com.example.voting_app.service.adminService;

import com.example.voting_app.data.dto.requests.AdminLoginRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.response.ElectionResponse;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.dto.response.Response;
import com.example.voting_app.data.models.Admin;
import com.example.voting_app.data.models.Election;
import com.example.voting_app.data.models.Roles;
import com.example.voting_app.data.repository.AdminRepository;
import com.example.voting_app.service.electionService.ElectionService;
import com.example.voting_app.service.votersService.VotersService;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class AdminServiceImpl implements AdminService{

    private final Admin admin = new Admin();

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private VotersService votersService;

    @Autowired
    private ElectionService electionService;

    @Override public LoginResponse login(AdminLoginRequest adminLoginRequest) {;
        Admin savedAdmin = creatAdmin();
        if(!BCrypt.checkpw(adminLoginRequest.getPassword(),savedAdmin.getPassword()) || savedAdmin.getLoginId() != adminLoginRequest.getLoginId()) throw new InvalidDetails("Invalid details");
       LoginResponse loginResponse = new LoginResponse();
       loginResponse.setUsersId(savedAdmin.getId()); loginResponse.setStatusCode(HttpStatus.OK.value());
       loginResponse.setMessage("Admin logged in successfully");
        return loginResponse;
    }

    @Override public ElectionResponse createElection(DeclareElectionRequest creatElectionRequest) throws MessagingException {
        Election election = electionService.createElection(creatElectionRequest);
        ElectionResponse electionResponse = new ElectionResponse();
        electionResponse.setElectionId(election.getId()); electionResponse.setMessage("Successfully declared an election");
        return electionResponse;
    }

    private Admin creatAdmin(){
        admin.setLoginId(90078967900000L);
        admin.setPassword(BCrypt.hashpw("IamTheAdmin#123@",BCrypt.gensalt()));
        admin.getAdminRoles().add(Roles.ADMIN);
        admin.setVoterCard(votersService.creatVotersCard(admin.getId()));
        return adminRepository.save(admin);
    }

}
