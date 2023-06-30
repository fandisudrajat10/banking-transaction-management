package com.fandi.bankingtransaction.dto;

import com.fandi.bankingtransaction.validator.ValidRole;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class CustomerRegisterRequestDto {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @DecimalMin(value = "0")
    private BigDecimal initialBalance;



}
