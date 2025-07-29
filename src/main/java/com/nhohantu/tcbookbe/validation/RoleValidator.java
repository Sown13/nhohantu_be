package com.nhohantu.tcbookbe.validation;

import com.nhohantu.tcbookbe.annotation.IsValidRole;
import com.nhohantu.tcbookbe.model.entity.UserBasicInfoModel;
import com.nhohantu.tcbookbe.model.enums.RoleEnum;
import com.nhohantu.tcbookbe.service.UserBasicInfoService;
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

//        UserBasicInfoModel userInfo;
//        try {
//            userInfo = userBasicInfoService.getUserInfoFromContext();
//            if (userInfo == null) {
//                constraintValidatorContext.disableDefaultConstraintViolation();
//                constraintValidatorContext.buildConstraintViolationWithTemplate("Error validate, can't get user info from context")
//                        .addConstraintViolation();
//                return false;
//            }
//        } catch (Exception e) {
//            constraintValidatorContext.disableDefaultConstraintViolation();
//            constraintValidatorContext.buildConstraintViolationWithTemplate("Error validate when get user info from context")
//                    .addConstraintViolation();
//            return false;
//        }

        if (requestRole.equals(RoleEnum.ROOT)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Can not create user with this role")
                    .addConstraintViolation();
            return false;
        }

//        if (userInfo.getRole().equals(RoleEnum.ROOT) || userInfo.getRole().equals(RoleEnum.ADMIN)) {
//            return true;
//        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate("You do not have permission for creating account with this ROLE").addConstraintViolation();
        return false;
    }

    @Override
    public void initialize(IsValidRole constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }


}
