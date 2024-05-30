package com.ftn.sbnz.app.feature.event.service.impl;

import com.ftn.sbnz.app.feature.event.dto.CreateSpecialOfferRequestDto;
import com.ftn.sbnz.app.feature.event.exception.SpecialOfferNotFoundException;
import com.ftn.sbnz.app.feature.event.repository.SpecialOfferRepository;
import com.ftn.sbnz.app.feature.event.service.SpecialOfferService;
import com.ftn.sbnz.model.event.SpecialOfferEntity;
import com.ftn.sbnz.model.event.SpecialOfferType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DefaultSpecialOfferService implements SpecialOfferService {

    private final SpecialOfferRepository specialOfferRepository;

    @Override
    public SpecialOfferEntity saveAndGetEntity(CreateSpecialOfferRequestDto dto) {
        double specialOfferDiscount = dto.getDiscount();
        SpecialOfferType specialOfferType = Enum.valueOf(SpecialOfferType.class, dto.getType());
        return specialOfferRepository.save(SpecialOfferEntity.builder()
                    .discount(specialOfferDiscount)
                    .type(specialOfferType)
                    .build());
    }

    @Override
    public SpecialOfferEntity updateAndGetEntity(UUID id, CreateSpecialOfferRequestDto dto) {
        SpecialOfferEntity specialOffer = specialOfferRepository.findById(id)
                .orElseThrow(SpecialOfferNotFoundException::new);

        double specialOfferDiscount = dto.getDiscount();
        SpecialOfferType specialOfferType = Enum.valueOf(SpecialOfferType.class, dto.getType());
        specialOffer.setType(specialOfferType);
        specialOffer.setDiscount(specialOfferDiscount);
        return specialOfferRepository.save(specialOffer);
    }

    @Override
    public SpecialOfferEntity getEntityById(UUID id) {
        return specialOfferRepository.findById(id).orElseThrow(SpecialOfferNotFoundException::new);
    }

    @Override
    public SpecialOfferEntity save(SpecialOfferEntity entity) {
        return specialOfferRepository.save(entity);
    }


}
