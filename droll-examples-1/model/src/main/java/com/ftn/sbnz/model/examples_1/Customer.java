package com.ftn.sbnz.model.examples_1;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
public class Customer implements Serializable {

    public enum Category {
        NA, GOLD, SILVER, BRONZE
    };
    private static final long serialVersionUID = 1L;

    private Long customerId;
    private Integer age;
    private String name;
    private String email;
    private Category category = Category.NA;
}
