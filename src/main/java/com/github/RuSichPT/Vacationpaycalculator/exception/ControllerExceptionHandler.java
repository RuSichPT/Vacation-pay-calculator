package com.github.RuSichPT.Vacationpaycalculator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Error> handlerResponseStatusException(ResponseStatusException exception) {
        return new ResponseEntity<>(new Error(exception.getReason()), exception.getStatusCode());
    }
}
