package com.ftn.sbnz.app.feature.auth.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
public class CreateVisitorDto extends CreateUserDto {

    @NotBlank(message = "You must add a country for the visitor!")
    private String countryId;
    private Collection<String> preferences = new HashSet<>();

}
