package com.example.tricolv2sb.Controller.ControllerInterfaces;

import com.example.tricolv2sb.DTO.CreateSupplierDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface SupplierControllerInterface {
    ResponseEntity<?> addNewSupplier(@Valid @RequestBody CreateSupplierDTO dto);
}
