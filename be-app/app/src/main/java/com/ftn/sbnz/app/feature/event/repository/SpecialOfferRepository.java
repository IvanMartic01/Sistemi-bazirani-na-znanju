package com.ftn.sbnz.app.feature.event.repository;

import com.ftn.sbnz.model.event.SpecialOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialOfferRepository extends JpaRepository<SpecialOfferEntity, UUID> {
}
