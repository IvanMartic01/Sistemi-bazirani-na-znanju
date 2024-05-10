package com.ftn.sbnz.app.core.user.organizer;

import com.ftn.sbnz.app.core.user.abstract_user.UserResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class OrganizerResponseDto extends UserResponseDto {

    private String name;
}
