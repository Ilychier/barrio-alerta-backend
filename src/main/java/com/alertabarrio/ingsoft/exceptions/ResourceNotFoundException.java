package com.alertabarrio.ingsoft.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseBusinessException {
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " with ID " + id + " not found in the database.", HttpStatus.NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
