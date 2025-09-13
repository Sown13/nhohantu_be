package com.nhohantu.tcbookbe.common.validation;

import com.nhohantu.tcbookbe.common.annotation.IsValidRole;
import com.nhohantu.tcbookbe.common.model.enums.RoleEnum;
import com.nhohantu.tcbookbe.common.service.UserBasicInfoService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleValidator implements ConstraintValidator<IsValidRole, String> {
    private final UserBasicInfoService userBasicInfoService;

    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        RoleEnum requestRole;
        try {
            requestRole = RoleEnum.valueOf(role.toUpperCase());
        } catch (Exception e) {
            return false;
        }

        if (requestRole.equals(RoleEnum.ROOT)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Can not create user with this role")
                    .addConstraintViolation();
            return false;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate("You do not have permission for creating account with this ROLE").addConstraintViolation();
        return false;
    }

    @Override
    public void initialize(IsValidRole constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }


}
