package com.fandi.bankingtransaction.dto;

import lombok.Data;

@Data
public class SearchUserResponseDto {

    private Long id;

    private String customerName;

    private String customerEmail;

    private String role;
}
