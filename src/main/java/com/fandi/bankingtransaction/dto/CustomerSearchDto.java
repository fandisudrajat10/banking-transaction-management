package com.fandi.bankingtransaction.dto;

import lombok.Data;

@Data
public class CustomerSearchDto extends SearchDto{

    private String accountNo;

    private String name;

    private String email;

    private String address;
}
