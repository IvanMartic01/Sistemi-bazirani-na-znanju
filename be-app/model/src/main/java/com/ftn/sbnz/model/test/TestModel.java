package com.ftn.sbnz.model.test;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@Builder(toBuilder = true)
public class TestModel {

    private UUID id;
    private String name;
    private int age;
}
