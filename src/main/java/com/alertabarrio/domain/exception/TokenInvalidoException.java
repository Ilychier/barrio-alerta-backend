package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class TokenInvalidoException extends DomainException {
    public TokenInvalidoException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
