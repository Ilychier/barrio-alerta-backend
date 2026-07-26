package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class CategoriaDescripcionInvalidaException extends DomainException {
    public CategoriaDescripcionInvalidaException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
