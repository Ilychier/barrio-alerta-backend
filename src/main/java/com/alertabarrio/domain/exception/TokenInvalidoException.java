package com.alertabarrio.domain.exception;

public class TokenInvalidoException extends DomainException {
    public TokenInvalidoException(String message) {
        super(message, CodigoError.NO_AUTORIZADO);
    }
}
