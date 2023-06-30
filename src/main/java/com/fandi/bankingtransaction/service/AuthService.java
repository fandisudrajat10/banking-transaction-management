package com.fandi.bankingtransaction.service;

import com.fandi.bankingtransaction.dto.LoginRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(LoginRequestDto requestDto);
}
