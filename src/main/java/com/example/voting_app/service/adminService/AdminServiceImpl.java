package com.example.voting_app.service.adminService;

import com.example.voting_app.data.dto.requests.AdminLoginRequest;
import com.example.voting_app.data.dto.requests.DeclareElectionRequest;
import com.example.voting_app.data.dto.response.ElectionResponse;
import com.example.voting_app.data.dto.response.LoginResponse;
import com.example.voting_app.data.models.Admin;
import com.example.voting_app.data.models.Election;
import com.example.voting_app.data.models.Roles;
import com.example.voting_app.data.repository.AdminRepository;
import com.example.voting_app.service.electionService.ElectionService;
import com.example.voting_app.utils.exceptions.InvalidDetails;
import jakarta.mail.MessagingException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AdminServiceImpl implements AdminService{

    private final Admin admin = new Admin();

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ElectionService electionService;

    @Override public LoginResponse login(AdminLoginRequest adminLoginRequest) {
        creatAdmin();
        Admin savedAdmin = adminRepository.findByLoginId(adminLoginRequest.getLoginId()).orElseThrow(() -> new InvalidDetails("Invalid details"));
        if(!BCrypt.checkpw(adminLoginRequest.getPassword(),savedAdmin.getPassword())) throw new InvalidDetails("Invalid details");
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setVoteId(savedAdmin.getVoteId()); loginResponse.setMessage("Admin logged in successfully");
        return loginResponse;
    }

    @Override public ElectionResponse createElection(DeclareElectionRequest creatElectionRequest) throws MessagingException {
        Election election = electionService.createElection(creatElectionRequest);
        ElectionResponse electionResponse = new ElectionResponse();
        electionResponse.setElectionId(election.getId()); electionResponse.setMessage("Successfully declared an election");
        return electionResponse;
    }

    private void creatAdmin(){
        admin.setLoginId(90078967900000L);
        admin.setPassword(BCrypt.hashpw("IamTheAdmin#123@",BCrypt.gensalt()));
        admin.setVoteId(generateId());
        admin.getAdminRoles().add(Roles.ADMIN);
        adminRepository.save(admin);
    }
    private long generateId(){
        Random random = new Random();
        return Math.abs(random.nextLong());
    }
}
