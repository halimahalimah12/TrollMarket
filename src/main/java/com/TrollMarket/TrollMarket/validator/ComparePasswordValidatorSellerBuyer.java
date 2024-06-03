package com.TrollMarket.TrollMarket.validator;

import com.TrollMarket.TrollMarket.dtos.auth.AuthRegisterDto;
import com.TrollMarket.TrollMarket.dtos.auth.AuthRegisterSellerBuyerDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ComparePasswordValidatorSellerBuyer implements ConstraintValidator<ComparePasswordSellerBuyer, AuthRegisterSellerBuyerDto> {

    @Override
    public boolean isValid(AuthRegisterSellerBuyerDto authRegisterSellerBuyerDto, ConstraintValidatorContext constraintValidatorContext) {
        return authRegisterSellerBuyerDto.getPassword().equals(authRegisterSellerBuyerDto.getConfirmPassword());
    }
}
