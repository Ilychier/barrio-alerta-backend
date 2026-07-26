package com.alertabarrio.domain.exception;

import org.springframework.http.HttpStatus;

public class ResourceConflictException extends DomainException {

    public ResourceConflictException(String resource, Object value) {
        super(resource + " already exists with value: " + value, HttpStatus.CONFLICT);
    }
}
