package com.fandi.bankingtransaction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyAccountResponseDto {

    private String accountNumber;

    private String name;

    private String address;

    private String email;

    private BigDecimal balance;
}
