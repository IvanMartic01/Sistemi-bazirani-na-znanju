package com.ftn.sbnz.app.feature.event.dto;

import com.ftn.sbnz.app.core.user.visitor.VisitorResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventPurchaseDto {

    private EventResponseDto event;
    private String status;
    private Double purchasePrice;
}
