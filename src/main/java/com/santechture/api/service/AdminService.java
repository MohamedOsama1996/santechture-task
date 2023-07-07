package com.santechture.api.service;

import com.santechture.api.dto.GeneralResponse;
import com.santechture.api.entity.Admin;
import com.santechture.api.exception.BusinessExceptions;
import com.santechture.api.repository.AdminRepository;
import com.santechture.api.validation.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Logger;

@Service
public class AdminService {


    private final AdminRepository adminRepository;

    @Autowired
    AuthenticationManager authmanger;

    @Autowired
    JwtService jwtService;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public ResponseEntity<GeneralResponse> login(LoginRequest request) throws BusinessExceptions {


            Admin admin = adminRepository.findByUsernameIgnoreCase(request.getUsername());

            if(Objects.isNull(admin) || !admin.getPassword().equals(request.getPassword())){
                throw new BusinessExceptions("login.credentials.not.match");
            }
            Authentication authentication = authmanger.authenticate( new UsernamePasswordAuthenticationToken(admin.getUsername(),admin.getPassword()));
            Admin authenticatedPrincipal = (Admin) authentication.getPrincipal();
            String accessToken = jwtService.generateAccessToken(authenticatedPrincipal);

            return new GeneralResponse().response(accessToken);



    }
}
