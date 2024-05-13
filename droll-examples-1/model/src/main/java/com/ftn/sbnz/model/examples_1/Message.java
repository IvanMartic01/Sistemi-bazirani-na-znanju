package com.ftn.sbnz.model.examples_1;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    public static final int HELLO = 0;
    public static final int GOODBYE = 1;
    public static final int ZDRAVO = 2;

    private String message;
    private int status;
}
