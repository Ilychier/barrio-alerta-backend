package com.alertabarrio.domain.exception;

public class EmailNoEnviadoException extends DomainException {
    public EmailNoEnviadoException(String message, Throwable cause) {
        super(message, CodigoError.ERROR_EXTERNO);
        initCause(cause);
    }
}
