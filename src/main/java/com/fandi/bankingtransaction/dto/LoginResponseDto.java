package com.fandi.bankingtransaction.dto;

import lombok.Data;

@Data
public class LoginResponseDto {

    private String token;
    private Long expiredAt;
}
