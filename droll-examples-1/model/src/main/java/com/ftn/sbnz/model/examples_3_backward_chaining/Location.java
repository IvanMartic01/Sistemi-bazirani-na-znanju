package com.ftn.sbnz.model.examples_3_backward_chaining;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.kie.api.definition.type.Position;

@Data
@AllArgsConstructor
public class Location {

    @Position(0)
    private String item;

    @Position(1)
    private String location;
}