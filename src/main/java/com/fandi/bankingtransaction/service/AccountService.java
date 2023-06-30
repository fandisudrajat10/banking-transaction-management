package com.fandi.bankingtransaction.service;

import com.fandi.bankingtransaction.dto.UserUpdatePasswordRequestDto;

public interface AccountService {

    void changePassword(UserUpdatePasswordRequestDto requestDto);
}
