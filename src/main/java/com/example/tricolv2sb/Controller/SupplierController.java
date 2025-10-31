package com.example.tricolv2sb.Controller;

import com.example.tricolv2sb.DTO.CreateSupplierDTO;
import com.example.tricolv2sb.DTO.ReadSupplierDTO;
import com.example.tricolv2sb.Service.ServiceInterfaces.SupplierServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierServiceInterface supplierService;

    @PostMapping("/create")
    public ResponseEntity<ReadSupplierDTO> addNewSupplier(@Valid @RequestBody CreateSupplierDTO dto) {
        ReadSupplierDTO newSupplier = supplierService.addSupplier(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSupplier);
    }


    @GetMapping("/read/{id}")
    public ResponseEntity<ReadSupplierDTO> getExistingSupplier(@PathVariable Long id) {
        return supplierService.fetchSupplier(id)
                .map(supplierDTO -> ResponseEntity.ok().body(supplierDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removeExistingSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok("Supplier with id: " + id + " successfully deleted.");
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateExistingSupplier(@PathVariable Long id, @Valid @RequestBody CreateSupplierDTO dto) {
        ReadSupplierDTO updatedSupplier = supplierService.updateSupplier(id, dto);
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Supplier updated successfully");
        body.put("updatedSupplier", updatedSupplier);
        return ResponseEntity.ok(body);
    }

}
