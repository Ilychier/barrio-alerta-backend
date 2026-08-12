package com.alertabarrio.domain.exception;

public class ArchivoInvalidoException extends DomainException {
    public ArchivoInvalidoException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
