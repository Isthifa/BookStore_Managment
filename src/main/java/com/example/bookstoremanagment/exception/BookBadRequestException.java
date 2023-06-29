package com.example.bookstoremanagment.exception;

public class BookBadRequestException extends RuntimeException{
    public BookBadRequestException() {
    }

    public BookBadRequestException(String message) {
        super(message);
    }

    public BookBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
