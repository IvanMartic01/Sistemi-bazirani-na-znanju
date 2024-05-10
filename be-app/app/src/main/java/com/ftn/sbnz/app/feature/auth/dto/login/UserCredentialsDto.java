package com.ftn.sbnz.app.feature.auth.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserCredentialsDto {

    @Email(message = "Invalid email format!")
    @NotBlank(message = "Email not provided!")
    private String email;

    @NotBlank(message = "Password not provided")
    private String password;
}
