package com.ftn.sbnz.app.core.user.visitor;

import com.ftn.sbnz.app.core.user.abstract_user.UserResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class VisitorResponseDto extends UserResponseDto {

    private String name;
}
