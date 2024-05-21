package com.ftn.sbnz.model.event.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventScaleUpPrice {

    private double occupancyMin;
    private double occupancyMax;
    private double priceIncrease;
}
