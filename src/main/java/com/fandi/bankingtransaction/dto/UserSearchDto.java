package com.fandi.bankingtransaction.dto;

import lombok.Data;

@Data
public class UserSearchDto extends SearchDto{

    private String name;

    private String email;
}
