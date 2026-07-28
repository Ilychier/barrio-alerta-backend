package com.alertabarrio.domain.exception;

public class BarrioInvalidoException extends DomainException {
    public BarrioInvalidoException(String message) {
        super(message, CodigoError.VALIDACION);
    }
}
