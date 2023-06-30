package com.fandi.bankingtransaction.service.impl;

import com.fandi.bankingtransaction.domain.User;
import com.fandi.bankingtransaction.dto.UserUpdatePasswordRequestDto;
import com.fandi.bankingtransaction.service.AccountService;
import com.fandi.bankingtransaction.service.UserService;
import com.fandi.bankingtransaction.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(UserUpdatePasswordRequestDto requestDto) {
        validationService.validateRequest(requestDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password change failed. The entered old password is incorrect. Please double-check the old password you entered and try again.");
        }
        if (requestDto.getNewPassword().equals(requestDto.getOldPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password change failed. The new password must be different from the old password. Please enter a different new password.");
        }
        userService.changeUserPassword(requestDto.getNewPassword());
    }

}
