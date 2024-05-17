package com.ftn.sbnz.app.core.user.organizer.mapper;

import com.ftn.sbnz.app.core.user.organizer.OrganizerResponseDto;
import com.ftn.sbnz.app.feature.auth.dto.user.request.CreateOrganizerDto;
import com.ftn.sbnz.model.core.OrganizerEntity;
import javax.annotation.processing.Generated;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-17T17:10:16+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class OrganizerMapperImpl implements OrganizerMapper {

    @Override
    public OrganizerEntity toEntity(CreateOrganizerDto dto, PasswordEncoder encoder) {
        if ( dto == null && encoder == null ) {
            return null;
        }

        OrganizerEntity.OrganizerEntityBuilder<?, ?> organizerEntity = OrganizerEntity.builder();

        if ( dto != null ) {
            organizerEntity.email( dto.getEmail() );
            organizerEntity.password( dto.getPassword() );
            organizerEntity.name( dto.getName() );
        }

        mapEntity( dto, encoder, organizerEntity );

        return organizerEntity.build();
    }

    @Override
    public OrganizerResponseDto toDto(OrganizerEntity entity) {
        if ( entity == null ) {
            return null;
        }

        OrganizerResponseDto.OrganizerResponseDtoBuilder<?, ?> organizerResponseDto = OrganizerResponseDto.builder();

        organizerResponseDto.email( entity.getEmail() );
        organizerResponseDto.name( entity.getName() );

        return organizerResponseDto.build();
    }
}
