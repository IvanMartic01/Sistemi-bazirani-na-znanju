package com.ftn.sbnz.app.core.user.abstract_user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found!");
    }
}
