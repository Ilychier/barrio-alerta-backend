package com.alertabarrio.ingsoft.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BaseBusinessException extends RuntimeException {

    private final HttpStatus status;

    protected BaseBusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
