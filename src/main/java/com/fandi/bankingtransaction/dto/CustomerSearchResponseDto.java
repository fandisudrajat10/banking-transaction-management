package com.fandi.bankingtransaction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerSearchResponseDto {

    private Long id;

    private String accountNo;

    private String name;

    private String email;

    private String address;

    private BigDecimal balance;

}
