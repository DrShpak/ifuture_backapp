package com.ifuture.accountservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class CustomHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error> handeNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(new Error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
