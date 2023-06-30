package com.fandi.bankingtransaction.controller;

import com.fandi.bankingtransaction.dto.*;
import com.fandi.bankingtransaction.service.UserService;
import com.fandi.bankingtransaction.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ValidationService validationService;


    @GetMapping("/user/{id}")
    public WebResponse<UserDetailDto> getUsers(@PathVariable Long id) {
        UserDetailDto responseDto = userService.getUserById(id);
        return WebResponse.<UserDetailDto>builder().data(responseDto).build();
    }


    @GetMapping("/users")
    public WebResponse<Page<SearchUserResponseDto>> searchUser(UserSearchDto userSearchDto) {
        Page<SearchUserResponseDto> responseDto = userService.searchUserByNameAndEmail(userSearchDto);
        return WebResponse.<Page<SearchUserResponseDto>>builder().data(responseDto).build();
    }

    @PostMapping(path = "/user")
    public WebResponse<UserResponseDto> registerUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.registerUser(requestDto);
        return WebResponse.<UserResponseDto>builder().data(responseDto).build();
    }

}
