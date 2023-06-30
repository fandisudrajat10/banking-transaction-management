package com.fandi.bankingtransaction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerRegisterResponseDto {

    private Long id;

    private String accountNumber;

    private String name;

    private String address;

    private String email;

    private BigDecimal balance;

    private String initialPassword;
}
