package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class CategoriaInvalidaException extends DomainException {

    public CategoriaInvalidaException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
