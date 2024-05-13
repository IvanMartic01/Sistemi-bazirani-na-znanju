package com.ftn.sbnz.service.feature.event.mapper;


import com.ftn.sbnz.service.core.user.organizer.mapper.OrganizerMapper;
import com.ftn.sbnz.service.feature.event.dto.CreateUpdateEventRequestDto;
import com.ftn.sbnz.service.feature.event.dto.EventResponseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.UUID;

@Mapper(uses = {OrganizerMapper.class})
public interface EventMapper {

    @Mapping(target = "name", source = "dto.name")
    EventEntity toEntity(CreateUpdateEventRequestDto dto, OrganizerEntity organizer);

    EventResponseDto toDto(EventEntity entity);
    Collection<EventResponseDto> toDto (Collection<EventEntity> entities);


    @AfterMapping
    default void mapEntity(CreateUpdateEventRequestDto dto, OrganizerEntity organizer, @MappingTarget EventEntity.EventEntityBuilder entity) {
        entity.id(UUID.randomUUID());
        entity.numberOfAvailableSeats(dto.getTotalSeats());
        entity.organizer(organizer);
    }
}
