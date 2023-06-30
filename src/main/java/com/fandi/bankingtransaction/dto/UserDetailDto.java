package com.fandi.bankingtransaction.dto;

import lombok.Data;

@Data
public class UserDetailDto {

    private Long id;

    private String name;

    private String email;

    private String role;
}
