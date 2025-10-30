package com.example.tricolv2sb.Controller;

import com.example.tricolv2sb.Controller.ControllerInterfaces.SupplierControllerInterface;
import com.example.tricolv2sb.DTO.CreateSupplierDTO;
import com.example.tricolv2sb.DTO.ReadSupplierDTO;
import com.example.tricolv2sb.Service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Supplier")
@RequiredArgsConstructor
public class SupplierController implements SupplierControllerInterface {
    private final SupplierService supplierService;

    @PostMapping("/addNew")
    public ResponseEntity<?> addNewSupplier(@Valid @RequestBody CreateSupplierDTO dto) {
        try {
            ReadSupplierDTO newSupplier = this.supplierService.addSupplier(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newSupplier);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error: " + e.getMessage());
        }
    }
}
