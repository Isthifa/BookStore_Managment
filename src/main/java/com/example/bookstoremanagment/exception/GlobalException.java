package com.example.bookstoremanagment.exception;

import com.example.bookstoremanagment.dto.responseDTO.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(BookNotFoundException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(new Date())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BookBadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleExceptionBadRequest(BookBadRequestException ex){
        ExceptionResponse exceptionResponse= ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .timestamp(new Date()).build();
        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ExceptionResponse> handleExceptionBadRequest(UserNotFound ex){
        ExceptionResponse exceptionResponse= ExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(new Date()).build();
        return new ResponseEntity<>(exceptionResponse,HttpStatus.NOT_FOUND);
    }
}
