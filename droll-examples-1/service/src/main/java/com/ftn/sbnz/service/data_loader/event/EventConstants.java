package com.ftn.sbnz.service.data_loader.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventConstants {

    public static final UUID FINISHED_EVENT_ID = UUID.fromString("bf130c9f-a842-4863-9ece-e7195ec07341");
    public static final UUID PENDING_EVENT_ID = UUID.fromString("69925184-4037-421f-84f4-e5d9d5d40372");
}
