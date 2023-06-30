package com.fandi.bankingtransaction.service.impl;

import com.fandi.bankingtransaction.domain.User;
import com.fandi.bankingtransaction.dto.LoginRequestDto;
import com.fandi.bankingtransaction.dto.LoginResponseDto;
import com.fandi.bankingtransaction.security.JwtTokenProvider;
import com.fandi.bankingtransaction.service.AuthService;
import com.fandi.bankingtransaction.service.UserService;
import com.fandi.bankingtransaction.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private ValidationService validationService;

    @Override
    public ResponseEntity<?> login(LoginRequestDto loginRequest) {

        validationService.validateRequest(loginRequest);
        try {
            String email = loginRequest.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword()));
            User user = userService.findByEmail(email);
            LoginResponseDto responseDto = jwtTokenProvider.createToken(email, user.getRole().toString());

            return ResponseEntity.ok(responseDto);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
