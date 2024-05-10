package com.ftn.sbnz.app.core.user.organizer.mapper;

import com.ftn.sbnz.app.core.user.abstract_user.db.model.Role;
import com.ftn.sbnz.app.core.user.organizer.OrganizerResponseDto;
import com.ftn.sbnz.app.core.user.organizer.db.OrganizerEntity;
import com.ftn.sbnz.app.feature.auth.dto.user.request.CreateOrganizerDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Mapper
public interface OrganizerMapper {

    OrganizerEntity toEntity(CreateOrganizerDto dto, PasswordEncoder encoder);

    OrganizerResponseDto toDto(OrganizerEntity entity);

    @AfterMapping
    default void mapEntity(CreateOrganizerDto dto, PasswordEncoder encoder, @MappingTarget OrganizerEntity.OrganizerEntityBuilder entity) {
        entity.id(UUID.randomUUID());
        entity.password(encoder.encode(dto.getPassword()));
        entity.role(Role.ORGANIZER);
    }
}
