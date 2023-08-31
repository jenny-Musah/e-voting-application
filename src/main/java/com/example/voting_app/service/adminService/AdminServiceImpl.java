package com.example.voting_app.service.adminService;

import com.example.voting_app.data.dto.requests.UserRegisterRequest;
import com.example.voting_app.data.models.Admin;
import com.example.voting_app.data.repository.AdminRepository;
import com.example.voting_app.service.userService.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class AdminServiceImpl implements AdminService{

    private final Admin admin = new Admin();

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserService userService;




    @PostConstruct
    private Admin creatAdmin(){
     UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
     userRegisterRequest.setPassword("IamTheAdmin#123@");
        admin.setUser(userService.createAdmin(userRegisterRequest,90078967900000L));
        return adminRepository.save(admin);
    }

    @Override public void deleteAll() {
        adminRepository.deleteAll();
    }
}
