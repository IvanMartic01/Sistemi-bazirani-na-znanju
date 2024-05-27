package com.ftn.sbnz.app.feature.event.service;

import com.ftn.sbnz.app.feature.event.dto.CreateSpecialOfferRequestDto;
import com.ftn.sbnz.model.event.SpecialOfferEntity;

import java.util.UUID;

public interface SpecialOfferService {

    SpecialOfferEntity saveAndGetEntity(CreateSpecialOfferRequestDto dto);
    SpecialOfferEntity updateAndGetEntity(UUID id, CreateSpecialOfferRequestDto dto);

}
