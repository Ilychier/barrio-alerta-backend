package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class EvidenciaInvalidaException extends DomainException {
    public EvidenciaInvalidaException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
