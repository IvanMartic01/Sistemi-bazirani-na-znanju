package com.ftn.sbnz.app.core.user.visitor;

import com.ftn.sbnz.app.feature.auth.dto.user.request.CreateVisitorDto;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import javax.annotation.processing.Generated;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-16T23:31:37+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class VisitorMapperImpl implements VisitorMapper {

    @Override
    public VisitorEntity toEntity(CreateVisitorDto dto, PasswordEncoder passwordEncoder) {
        if ( dto == null && passwordEncoder == null ) {
            return null;
        }

        VisitorEntity.VisitorEntityBuilder<?, ?> visitorEntity = VisitorEntity.builder();

        if ( dto != null ) {
            visitorEntity.email( dto.getEmail() );
            visitorEntity.password( dto.getPassword() );
            visitorEntity.name( dto.getName() );
        }

        mapEntity( dto, passwordEncoder, visitorEntity );

        return visitorEntity.build();
    }

    @Override
    public VisitorResponseDto toDto(VisitorEntity entity) {
        if ( entity == null ) {
            return null;
        }

        VisitorResponseDto.VisitorResponseDtoBuilder<?, ?> visitorResponseDto = VisitorResponseDto.builder();

        visitorResponseDto.email( entity.getEmail() );
        visitorResponseDto.name( entity.getName() );

        return visitorResponseDto.build();
    }
}
