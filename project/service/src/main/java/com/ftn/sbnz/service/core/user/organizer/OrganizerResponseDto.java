package com.ftn.sbnz.service.core.user.organizer;

import com.ftn.sbnz.service.core.user.abstract_user.UserResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class OrganizerResponseDto extends UserResponseDto {

    private String name;
}
