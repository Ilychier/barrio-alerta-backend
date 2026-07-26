package com.alertabarrio.domain.exception;

public class CuadranteInvalidaException extends DomainException {

    public CuadranteInvalidaException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
