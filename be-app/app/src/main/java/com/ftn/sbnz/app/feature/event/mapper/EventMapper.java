package com.ftn.sbnz.app.feature.event.mapper;

import com.ftn.sbnz.app.core.user.organizer.db.OrganizerEntity;
import com.ftn.sbnz.app.core.user.organizer.mapper.OrganizerMapper;
import com.ftn.sbnz.app.feature.event.dto.CreateUpdateEventRequestDto;
import com.ftn.sbnz.app.feature.event.dto.EventResponseDto;
import com.ftn.sbnz.app.feature.event.model.EventEntity;
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

    @Mapping(target = "totalSeats", source = "totalSeats")
    EventResponseDto toDto(EventEntity entity);
    @Mapping(target = "totalSeats", source = "totalSeats")
    Collection<EventResponseDto> toDto (Collection<EventEntity> entities);


    @AfterMapping
    default void mapEntity(CreateUpdateEventRequestDto dto, OrganizerEntity organizer, @MappingTarget EventEntity.EventEntityBuilder entity) {
        entity.id(UUID.randomUUID());
        entity.numberOfAvailableSeats(dto.getTotalSeats());
        entity.organizer(organizer);
    }
}
