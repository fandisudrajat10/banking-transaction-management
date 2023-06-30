package com.fandi.bankingtransaction.dto;

import lombok.Data;

@Data
public class TransactionHistoryRequestDto extends SearchDto{

    private String startDate;

    private String endDate;
}
