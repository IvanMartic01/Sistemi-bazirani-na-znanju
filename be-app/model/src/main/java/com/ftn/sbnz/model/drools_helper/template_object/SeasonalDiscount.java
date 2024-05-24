package com.ftn.sbnz.model.drools_helper.template_object;

import com.ftn.sbnz.model.drools_helper.PrecipitationType;
import com.ftn.sbnz.model.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonalDiscount {
    private EventType eventType;
    private double maxTemperature;
    private double minTemperature;
    private PrecipitationType precipitationType;
    private double discount;
}
