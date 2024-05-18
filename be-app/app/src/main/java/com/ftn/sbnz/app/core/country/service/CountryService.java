package com.ftn.sbnz.app.core.country.service;

import com.ftn.sbnz.app.core.country.CountryDto;
import com.ftn.sbnz.model.core.CountryEntity;

import java.util.Collection;
import java.util.UUID;

public interface CountryService {

    CountryEntity getEntityById(UUID id);
    Collection<CountryDto> getAllCountries();

}
