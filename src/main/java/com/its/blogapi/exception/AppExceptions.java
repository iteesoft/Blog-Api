package com.its.blogapi.exception;

import org.springframework.http.HttpStatus;

public class AppExceptions extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private HttpStatus httpStatus;

    public AppExceptions(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
