package com.alertabarrio.domain.exception;

public class CategoriaInvalidaException extends DomainException {

    public CategoriaInvalidaException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
