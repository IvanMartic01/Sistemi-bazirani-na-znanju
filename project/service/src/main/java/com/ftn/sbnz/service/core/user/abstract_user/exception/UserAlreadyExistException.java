package com.ftn.sbnz.service.core.user.abstract_user.exception;

public class UserAlreadyExistException extends RuntimeException{

public UserAlreadyExistException() {
        super("User already exist");
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
