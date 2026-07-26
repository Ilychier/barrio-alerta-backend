package com.alertabarrio.domain.exception;

public class EvidenciaInvalidaException extends DomainException {
    public EvidenciaInvalidaException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
