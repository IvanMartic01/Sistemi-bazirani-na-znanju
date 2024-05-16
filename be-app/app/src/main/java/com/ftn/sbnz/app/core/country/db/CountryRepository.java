package com.ftn.sbnz.app.core.country.db;

import com.ftn.sbnz.model.core.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {
}
