package com.TrollMarket.TrollMarket.dtos.auth;

import com.TrollMarket.TrollMarket.validator.ComparePassword;
import com.TrollMarket.TrollMarket.validator.Username;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ComparePassword
public class AuthRegisterDto {
    @Username
    @NotNull
    @NotBlank(message = "Please, Entry Your username!")
    public String username;
    @NotBlank(message = "Please, Entry Your password!")
    @Size(min = 8, max = 20, message = "Please enter a password between 8 and 20!")
    public String password;
    @NotBlank(message = "Please, Entry Your confirm password!")
    @Size(min = 8, max = 20, message = "Please enter a password between 8 and 20!")
    public String confirmPassword;

}
