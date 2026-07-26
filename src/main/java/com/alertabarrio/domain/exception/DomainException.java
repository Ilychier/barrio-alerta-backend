package com.alertabarrio.domain.exception;

/**
 * Base exception for all domain errors.
 * Extends RuntimeException so that @Transactional in use cases performs rollback automatically.
 */
public abstract class DomainException extends RuntimeException {

    private final CodigoError codigo;

    protected DomainException(String message, CodigoError codigo) {
        super(message);
        this.codigo = codigo;
    }

    public CodigoError getCodigo() {
        return codigo;
    }
}
