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
        Map<String, Object> body = new HashMap<>();
        body.put("Status", HttpStatus.CONFLICT.value());
        body.put("Message", e.getMessage());
        logger.error("Error adding new supplier : " + e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleSupplierNotFound(SupplierNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("Status", HttpStatus.NOT_FOUND.value());
        body.put("Message", e.getMessage());
        logger.error("Error fetching supplier : " + e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
