package com.ftn.sbnz.app.feature.auth.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public abstract class CreateUserDto {

    @NotBlank(message = "Name not provided!")
    protected String name;

    @Email(message = "Invalid email format!")
    @NotBlank(message = "Email not provided!")
    protected String email;
    @Pattern.List({
            @Pattern(regexp = "^(?=.{8,20}).+", message = "Password must be between 8 and 20 characters!"),
            @Pattern(regexp = "^(?=.*[0-9]).+", message = "Password must contain at least one number!"),
            @Pattern(regexp = "^(?=.*[a-z]).+", message = "Password must contain at least one lowercase letter!"),
            @Pattern(regexp = "^(?=.*[A-Z]).+", message = "Password must contain at least one uppercase letter!")
    })
    @NotNull(message = "Password not provided!")
    protected String password;
}
