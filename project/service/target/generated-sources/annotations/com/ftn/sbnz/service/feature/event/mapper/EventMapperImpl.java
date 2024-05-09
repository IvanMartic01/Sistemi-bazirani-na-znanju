package com.ftn.sbnz.service.feature.event.mapper;

import com.ftn.sbnz.service.core.user.organizer.db.OrganizerEntity;
import com.ftn.sbnz.service.core.user.organizer.mapper.OrganizerMapper;
import com.ftn.sbnz.service.feature.event.dto.CreateUpdateEventRequestDto;
import com.ftn.sbnz.service.feature.event.dto.EventResponseDto;
import com.ftn.sbnz.service.feature.event.model.EventEntity;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-09T05:51:53+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class EventMapperImpl implements EventMapper {

    private final OrganizerMapper organizerMapper;

    @Autowired
    public EventMapperImpl(OrganizerMapper organizerMapper) {

        this.organizerMapper = organizerMapper;
    }

    @Override
    public EventEntity toEntity(CreateUpdateEventRequestDto dto, OrganizerEntity organizer) {
        if ( dto == null && organizer == null ) {
            return null;
        }

        EventEntity.EventEntityBuilder eventEntity = EventEntity.builder();

        if ( dto != null ) {
            eventEntity.name( dto.getName() );
            eventEntity.startDateTime( dto.getStartDateTime() );
            eventEntity.endDateTime( dto.getEndDateTime() );
            eventEntity.totalSeats( dto.getTotalSeats() );
            eventEntity.price( dto.getPrice() );
            eventEntity.shortDescription( dto.getShortDescription() );
            eventEntity.detailedDescription( dto.getDetailedDescription() );
            eventEntity.organizationPlan( dto.getOrganizationPlan() );
        }
        if ( organizer != null ) {
            eventEntity.id( organizer.getId() );
        }

        mapEntity( dto, organizer, eventEntity );

        return eventEntity.build();
    }

    @Override
    public EventResponseDto toDto(EventEntity entity) {
        if ( entity == null ) {
            return null;
        }

        EventResponseDto.EventResponseDtoBuilder<?, ?> eventResponseDto = EventResponseDto.builder();

        eventResponseDto.id( entity.getId() );
        eventResponseDto.name( entity.getName() );
        if ( entity.getStartDateTime() != null ) {
            eventResponseDto.startDateTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( entity.getStartDateTime() ) );
        }
        if ( entity.getEndDateTime() != null ) {
            eventResponseDto.endDateTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( entity.getEndDateTime() ) );
        }
        eventResponseDto.price( entity.getPrice() );
        eventResponseDto.shortDescription( entity.getShortDescription() );
        eventResponseDto.detailedDescription( entity.getDetailedDescription() );
        eventResponseDto.organizationPlan( entity.getOrganizationPlan() );
        eventResponseDto.organizer( organizerMapper.toDto( entity.getOrganizer() ) );
        eventResponseDto.numberOfAvailableSeats( entity.getNumberOfAvailableSeats() );

        return eventResponseDto.build();
    }

    @Override
    public Collection<EventResponseDto> toDto(Collection<EventEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        Collection<EventResponseDto> collection = new ArrayList<EventResponseDto>( entities.size() );
        for ( EventEntity eventEntity : entities ) {
            collection.add( toDto( eventEntity ) );
        }

        return collection;
    }
}
