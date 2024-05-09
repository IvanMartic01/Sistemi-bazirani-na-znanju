package com.ftn.sbnz.service.core.user.visitor;

import com.ftn.sbnz.service.core.user.abstract_user.db.model.Role;
import com.ftn.sbnz.service.core.user.visitor.db.VisitorEntity;
import com.ftn.sbnz.service.feature.auth.dto.user.request.CreateVisitorDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Mapper(componentModel = "spring")
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
