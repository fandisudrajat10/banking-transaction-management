package com.fandi.bankingtransaction.service;

import com.fandi.bankingtransaction.domain.User;
import com.fandi.bankingtransaction.dto.*;
import org.springframework.data.domain.Page;

public interface UserService {

    void createFirstAdmin();

    User findByEmail(String email);

    UserResponseDto registerUser(UserRequestDto requestDto);

    UserDetailDto getMyProfile();

    UserDetailDto getUserById(Long id);

    CustomerRegisterResponseDto registerNewCustomer(CustomerRegisterRequestDto requestDto);

    UserDetailDto getUserByEmail(Long id);

    void updateUserByEmail(Long id, UserRequestDto requestDto);

    void deleteUserByUserId(Long id);

    void changeUserPassword(String newPassword);

    Page<SearchUserResponseDto> searchUserByNameAndEmail(UserSearchDto userSearchDto);
}
