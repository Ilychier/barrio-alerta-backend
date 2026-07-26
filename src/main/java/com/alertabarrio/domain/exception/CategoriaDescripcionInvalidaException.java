package com.alertabarrio.domain.exception;

public class CategoriaDescripcionInvalidaException extends DomainException {
    public CategoriaDescripcionInvalidaException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
