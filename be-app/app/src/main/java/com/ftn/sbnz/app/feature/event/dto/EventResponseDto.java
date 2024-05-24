package com.ftn.sbnz.app.feature.event.dto;

import com.ftn.sbnz.app.core.user.organizer.OrganizerResponseDto;
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
    private String type;
}
