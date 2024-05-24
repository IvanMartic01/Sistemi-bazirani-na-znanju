package com.ftn.sbnz.app.feature.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSpecialOfferRequestDto {
    private double discount;
    private String type;
}
