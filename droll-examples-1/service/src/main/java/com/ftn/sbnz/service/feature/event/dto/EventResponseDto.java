package com.ftn.sbnz.service.feature.event.dto;

import com.ftn.sbnz.service.core.user.organizer.OrganizerResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@SuperBuilder(toBuilder = true)
public class EventResponseDto {

    private UUID id;
    private String name;
    private String startDateTime;
    private String endDateTime;
    private int totalSeats;
    private int numberOfAvailableSeats;
    private double price;
    private String shortDescription;
    private String detailedDescription;
    private String organizationPlan;
    private OrganizerResponseDto organizer;
}
