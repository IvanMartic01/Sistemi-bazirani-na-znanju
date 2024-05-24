package com.ftn.sbnz.app.feature.event.mapper;

import com.ftn.sbnz.app.feature.event.dto.EventPurchaseDto;
import com.ftn.sbnz.model.event.EventPurchaseEntity;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-24T20:42:50+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class EventPurchaseMapperImpl implements EventPurchaseMapper {

    private final EventMapper eventMapper;

    @Autowired
    public EventPurchaseMapperImpl(EventMapper eventMapper) {

        this.eventMapper = eventMapper;
    }

    @Override
    public EventPurchaseDto toDto(EventPurchaseEntity entity) {
        if ( entity == null ) {
            return null;
        }

        EventPurchaseDto eventPurchaseDto = new EventPurchaseDto();

        eventPurchaseDto.setEvent( eventMapper.toDto( entity.getEvent() ) );
        if ( entity.getStatus() != null ) {
            eventPurchaseDto.setStatus( entity.getStatus().name() );
        }
        eventPurchaseDto.setPurchasePrice( entity.getPurchasePrice() );

        return eventPurchaseDto;
    }
}
