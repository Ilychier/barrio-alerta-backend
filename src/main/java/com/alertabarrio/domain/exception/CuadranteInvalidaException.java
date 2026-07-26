package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class CuadranteInvalidaException extends DomainException {

    public CuadranteInvalidaException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
