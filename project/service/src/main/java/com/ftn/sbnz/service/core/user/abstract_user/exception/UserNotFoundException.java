package com.ftn.sbnz.service.core.user.abstract_user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found!");
    }
}
