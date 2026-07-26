package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception for all domain errors.
 * Extends RuntimeException so that @Transactional in use cases performs rollback automatically.
 */
public abstract class DomainException extends RuntimeException {

    private final HttpStatus status;

    protected DomainException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
