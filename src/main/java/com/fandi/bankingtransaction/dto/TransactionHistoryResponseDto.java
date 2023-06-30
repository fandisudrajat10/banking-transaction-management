package com.fandi.bankingtransaction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionHistoryResponseDto {

    private String destinationAccount;

    private BigDecimal amount;

    private String transactionDate;
}
