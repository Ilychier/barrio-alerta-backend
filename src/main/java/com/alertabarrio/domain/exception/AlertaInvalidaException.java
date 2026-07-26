package com.alertabarrio.domain.exception;

public class AlertaInvalidaException extends DomainException {
    public AlertaInvalidaException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
