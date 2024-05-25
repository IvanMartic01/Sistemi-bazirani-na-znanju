package com.ftn.sbnz.app.feature.event.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSpecialOfferRequestDto {
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount must be greater than 0%")
    @DecimalMax(value = "100.0", inclusive = false, message = "Discount must be lesser than 100%")
    private double discount;
    private String type;
}
