package com.ftn.sbnz.app.core.other.exception;

public class StartDateIsAfterEndDateException extends RuntimeException {
    public StartDateIsAfterEndDateException() {
        super("Start date is after end date!");
    }

    public StartDateIsAfterEndDateException(String message) {
        super(message);
    }
}
