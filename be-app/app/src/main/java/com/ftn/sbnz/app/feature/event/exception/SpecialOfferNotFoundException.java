package com.ftn.sbnz.app.feature.event.exception;

public class SpecialOfferNotFoundException extends RuntimeException {

    public SpecialOfferNotFoundException() {
        super("Special offer not found!");
    }
    public SpecialOfferNotFoundException(String message) {
        super(message);
    }

}
