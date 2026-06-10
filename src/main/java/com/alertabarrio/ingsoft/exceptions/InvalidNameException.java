package com.alertabarrio.ingsoft.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidNameException extends BaseBusinessException {
    
    public InvalidNameException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
