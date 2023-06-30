package com.fandi.bankingtransaction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {

    private String destinationAccountNo;

    private BigDecimal amount;
}
