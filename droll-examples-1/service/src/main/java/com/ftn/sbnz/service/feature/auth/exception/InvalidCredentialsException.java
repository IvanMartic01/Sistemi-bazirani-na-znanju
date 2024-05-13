package com.ftn.sbnz.service.feature.auth.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid credentials!");
    }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
