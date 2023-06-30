package com.fandi.bankingtransaction.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CustomerUpdateRequestDto {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 255)
    private String address;

}
