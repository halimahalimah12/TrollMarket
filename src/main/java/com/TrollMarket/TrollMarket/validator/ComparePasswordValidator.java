package com.TrollMarket.TrollMarket.validator;

import com.TrollMarket.TrollMarket.dtos.auth.AuthRegisterDto;
import com.TrollMarket.TrollMarket.dtos.auth.AuthRegisterSellerBuyerDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ComparePasswordValidator implements ConstraintValidator<ComparePassword, AuthRegisterDto> {
    @Override
    public boolean isValid(AuthRegisterDto authRegisterDto, ConstraintValidatorContext constraintValidatorContext) {
        return authRegisterDto.getPassword().equals(authRegisterDto.getConfirmPassword());
    }
}
