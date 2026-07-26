package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class AlertaInvalidaException extends DomainException {
    public AlertaInvalidaException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
