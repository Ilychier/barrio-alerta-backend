package com.alertabarrio.domain.exception;

public class ResourceConflictException extends DomainException {

    public ResourceConflictException(String resource, Object value) {
        super(resource + " already exists with value: " + value, CodigoError.CONFLICTO);
    }
}
