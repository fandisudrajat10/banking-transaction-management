package com.fandi.bankingtransaction.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserUpdatePasswordRequestDto {

    @NotBlank
    @Size(min = 8, max = 100)
    private String newPassword;

    @NotBlank
    @Size(min = 8, max = 100)
    private String oldPassword;
}
