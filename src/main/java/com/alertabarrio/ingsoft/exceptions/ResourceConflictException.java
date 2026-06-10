package com.alertabarrio.ingsoft.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceConflictException extends BaseBusinessException {
    
    public ResourceConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}
