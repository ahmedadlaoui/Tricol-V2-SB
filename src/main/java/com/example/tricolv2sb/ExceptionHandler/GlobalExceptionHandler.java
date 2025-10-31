package com.example.tricolv2sb.ExceptionHandler;

import com.example.tricolv2sb.Exception.SupplierAlreadyExistsException;
import com.example.tricolv2sb.Exception.SupplierNotFoundException;
import com.example.tricolv2sb.Service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(SupplierService.class);

    @ExceptionHandler(SupplierAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleSupplierConflict(SupplierAlreadyExistsException e) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Error adding new supplier : ", e);
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSupplierNotFound(SupplierNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Error fetching supplier : ", e);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String logPrefix, Exception e) {
        logger.error(logPrefix + e.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("Status", status.value());
        body.put("Message", e.getMessage());

        return ResponseEntity.status(status).body(body);
    }

}
