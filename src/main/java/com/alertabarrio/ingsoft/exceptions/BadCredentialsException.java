package com.alertabarrio.ingsoft.exceptions;

import org.springframework.http.HttpStatus;

public class BadCredentialsException extends BaseBusinessException {

    public BadCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
