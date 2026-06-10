package com.alertabarrio.ingsoft.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<Map<String, String>>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errorList = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, String> errorDetail = new HashMap<>();
            errorDetail.put("name", error.getDefaultMessage());
            errorList.add(errorDetail);
        });

        return buildResponse(errorList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseBusinessException.class)
    public ResponseEntity<Map<String, List<Map<String, String>>>> handleBusinessExceptions(BaseBusinessException ex) {
        List<Map<String, String>> errorList = new ArrayList<>();
        Map<String, String> errorDetail = new HashMap<>();
        
        errorDetail.put("name", ex.getMessage());
        errorList.add(errorDetail);

        return buildResponse(errorList, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, List<Map<String, String>>>> handleGeneralExceptions(Exception ex) {
        List<Map<String, String>> errorList = new ArrayList<>();
        Map<String, String> errorDetail = new HashMap<>();
        
        errorDetail.put("name", "An error has ocurred in the server: " + ex.getMessage());
        errorList.add(errorDetail);

        return buildResponse(errorList, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, List<Map<String, String>>>> buildResponse(List<Map<String, String>> errors, HttpStatus status) {
        Map<String, List<Map<String, String>>> response = new HashMap<>();
        response.put("errors", errors);
        return new ResponseEntity<>(response, status);
    }

}
