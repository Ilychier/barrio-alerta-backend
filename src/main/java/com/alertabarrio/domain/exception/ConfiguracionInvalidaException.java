package com.alertabarrio.domain.exception;

public class ConfiguracionInvalidaException extends DomainException {
    public ConfiguracionInvalidaException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
