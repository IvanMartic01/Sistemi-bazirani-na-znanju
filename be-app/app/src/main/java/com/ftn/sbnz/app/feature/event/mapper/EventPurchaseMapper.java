package com.ftn.sbnz.app.feature.event.mapper;

import com.ftn.sbnz.app.feature.event.dto.EventPurchaseDto;
import com.ftn.sbnz.model.event.EventPurchaseEntity;
import org.mapstruct.Mapper;

@Mapper(uses = {EventMapper.class})
public interface EventPurchaseMapper {

    EventPurchaseDto toDto(EventPurchaseEntity entity);
}
