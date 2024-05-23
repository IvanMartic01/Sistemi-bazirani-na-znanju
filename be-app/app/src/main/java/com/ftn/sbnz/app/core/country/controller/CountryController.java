package com.ftn.sbnz.app.core.country.controller;

import com.ftn.sbnz.app.core.country.CountryDto;
import com.ftn.sbnz.app.core.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/country")
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/all")
    public ResponseEntity<Collection<CountryDto>> getAll() {
        return ResponseEntity.ok(countryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(countryService.getById(id));
    }
}
