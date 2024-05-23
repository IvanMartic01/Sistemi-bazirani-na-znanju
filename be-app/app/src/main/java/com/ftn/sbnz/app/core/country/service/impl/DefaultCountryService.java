package com.ftn.sbnz.app.core.country.service.impl;

import com.ftn.sbnz.app.core.country.CountryDto;
import com.ftn.sbnz.app.core.country.CountryMapper;
import com.ftn.sbnz.app.core.country.db.CountryRepository;
import com.ftn.sbnz.app.core.country.service.CountryService;
import com.ftn.sbnz.model.core.CountryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DefaultCountryService implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public CountryEntity getEntityById(UUID id) {
        // TODO: Replace RunTimeException
        return countryRepository.findById(id).orElseThrow(() -> new RuntimeException("Country not found!"));
    }



    @Override
    public Collection<CountryDto> getAll() {
        return countryRepository.findAll().stream().map(countryMapper::toDto).toList();
    }

    @Override
    public CountryDto getById(UUID id) {
        return countryMapper.toDto(getEntityById(id));
    }
}
