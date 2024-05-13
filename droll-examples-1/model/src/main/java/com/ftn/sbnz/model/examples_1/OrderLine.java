package com.ftn.sbnz.model.examples_1;

import lombok.*;
import org.kie.api.definition.type.PropertyReactive;

import java.io.Serializable;

@Data
@PropertyReactive
public class OrderLine implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Item item;
    private Integer quantity = 1;
}
