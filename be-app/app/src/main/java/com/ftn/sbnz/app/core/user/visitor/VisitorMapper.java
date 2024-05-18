package com.ftn.sbnz.app.core.user.visitor;

import com.ftn.sbnz.app.feature.auth.dto.user.request.CreateVisitorDto;
import com.ftn.sbnz.model.core.Role;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Mapper(uses = {})
public interface VisitorMapper {

    VisitorEntity toEntity(CreateVisitorDto dto, PasswordEncoder passwordEncoder);
    VisitorResponseDto toDto(VisitorEntity entity);

    @AfterMapping
    default void mapEntity(CreateVisitorDto dto, PasswordEncoder passwordEncoder, @MappingTarget VisitorEntity.VisitorEntityBuilder entity) {
        entity.id(UUID.randomUUID());
        entity.password(passwordEncoder.encode(dto.getPassword()));
        entity.role(Role.VISITOR);
    }
}
