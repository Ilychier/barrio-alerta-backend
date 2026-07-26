package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class ConfiguracionInvalidaException extends DomainException {
    public ConfiguracionInvalidaException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
