package com.ftn.sbnz.app.core.user.visitor;

import com.ftn.sbnz.app.feature.auth.dto.user.request.CreateVisitorDto;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import com.ftn.sbnz.model.core.visitor.VisitorEventPreference;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-24T21:55:33+0200",
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
            visitorEntity.preferences( stringCollectionToVisitorEventPreferenceCollection( dto.getPreferences() ) );
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
        visitorResponseDto.money( entity.getMoney() );

        return visitorResponseDto.build();
    }

    protected Collection<VisitorEventPreference> stringCollectionToVisitorEventPreferenceCollection(Collection<String> collection) {
        if ( collection == null ) {
            return null;
        }

        Collection<VisitorEventPreference> collection1 = new ArrayList<VisitorEventPreference>( collection.size() );
        for ( String string : collection ) {
            collection1.add( Enum.valueOf( VisitorEventPreference.class, string ) );
        }

        return collection1;
    }
}
