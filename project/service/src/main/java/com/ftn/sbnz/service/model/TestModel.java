package com.ftn.sbnz.service.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Builder(toBuilder = true)
@Getter
public class TestModel {

    private UUID id;
    private String name;
    private int age;
}
