package com.ftn.sbnz.app.feature.event.exception;

public class CountryNotFoundException extends RuntimeException {

    public CountryNotFoundException() {
        super("Country not found!");
    }

    public CountryNotFoundException(String message) {
        super(message);
    }
}
