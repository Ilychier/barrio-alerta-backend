package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class BarrioInvalidoException extends DomainException {
    public BarrioInvalidoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
