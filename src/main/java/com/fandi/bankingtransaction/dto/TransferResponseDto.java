package com.fandi.bankingtransaction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferResponseDto {

    private String accountNo;

    private String destinationAccountNo;

    private BigDecimal amount;

    private String status;
}
