package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class UsuarioInvalidoException extends DomainException {
    public UsuarioInvalidoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
