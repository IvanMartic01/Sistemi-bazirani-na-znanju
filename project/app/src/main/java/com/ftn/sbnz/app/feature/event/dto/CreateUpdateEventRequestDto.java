package com.ftn.sbnz.app.feature.event.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateUpdateEventRequestDto {

    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Start date and time is mandatory")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date and time is mandatory")
    private LocalDateTime endDateTime;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private double price;

    @Min(value = 1, message = "Seats must be greater than or equal to 1")
    private int totalSeats;

    @NotBlank(message = "Short description is mandatory")
    @NotNull(message = "Short description is mandatory")
    private String shortDescription;

    @NotBlank(message = "Detailed description is mandatory")
    @NotNull(message = "Detailed description is mandatory")
    private String detailedDescription;

    @NotBlank(message = "Organization plan is mandatory")
    @NotNull(message = "Organization plan is mandatory")
    private String organizationPlan;
}
