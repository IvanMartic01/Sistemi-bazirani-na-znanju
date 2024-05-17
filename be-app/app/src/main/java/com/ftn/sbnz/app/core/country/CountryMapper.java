package com.ftn.sbnz.app.core.country;

import com.ftn.sbnz.model.core.CountryEntity;
import org.mapstruct.Mapper;

@Mapper
public interface CountryMapper {

    CountryDto toDto(CountryEntity entity);

}
