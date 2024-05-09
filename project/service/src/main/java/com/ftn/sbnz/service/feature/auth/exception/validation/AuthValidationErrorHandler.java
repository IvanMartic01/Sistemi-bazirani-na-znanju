package com.ftn.sbnz.service.feature.auth.exception.validation;


import com.ftn.sbnz.service.core.other.dto.ErrorResponseDto;
import com.ftn.sbnz.service.feature.auth.exception.PasswordMismatchException;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthValidationErrorHandler {

    @ExceptionHandler({InvalidCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PasswordMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handlePasswordMismatchException(PasswordMismatchException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
