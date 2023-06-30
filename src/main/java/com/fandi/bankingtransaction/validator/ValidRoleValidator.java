package com.fandi.bankingtransaction.validator;

import com.fandi.bankingtransaction.domain.Role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidRoleValidator implements ConstraintValidator<ValidRole, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (Role role : Role.values()) {
            if (role.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
