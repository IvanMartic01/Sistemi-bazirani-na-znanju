package com.ftn.sbnz.model.examples_1;

import lombok.*;
import org.kie.api.definition.type.PropertyReactive;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@PropertyReactive
public class Item implements Serializable {

    public enum Category {
        NA, LOW_RANGE, MID_RANGE, HIGH_RANGE,
        SPECIAL_MIDHIGH_RANGE //used in chapter 4
    };
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Double cost;
    private Double salePrice;
    private Category category;

    public Item(String name, Double cost, Double salePrice) {
        this(null, name, cost, salePrice);
    }
    
    public Item(Long id, String name, Double cost, Double salePrice) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.salePrice = salePrice;
        this.category = Category.NA;
    }
}
