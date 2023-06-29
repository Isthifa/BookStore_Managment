package com.example.bookstoremanagment.exception;

public class UserNotFound extends RuntimeException{
    public UserNotFound() {
    }

    public UserNotFound(String message) {
        super(message);
    }

    public UserNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
