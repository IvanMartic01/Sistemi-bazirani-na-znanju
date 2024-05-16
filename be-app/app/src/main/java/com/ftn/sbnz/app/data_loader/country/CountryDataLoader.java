package com.ftn.sbnz.app.data_loader.country;

import com.ftn.sbnz.app.core.country.db.CountryRepository;
import com.ftn.sbnz.model.core.CountryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.ftn.sbnz.app.data_loader.country.CountryDataConstants.*;

@RequiredArgsConstructor
@Component
public class CountryDataLoader {

    private final CountryRepository countryRepository;

    public void load() {
        countryRepository.save(getCountry_1());
        countryRepository.save(getCountry_2());
    }

    private CountryEntity getCountry_1() {
        return CountryEntity.builder()
                .id(COUNTRY_ID_1)
                .name(COUNTRY_NAME_1)
                .city(COUNTRY_CITY_1)
                .build();
    }

    private CountryEntity getCountry_2() {
        return CountryEntity.builder()
                .id(COUNTRY_ID_2)
                .name(COUNTRY_NAME_2)
                .city(COUNTRY_CITY_2)
                .build();
    }
}
