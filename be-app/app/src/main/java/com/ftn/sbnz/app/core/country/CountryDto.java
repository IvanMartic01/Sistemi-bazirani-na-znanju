package com.ftn.sbnz.app.core.country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kie.api.definition.rule.All;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryDto {
    private String id;
    private String name;
    private String city;
}
