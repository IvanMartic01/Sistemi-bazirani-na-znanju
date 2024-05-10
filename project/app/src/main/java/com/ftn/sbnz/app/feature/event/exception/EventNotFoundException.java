package com.ftn.sbnz.app.feature.event.exception;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException() {
        super("Event not found");
    }
}
