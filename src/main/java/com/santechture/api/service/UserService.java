package com.santechture.api.service;

import com.santechture.api.dto.GeneralResponse;
import com.santechture.api.dto.user.UserDto;
import com.santechture.api.entity.Admin;
import com.santechture.api.entity.User;
import com.santechture.api.exception.BusinessExceptions;
import com.santechture.api.repository.UserRepository;
import com.santechture.api.validation.AddUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public ResponseEntity<GeneralResponse> list(Pageable pageable){
        return new GeneralResponse().response(userRepository.findAll(pageable));
    }

    public ResponseEntity<GeneralResponse> addNewUser(AddUserRequest request) throws BusinessExceptions {

        if(userRepository.existsByUsernameIgnoreCase(request.getUsername())){
            throw new BusinessExceptions("username.exist");
        } else if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BusinessExceptions("email.exist");
        }

        User user = new User(request.getUsername(),request.getEmail(),request.getPassword());
        userRepository.save(user);
        //clear the security context from the admin
        SecurityContextHolder.getContext().setAuthentication(null);
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        User authenticatedPrincipal = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessTokenForUser(authenticatedPrincipal);
        return new GeneralResponse().response(accessToken);
    }

}