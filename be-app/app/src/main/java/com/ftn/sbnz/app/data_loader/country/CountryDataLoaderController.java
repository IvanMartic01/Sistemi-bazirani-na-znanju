package com.ftn.sbnz.app.data_loader.country;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/load-country-data")
public class CountryDataLoaderController {

    private final CountryDataLoader countryDataLoader;

    @PostMapping
    public void loadData() {
        countryDataLoader.load();
    }
}
