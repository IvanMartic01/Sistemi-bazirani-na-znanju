package com.ftn.sbnz.model.examples_1;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Discount implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double percentage;
}
