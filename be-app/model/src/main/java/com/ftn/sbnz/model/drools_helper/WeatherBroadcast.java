package com.ftn.sbnz.model.drools_helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WeatherBroadcast {
    private double temperature;
    private LocalDate date;
    private PrecipitationType precipitation;
}
