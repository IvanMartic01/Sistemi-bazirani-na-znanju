package com.ftn.sbnz.model.examples_1;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.kie.api.definition.type.PropertyReactive;

@Data
@AllArgsConstructor
@PropertyReactive
public class Person {
    private String name;
    private int age;
}