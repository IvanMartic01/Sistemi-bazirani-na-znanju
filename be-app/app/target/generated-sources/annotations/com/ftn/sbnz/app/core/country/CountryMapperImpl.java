package com.ftn.sbnz.app.core.country;

import com.ftn.sbnz.model.core.CountryEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-18T13:14:35+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class CountryMapperImpl implements CountryMapper {

    @Override
    public CountryDto toDto(CountryEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CountryDto.CountryDtoBuilder countryDto = CountryDto.builder();

        if ( entity.getId() != null ) {
            countryDto.id( entity.getId().toString() );
        }
        countryDto.name( entity.getName() );
        countryDto.city( entity.getCity() );

        return countryDto.build();
    }
}
