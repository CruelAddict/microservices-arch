package com.microservices.ordersservice.api;

import com.microservices.ordersservice.model.ErrorMessageDto;
import com.microservices.ordersservice.model.exceptions.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class GlobalControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleConflict() {
        return new ResponseEntity<Object>(
                new ErrorMessageDto("Provided data is incorrect"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity handleItemNotFound() {
        return new ResponseEntity<Object>(
                new ErrorMessageDto("Item not found"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}