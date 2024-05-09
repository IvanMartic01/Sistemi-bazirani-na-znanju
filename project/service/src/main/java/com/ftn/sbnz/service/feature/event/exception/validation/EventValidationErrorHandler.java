package com.ftn.sbnz.service.feature.event.exception.validation;


import com.ftn.sbnz.service.core.other.dto.ErrorResponseDto;
import com.ftn.sbnz.service.feature.event.exception.EventException;
import com.ftn.sbnz.service.feature.event.exception.EventNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventValidationErrorHandler {

    @ExceptionHandler({EventException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponseDto> handleEventException(EventException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EventNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponseDto> handleEventNotFoundException(EventNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto(e.getMessage()), HttpStatus.NOT_FOUND);
    }


}
