package com.ftn.sbnz.app.core.user.abstract_user.exception;

public class UserAlreadyExistException extends RuntimeException{

public UserAlreadyExistException() {
        super("User already exist");
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
