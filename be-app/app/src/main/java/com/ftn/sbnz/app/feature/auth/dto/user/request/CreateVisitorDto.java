package com.ftn.sbnz.app.feature.auth.dto.user.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVisitorDto extends CreateUserDto {

    private String countryId;

}
