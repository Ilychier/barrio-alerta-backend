package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class EmailNoEnviadoException extends DomainException {
    public EmailNoEnviadoException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_GATEWAY);
        initCause(cause);
    }
}
