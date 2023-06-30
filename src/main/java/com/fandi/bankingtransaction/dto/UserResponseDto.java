package com.fandi.bankingtransaction.dto;

import lombok.Data;

@Data
public class UserResponseDto {

    private String name;

    private String email;

    private String initialPassword;

    private String role;
}
